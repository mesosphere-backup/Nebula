package com.hubspot.nebula;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import java.util.regex.Pattern;

import org.skife.jdbi.v2.DBI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.nebula.config.GitHubConfiguration;

public class NebulaServiceModule extends AbstractModule {
  public static final String BUILD_REGISTRY_REGEX = "build.registry.regex";
  public static final String BUILD_REGISTRY_ENABLED = "build.registry.enabled";
  public static final String DEPLOY_REGISTRY_REGEX = "deploy.registry.regex";
  public static final String DEPLOY_REGISTRY_ENABLED = "deploy.registry.enabled";

  @Override
  protected void configure() {
    install(new NebulaDataModule());
  }

  @Provides
  public ObjectMapper providesObjectMapper(Environment environment) {
    return environment.getObjectMapper();
  }

  @Provides
  @Singleton
  public DBI providesDBI(Environment environment, NebulaServiceConfiguration configuration) throws ClassNotFoundException {
    final DBIFactory factory = new DBIFactory();

    return factory.build(environment, configuration.getDatabase(), "db");
  }

  @Provides
  @Singleton
  public GitHubConfiguration providesGitHubConfiguration(NebulaServiceConfiguration configuration) {
    return configuration.getGithub();
  }

  @Provides
  @Singleton
  @Named(NebulaDataModule.GITHUB_BASE_URL)
  public String providesGitHubBaseUrl(GitHubConfiguration github) {
    return github.getBaseUrl();
  }

  @Provides
  @Singleton
  @Named(NebulaDataModule.GITHUB_ACCESS_TOKEN)
  public Optional<String> providesGitHubApiToken(GitHubConfiguration github) {
    return github.getAccessToken();
  }

  @Provides
  @Singleton
  @Named(BUILD_REGISTRY_ENABLED)
  public boolean providesBuildRegistryEnabled(NebulaServiceConfiguration configuration) {
    return configuration.getBuildRegistry().isEnabled();
  }

  @Provides
  @Singleton
  @Named(DEPLOY_REGISTRY_ENABLED)
  public boolean providesDeployRegistryEnabled(NebulaServiceConfiguration configuration) {
    return configuration.getDeployRegistry().isEnabled();
  }

  @Provides
  @Singleton
  @Named(BUILD_REGISTRY_REGEX)
  public Optional<Pattern> providesBuildRegistryRegex(NebulaServiceConfiguration configuration) {
    if (configuration.getBuildRegistry().isEnabled()) {
      return Optional.of(Pattern.compile(configuration.getBuildRegistry().getRegex()));
    } else {
      return Optional.absent();
    }
  }

  @Provides
  @Singleton
  @Named(DEPLOY_REGISTRY_REGEX)
  public Optional<Pattern> providesDeployRegistryRegex(NebulaServiceConfiguration configuration) {
    if (configuration.getDeployRegistry().isEnabled()) {
      return Optional.of(Pattern.compile(configuration.getDeployRegistry().getRegex()));
    } else {
      return Optional.absent();
    }
  }
}
