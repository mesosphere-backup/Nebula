package com.hubspot.nebula.registry;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import com.hubspot.nebula.NebulaDataModule;
import com.hubspot.rosetta.jdbi.RosettaBinder;

public interface BuildRegistryDAO {
  @SqlQuery("SELECT * FROM " + NebulaDataModule.BUILD_REGISTRY_TABLE_NAME)
  public List<RegistryEntry> getAll();

  @SqlQuery("SELECT * FROM " + NebulaDataModule.BUILD_REGISTRY_TABLE_NAME + " WHERE `key` = :key LIMIT 1")
  public RegistryEntry get(@Bind("key") String key);

  @SqlUpdate("REPLACE INTO " + NebulaDataModule.BUILD_REGISTRY_TABLE_NAME + " (`key`, gitUser, gitRepo, gitBranch, path, lastGitCommitSha, lastUpdated) VALUES (:key, :gitUser, :gitRepo, :gitBranch, :path, :lastGitCommitSha, :lastUpdated)")
  public void add(@RosettaBinder RegistryEntry entry);

  @SqlUpdate("DELETE FROM " + NebulaDataModule.BUILD_REGISTRY_TABLE_NAME + " WHERE `key` = :key")
  public void remove(@Bind("key") String key);
}
