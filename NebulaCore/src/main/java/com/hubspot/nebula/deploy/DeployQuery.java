package com.hubspot.nebula.deploy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.hubspot.nebula.Query;
import com.hubspot.nebula.SqlPredicate;
import com.hubspot.nebula.SqlPredicate.Operator;

public class DeployQuery implements Query {
  private final int count;
  private final Optional<Long> offset;
  private final boolean ascending;

  private final Optional<DeployAction> action;
  private final Optional<DeployStatus> status;
  private final Optional<String> deployKey;
  private final Optional<String> buildKey;
  private final Optional<Long> buildId;
  private final Optional<String> environment;
  private final Optional<String> user;

  private final Optional<Long> startTimeBefore;
  private final Optional<Long> startTimeAfter;

  private final Optional<Long> durationLessThan;
  private final Optional<Long> durationGreaterThan;

  @JsonCreator
  public DeployQuery(@JsonProperty("count") int count,
                     @JsonProperty("offset") Optional<Long> offset,
                     @JsonProperty("ascending") boolean ascending,
                     @JsonProperty("action") Optional<DeployAction> action,
                     @JsonProperty("status") Optional<DeployStatus> status,
                     @JsonProperty("deployKey") Optional<String> deployKey,
                     @JsonProperty("buildKey") Optional<String> buildKey,
                     @JsonProperty("buildId") Optional<Long> buildId,
                     @JsonProperty("environment") Optional<String> environment,
                     @JsonProperty("user") Optional<String> user,
                     @JsonProperty("startTimeBefore") Optional<Long> startTimeBefore,
                     @JsonProperty("startTimeAfter") Optional<Long> startTimeAfter,
                     @JsonProperty("durationLessThan") Optional<Long> durationLessThan,
                     @JsonProperty("durationGreaterThan") Optional<Long> durationGreaterThan) {
    this.count = count;
    this.offset = offset;
    this.ascending = ascending;
    this.action = action;
    this.status = status;
    this.deployKey = deployKey;
    this.buildKey = buildKey;
    this.buildId = buildId;
    this.environment = environment;
    this.user = user;
    this.startTimeBefore = startTimeBefore;
    this.startTimeAfter = startTimeAfter;
    this.durationLessThan = durationLessThan;
    this.durationGreaterThan = durationGreaterThan;
  }

  public int getCount() {
    return count;
  }

  public Optional<Long> getOffset() {
    return offset;
  }

  public boolean isAscending() {
    return ascending;
  }

  @SqlPredicate
  public Optional<DeployAction> getAction() {
    return action;
  }

  @SqlPredicate
  public Optional<DeployStatus> getStatus() {
    return status;
  }

  @SqlPredicate
  public Optional<String> getDeployKey() {
    return deployKey;
  }

  @SqlPredicate
  public Optional<String> getBuildKey() {
    return buildKey;
  }

  @SqlPredicate
  public Optional<Long> getBuildId() {
    return buildId;
  }

  @SqlPredicate
  public Optional<String> getEnvironment() {
    return environment;
  }

  @SqlPredicate
  public Optional<String> getUser() {
    return user;
  }

  @SqlPredicate(columnName = "startTime", operator = Operator.LESS_THAN)
  public Optional<Long> getStartTimeBefore() {
    return startTimeBefore;
  }

  @SqlPredicate(columnName = "startTime", operator = Operator.GREATER_THAN)
  public Optional<Long> getStartTimeAfter() {
    return startTimeAfter;
  }

  @SqlPredicate(columnName = "duration", operator = Operator.LESS_THAN)
  public Optional<Long> getDurationLessThan() {
    return durationLessThan;
  }

  @SqlPredicate(columnName = "duration", operator = Operator.GREATER_THAN)
  public Optional<Long> getDurationGreaterThan() {
    return durationGreaterThan;
  }
}