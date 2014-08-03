package com.hubspot.nebula.deploy;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import com.hubspot.nebula.NebulaDataModule;
import com.hubspot.rosetta.jdbi.RosettaBinder;
import com.hubspot.rosetta.jdbi.RosettaResultSetMapperFactory;

@RegisterMapperFactory(RosettaResultSetMapperFactory.class)
public interface DeployDAO {
  @SqlQuery("SELECT * FROM " + NebulaDataModule.DEPLOYS_TABLE_NAME + " WHERE id = :id LIMIT 1")
  public Deploy get(@Bind("id") long id);

  @SqlQuery("SELECT * FROM " + NebulaDataModule.DEPLOYS_TABLE_NAME + " WHERE id > :offset LIMIT :count")
  public List<Deploy> getMany(@Bind("offset") long offset, @Bind("count") int count);

  @SqlUpdate("INSERT INTO " + NebulaDataModule.DEPLOYS_TABLE_NAME + " (action, status, registryKey, buildId, environment, startTime, duration, rollbackDeployId, deployConfigSnapshot, user) VALUES (:action, :status, :registryKey, :buildId, :environment, :startTime, :duration, :rollbackDeployId, :deployConfigSnapshot, :user)")
  @GetGeneratedKeys
  public int create(@RosettaBinder Deploy deploy);

  @SqlUpdate("UPDATE " + NebulaDataModule.DEPLOYS_TABLE_NAME + " SET status = :status WHERE id = :id")
  public void setStatus(@Bind("id") long id, @Bind("status") DeployStatus status);

  @SqlUpdate("UPDATE " + NebulaDataModule.DEPLOYS_TABLE_NAME + " SET duration = :duration WHERE id = :id")
  public void setDuration(@Bind("id") long id, @Bind("duration") long duration);
}
