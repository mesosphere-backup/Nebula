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
import com.hubspot.nebula.build.BuildQuery;
import com.hubspot.nebula.jersey.BindQueryParams;
import com.hubspot.nebula.registry.BuildRegistryDAO;
import com.hubspot.nebula.registry.RegistryEntry;

@Path("/builds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class BuildResource {
  private final BuildDAO dao;
  private final BuildRegistryDAO registryDAO;
  private final QueryManager<Build, BuildQuery> manager;
  private final boolean buildRegistryEnabled;

  @Inject
  public BuildResource(BuildDAO dao,
                       BuildRegistryDAO registryDAO,
                       QueryManager<Build, BuildQuery> manager,
                       @Named(NebulaServiceModule.BUILD_REGISTRY_ENABLED) boolean buildRegistryEnabled) {
    this.dao = dao;
    this.registryDAO = registryDAO;
    this.manager = manager;
    this.buildRegistryEnabled = buildRegistryEnabled;
  }

  @GET
  public QueryResult<Build, BuildQuery> query(@BindQueryParams BuildQuery query) {
    return manager.query(query);
  }

  @GET
  @Path("/build/{id}")
  public Optional<Build> get(@PathParam("id") long id) {
    return Optional.fromNullable(dao.get(id));
  }

  @POST
  public Build create(@Valid Build newBuild) {
    final Optional<RegistryEntry> maybeEntry = Optional.fromNullable(registryDAO.get(newBuild.getBuildKey()));

    if (buildRegistryEnabled && !maybeEntry.isPresent()) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(String.format("Build registry key %s does not exist!", newBuild.getBuildKey())).build());
    }

    return dao.get(dao.create(newBuild));
  }

  @POST
  @Path("/build/{id}")
  public Optional<Build> update(@PathParam("id") long id, Build updatedBuild) {
    final Optional<Build> maybeBuild = Optional.fromNullable(dao.get(id));

    if (maybeBuild.isPresent()) {
      dao.setBad(id, updatedBuild.isBad());
      dao.setDuration(id, updatedBuild.getDuration());
      dao.setStatus(id, updatedBuild.getStatus());
      dao.setArtifactLocation(id, updatedBuild.getArtifactLocation());
    } else {
      return Optional.absent();
    }

    return Optional.fromNullable(dao.get(id));
  }
}
