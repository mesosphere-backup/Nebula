package com.hubspot.nebula.build;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import com.hubspot.nebula.NebulaDataModule;
import com.hubspot.nebula.deploy.Deploy;
import com.hubspot.rosetta.jdbi.RosettaBinder;
import com.hubspot.rosetta.jdbi.RosettaResultSetMapperFactory;

@RegisterMapperFactory(RosettaResultSetMapperFactory.class)
public interface BuildDAO {
  @SqlQuery("SELECT * FROM " + NebulaDataModule.BUILDS_TABLE_NAME + " WHERE id = :id LIMIT 1")
  public Build get(@Bind("id") long id);

  @SqlQuery("SELECT * FROM " + NebulaDataModule.BUILDS_TABLE_NAME + " WHERE buildKey = :buildKey AND buildNumber = :buildNumber LIMIT 1")
  public Build getForDeploy(@RosettaBinder Deploy deploy);

  @SqlUpdate("INSERT INTO " + NebulaDataModule.BUILDS_TABLE_NAME + " (buildKey, number, status, startTime, duration, gitUser, gitRepo, gitBranch, lastGitCommitSha, bad, artifactLocation) VALUES (:buildKey, :number, :status, :startTime, :duration, :gitUser, :gitRepo, :gitBranch, :lastGitCommitSha, :bad, :artifactLocation)")
  @GetGeneratedKeys
  public int create(@RosettaBinder Build build);

  @SqlUpdate("UPDATE " + NebulaDataModule.BUILDS_TABLE_NAME + " SET status = :status WHERE id = :id")
  public void setStatus(@Bind("id") long id, @Bind("status") BuildStatus status);

  @SqlUpdate("UPDATE " + NebulaDataModule.BUILDS_TABLE_NAME + " SET bad = :bad WHERE id = :id")
  public void setBad(@Bind("id") long id, @Bind("bad") boolean bad);

  @SqlUpdate("UPDATE " + NebulaDataModule.BUILDS_TABLE_NAME + " SET duration = :duration WHERE id = :id")
  public void setDuration(@Bind("id") long id, @Bind("duration") long duration);

  @SqlUpdate("UPDATE " + NebulaDataModule.BUILDS_TABLE_NAME + " SET artifactLocation = :artifactLocation WHERE id = :id")
  public void setArtifactLocation(@Bind("id") long id, @Bind("artifactLocation") String artifactLocation);
}