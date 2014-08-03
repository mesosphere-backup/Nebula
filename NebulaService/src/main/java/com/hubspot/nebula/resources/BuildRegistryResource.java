package com.hubspot.nebula.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.hubspot.nebula.github.GitHubClient;
import com.hubspot.nebula.registry.BuildRegistryDAO;
import com.hubspot.nebula.registry.RegistryEntry;

@Path("/builds/registry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class BuildRegistryResource {
  private final BuildRegistryDAO dao;
  private final GitHubClient gitHubClient;

  @Inject
  public BuildRegistryResource(BuildRegistryDAO dao, GitHubClient gitHubClient) {
    this.dao = dao;
    this.gitHubClient = gitHubClient;
  }

  @GET
  public List<RegistryEntry> getAll() {
    return dao.getAll();
  }

  @GET
  @Path("/{key}")
  public Optional<RegistryEntry> get(@PathParam("key") String key) {
    return Optional.fromNullable(dao.get(key));
  }

  @GET
  @Path("/{key}/contents")
  public Optional<String> getContents(@PathParam("key") String key) {
    final Optional<RegistryEntry> maybeEntry = Optional.fromNullable(dao.get(key));

    if (maybeEntry.isPresent()) {
      return gitHubClient.getContents(maybeEntry.get().getGitUser(), maybeEntry.get().getGitRepo(), maybeEntry.get().getPath(), Optional.of(maybeEntry.get().getGitBranch()));
    }

    return Optional.absent();
  }

  @POST
  public void add(@Valid RegistryEntry entry) {
    dao.add(entry);
  }

  @DELETE
  @Path("/{key}")
  public Optional<RegistryEntry> remove(@PathParam("key") String key) {
    final Optional<RegistryEntry> maybeEntry = Optional.fromNullable(dao.get(key));

    if (maybeEntry.isPresent()) {
      dao.remove(key);
    }

    return maybeEntry;
  }
}
