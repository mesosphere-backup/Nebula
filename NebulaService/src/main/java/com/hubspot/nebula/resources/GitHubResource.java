package com.hubspot.nebula.resources;

import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hubspot.nebula.NebulaServiceModule;
import com.hubspot.nebula.github.GitHubCommit;
import com.hubspot.nebula.github.GitHubHelper;
import com.hubspot.nebula.github.WebhookPayload;
import com.hubspot.nebula.registry.BuildRegistryDAO;
import com.hubspot.nebula.registry.DeployRegistryDAO;
import com.hubspot.nebula.registry.RegistryEntry;

@Path("/github")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class GitHubResource {
  private static final Logger LOG = LoggerFactory.getLogger(GitHubResource.class);

  private final DeployRegistryDAO deployRegistryDAO;
  private final BuildRegistryDAO buildRegistryDAO;

  private final Optional<Pattern> buildRegistryRegex;
  private final Optional<Pattern> deployRegistryRegex;

  @Inject
  public GitHubResource(DeployRegistryDAO deployRegistryDAO,
                        BuildRegistryDAO buildRegistryDAO,
                        @Named(NebulaServiceModule.BUILD_REGISTRY_REGEX) Optional<Pattern> buildRegistryRegex,
                        @Named(NebulaServiceModule.DEPLOY_REGISTRY_REGEX) Optional<Pattern> deployRegistryRegex) {
    this.deployRegistryDAO = deployRegistryDAO;
    this.buildRegistryDAO = buildRegistryDAO;
    this.buildRegistryRegex = buildRegistryRegex;
    this.deployRegistryRegex = deployRegistryRegex;
  }

  @Path("/webhook")
  @POST
  public void processWebhook(WebhookPayload webhookPayload) {
    LOG.info("Received webhook from GitHub: {}", webhookPayload);

    final Optional<String> maybeBranch = GitHubHelper.parseBranch(webhookPayload);
    final String user = webhookPayload.getRepository().getOrganization().or(webhookPayload.getRepository().getOwner().getName());

    if (maybeBranch.isPresent()) {
      for (GitHubCommit commit : webhookPayload.getCommits()) {
        for (String removedFile : commit.getRemoved()) {
          final String key = RegistryEntry.buildKey(user, webhookPayload.getRepository().getName(), maybeBranch.get(), removedFile);

          if (buildRegistryRegex.isPresent() && buildRegistryRegex.get().matcher(removedFile).matches()) {
            LOG.info("Removing key from build registry: {}", key);
            deployRegistryDAO.remove(key);
          }

          if (deployRegistryRegex.isPresent() && deployRegistryRegex.get().matcher(removedFile).matches()) {
            LOG.info("Removing key from deploy registry: {}", key);
            deployRegistryDAO.remove(key);
          }
        }

        for (String added : Iterables.concat(commit.getAdded(), commit.getModified())) {
          final RegistryEntry entry = RegistryEntry.build(user, webhookPayload.getRepository().getName(), maybeBranch.get(), added, commit.getId(), System.currentTimeMillis());

          if (buildRegistryRegex.isPresent() && buildRegistryRegex.get().matcher(added).matches()) {
            LOG.info("Adding key to build registry: {}", entry.getKey());
            buildRegistryDAO.add(entry);
          }

          if (deployRegistryRegex.isPresent() && deployRegistryRegex.get().matcher(added).matches()) {
            LOG.info("Adding key to deploy registry: {}", entry.getKey());
            deployRegistryDAO.add(entry);
          }
        }
      }
    }
  }
}
