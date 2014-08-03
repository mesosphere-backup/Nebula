package com.hubspot.nebula;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.hubspot.jackson.jaxrs.PropertyFilteringMessageBodyWriter;

public class NebulaService extends Application<NebulaServiceConfiguration> {
  @Override
  public void initialize(Bootstrap<NebulaServiceConfiguration> bootstrap) {
    GuiceBundle<NebulaServiceConfiguration> guiceBundle = GuiceBundle.<NebulaServiceConfiguration>newBuilder()
        .addModule(new NebulaServiceModule())
        .enableAutoConfig(getClass().getPackage().getName())
        .setConfigClass(NebulaServiceConfiguration.class)
        .build(Stage.DEVELOPMENT);

    bootstrap.addBundle(guiceBundle);

    // database migrations
    bootstrap.addBundle(new MigrationsBundle<NebulaServiceConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(NebulaServiceConfiguration configuration) {
        return configuration.getDatabase();
      }
    });

    bootstrap.getObjectMapper().registerModule(new ProtobufModule());
    bootstrap.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void run(NebulaServiceConfiguration configuration, Environment environment) throws Exception {
    environment.jersey().register(PropertyFilteringMessageBodyWriter.class);
  }

  public static void main(String... args) {
    try {
      new NebulaService().run(args);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }
}
