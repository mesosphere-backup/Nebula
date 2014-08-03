package com.hubspot.nebula.registry;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import com.hubspot.nebula.NebulaDataModule;
import com.hubspot.rosetta.jdbi.RosettaBinder;
import com.hubspot.rosetta.jdbi.RosettaResultSetMapperFactory;

@RegisterMapperFactory(RosettaResultSetMapperFactory.class)
public interface DeployRegistryDAO {
  @SqlQuery("SELECT * FROM " + NebulaDataModule.DEPLOY_REGISTRY_TABLE_NAME)
  public List<RegistryEntry> getAll();

  @SqlQuery("SELECT * FROM " + NebulaDataModule.DEPLOY_REGISTRY_TABLE_NAME + " WHERE `key` = :key LIMIT 1")
  public RegistryEntry get(@Bind("key") String key);

  @SqlUpdate("REPLACE INTO " + NebulaDataModule.DEPLOY_REGISTRY_TABLE_NAME + " (`key`, gitUser, gitRepo, gitBranch, path, last_commit, last_updated) VALUES (:key, :gitUser, :gitRepo, :gitBranch, :path, :lastCommit, :lastUpdated)")
  public void add(@RosettaBinder RegistryEntry entry);

  @SqlUpdate("DELETE FROM " + NebulaDataModule.DEPLOY_REGISTRY_TABLE_NAME + " WHERE `key` = :key")
  public void remove(@Bind("key") String key);
}
