package com.hubspot.nebula.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hubspot.nebula.NebulaServiceModule;
import com.hubspot.nebula.QueryManager;
import com.hubspot.nebula.QueryResult;
import com.hubspot.nebula.build.Build;
import com.hubspot.nebula.build.BuildDAO;
import com.hubspot.nebula.deploy.Deploy;
import com.hubspot.nebula.deploy.DeployDAO;
import com.hubspot.nebula.deploy.DeployQuery;
import com.hubspot.nebula.jersey.BindQueryParams;
import com.hubspot.nebula.registry.DeployRegistryDAO;
import com.hubspot.nebula.registry.RegistryEntry;

@Path("/deploys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class DeployResource {
  private final DeployDAO dao;
  private final BuildDAO buildDAO;
  private final QueryManager<Deploy, DeployQuery> manager;
  private final DeployRegistryDAO registryDAO;
  private final boolean deployRegistryEnabled;

  @Inject
  public DeployResource(DeployDAO dao,
                        BuildDAO buildDAO,
                        QueryManager<Deploy, DeployQuery> manager,
                        DeployRegistryDAO registryDAO,
                        @Named(NebulaServiceModule.DEPLOY_REGISTRY_ENABLED) boolean deployRegistryEnabled) {
    this.dao = dao;
    this.buildDAO = buildDAO;
    this.manager = manager;
    this.registryDAO = registryDAO;
    this.deployRegistryEnabled = deployRegistryEnabled;
  }

  @GET
  public QueryResult<Deploy, DeployQuery> query(@BindQueryParams DeployQuery query) {
    return manager.query(query);
  }

  @GET
  @Path("/deploy/{id}")
  public Optional<Deploy> get(@PathParam("id") long id) {
    return Optional.fromNullable(dao.get(id));
  }

  @POST
  public Deploy create(@Valid Deploy newDeploy) {
    final Optional<Build> maybeBuild = Optional.fromNullable(buildDAO.getForDeploy(newDeploy));
    final Optional<RegistryEntry> maybeEntry = deployRegistryEnabled ? Optional.fromNullable(registryDAO.get(newDeploy.getDeployKey())) : Optional.<RegistryEntry>absent();

    if (!maybeBuild.isPresent()) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(String.format("Build %s#%d does not exist", newDeploy.getBuildKey(), newDeploy.getBuildNumber())).build());
    }

    if (deployRegistryEnabled && !maybeEntry.isPresent()) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(String.format("Deploy registry key %s does not exist", newDeploy.getDeployKey())).build());
    }

    return dao.get(dao.create(newDeploy));
  }

  @POST
  @Path("/deploy/{id}")
  public Optional<Deploy> update(@PathParam("id") long id, Deploy updatedDeploy) {
    final Optional<Deploy> maybeDeploy = Optional.fromNullable(dao.get(id));

    if (maybeDeploy.isPresent()) {
      dao.setStatus(id, updatedDeploy.getStatus());
      dao.setDuration(id, updatedDeploy.getDuration());
    } else {
      return Optional.absent();
    }

    return Optional.fromNullable(dao.get(id));
  }
}
