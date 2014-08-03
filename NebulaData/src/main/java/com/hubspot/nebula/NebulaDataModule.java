package com.hubspot.nebula;

import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hubspot.nebula.build.Build;
import com.hubspot.nebula.build.BuildDAO;
import com.hubspot.nebula.build.BuildQuery;
import com.hubspot.nebula.deploy.Deploy;
import com.hubspot.nebula.deploy.DeployDAO;
import com.hubspot.nebula.deploy.DeployQuery;
import com.hubspot.nebula.registry.BuildRegistryDAO;
import com.hubspot.nebula.registry.DeployRegistryDAO;

public class NebulaDataModule extends AbstractModule {
  public static final String DEPLOYS_TABLE_NAME = "deploys";
  public static final String DEPLOY_REGISTRY_TABLE_NAME = "deploy_registry";
  public static final String BUILDS_TABLE_NAME = "builds";
  public static final String BUILD_REGISTRY_TABLE_NAME = "build_registry";

  public static final String GITHUB_BASE_URL = "github.baseUrl";
  public static final String GITHUB_ACCESS_TOKEN = "github.access.token";

  @Override
  protected void configure() {

  }

  @Provides
  public BuildDAO providesBuildDAO(DBI dbi) {
    return dbi.onDemand(BuildDAO.class);
  }

  @Provides
  public DeployDAO providesDeployDAO(DBI dbi) {
    return dbi.onDemand(DeployDAO.class);
  }

  @Provides
  public DeployRegistryDAO providesDeployRegistryDAO(DBI dbi) {
    return dbi.onDemand(DeployRegistryDAO.class);
  }

  @Provides
  public BuildRegistryDAO providesBuildRegistryDAO(DBI dbi) {
    return dbi.onDemand(BuildRegistryDAO.class);
  }

  @Provides
  @Singleton
  public QueryManager<Deploy, DeployQuery> providesDeployQueryManager(DBI dbi) {
    return new QueryManager<>(dbi, DEPLOYS_TABLE_NAME, Deploy.class);
  }

  @Provides
  @Singleton
  public QueryManager<Build, BuildQuery> providesBuildQueryManager(DBI dbi) {
    return new QueryManager<>(dbi, BUILDS_TABLE_NAME, Build.class);
  }
}
