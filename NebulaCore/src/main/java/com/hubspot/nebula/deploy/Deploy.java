package com.hubspot.nebula.deploy;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.hubspot.nebula.Result;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Deploy implements Result {
  private final long id;

  @NotNull
  private final DeployAction action;

  @NotNull
  private final DeployStatus status;

  @NotNull
  private final String deployKey;

  @NotNull
  private final String buildKey;

  @Min(0)
  private final int buildNumber;

  @NotNull
  private final String environment;

  @Min(0)
  private final long startTime;

  @Min(0)
  private final long duration;

  private final Optional<Long> rollbackDeployId;

  private final String deployConfigSnapshot;

  @NotNull
  private final String user;

  @JsonCreator
  public Deploy(@JsonProperty("id") long id,
                @JsonProperty("action") DeployAction action,
                @JsonProperty("status") DeployStatus status,
                @JsonProperty("deployKey") String deployKey,
                @JsonProperty("buildKey") String buildKey,
                @JsonProperty("buildNumber") int buildNumber,
                @JsonProperty("environment") String environment,
                @JsonProperty("startTime") long startTime,
                @JsonProperty("duration") long duration,
                @JsonProperty("rollbackDeployId") Optional<Long> rollbackDeployId,
                @JsonProperty("deployConfigSnapshot") String deployConfigSnapshot,
                @JsonProperty("user") String user) {
    this.id = id;
    this.action = action;
    this.status = status;
    this.deployKey = deployKey;
    this.buildKey = buildKey;
    this.buildNumber = buildNumber;
    this.environment = environment;
    this.startTime = startTime;
    this.duration = duration;
    this.rollbackDeployId = rollbackDeployId;
    this.deployConfigSnapshot = deployConfigSnapshot;
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public DeployAction getAction() {
    return action;
  }

  public DeployStatus getStatus() {
    return status;
  }

  public String getDeployKey() {
    return deployKey;
  }

  public String getBuildKey() {
    return buildKey;
  }

  public int getBuildNumber() {
    return buildNumber;
  }

  public String getEnvironment() {
    return environment;
  }

  public long getStartTime() {
    return startTime;
  }

  public long getDuration() {
    return duration;
  }

  public Optional<Long> getRollbackDeployId() {
    return rollbackDeployId;
  }

  public String getDeployConfigSnapshot() {
    return deployConfigSnapshot;
  }

  public String getUser() {
    return user;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("action", action)
        .add("status", status)
        .add("deployKey", deployKey)
        .add("buildKey", buildKey)
        .add("buildNumber", buildNumber)
        .add("environment", environment)
        .add("startTime", startTime)
        .add("duration", duration)
        .add("rollbackDeployId", rollbackDeployId)
        .add("deployConfigSnapshot", deployConfigSnapshot)
        .add("user", user)
        .toString();
  }
}
