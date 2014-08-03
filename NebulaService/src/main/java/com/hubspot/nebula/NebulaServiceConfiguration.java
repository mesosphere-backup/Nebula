package com.hubspot.nebula;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.hubspot.nebula.config.GitHubConfiguration;
import com.hubspot.nebula.config.RegistryConfiguration;

public class NebulaServiceConfiguration extends Configuration {
  @Valid
  @NotNull
  private DataSourceFactory database;

  @NotNull
  private GitHubConfiguration github = new GitHubConfiguration();

  @NotNull
  private RegistryConfiguration buildRegistry = RegistryConfiguration.disabled();

  @NotNull
  private RegistryConfiguration deployRegistry = RegistryConfiguration.disabled();

  public DataSourceFactory getDatabase() {
    return database;
  }

  public void setDatabase(DataSourceFactory database) {
    this.database = database;
  }

  public GitHubConfiguration getGithub() {
    return github;
  }

  public void setGithub(GitHubConfiguration github) {
    this.github = github;
  }

  public RegistryConfiguration getBuildRegistry() {
    return buildRegistry;
  }

  public void setBuildRegistry(RegistryConfiguration buildRegistry) {
    this.buildRegistry = buildRegistry;
  }

  public RegistryConfiguration getDeployRegistry() {
    return deployRegistry;
  }

  public void setDeployRegistry(RegistryConfiguration deployRegistry) {
    this.deployRegistry = deployRegistry;
  }
}
