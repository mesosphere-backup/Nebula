package com.hubspot.nebula.build;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.hubspot.nebula.SqlPredicate;
import com.hubspot.nebula.Query;
import com.hubspot.nebula.SqlPredicate.Operator;

public class BuildQuery implements Query {
  private final int count;
  private final Optional<Long> offset;
  private final boolean ascending;

  private final Optional<String> registryKey;
  private final Optional<BuildStatus> status;
  private final Optional<Boolean> bad;

  private final Optional<String> gitUser;
  private final Optional<String> gitRepo;
  private final Optional<String> gitBranch;
  private final Optional<String> lastGitCommitSha;

  private final Optional<Integer> number;
  private final Optional<Integer> numberBefore;
  private final Optional<Integer> numberAfter;

  private final Optional<Long> startTimeBefore;
  private final Optional<Long> startTimeAfter;

  private final Optional<Long> durationLessThan;
  private final Optional<Long> durationGreaterThan;

  @JsonCreator
  public BuildQuery(@JsonProperty("count") int count,
                    @JsonProperty("offset") Optional<Long> offset,
                    @JsonProperty("ascending") boolean ascending,
                    @JsonProperty("registryKey") Optional<String> registryKey,
                    @JsonProperty("status") Optional<BuildStatus> status,
                    @JsonProperty("gitUser") Optional<String> gitUser,
                    @JsonProperty("gitRepo") Optional<String> gitRepo,
                    @JsonProperty("gitBranch") Optional<String> gitBranch,
                    @JsonProperty("lastGitCommitSha") Optional<String> lastGitCommitSha,
                    @JsonProperty("number") Optional<Integer> number,
                    @JsonProperty("numberBefore") Optional<Integer> numberBefore,
                    @JsonProperty("numberAfter") Optional<Integer> numberAfter,
                    @JsonProperty("startTimeBefore") Optional<Long> startTimeBefore,
                    @JsonProperty("startTimeAfter") Optional<Long> startTimeAfter,
                    @JsonProperty("durationLessThan") Optional<Long> durationLessThan,
                    @JsonProperty("durationGreaterThan") Optional<Long> durationGreaterThan,
                    @JsonProperty("bad") Optional<Boolean> bad) {
    this.count = count;
    this.offset = offset;
    this.ascending = ascending;
    this.registryKey = registryKey;
    this.status = status;
    this.gitUser = gitUser;
    this.gitRepo = gitRepo;
    this.gitBranch = gitBranch;
    this.lastGitCommitSha = lastGitCommitSha;
    this.number = number;
    this.numberBefore = numberBefore;
    this.numberAfter = numberAfter;
    this.startTimeBefore = startTimeBefore;
    this.startTimeAfter = startTimeAfter;
    this.durationLessThan = durationLessThan;
    this.durationGreaterThan = durationGreaterThan;
    this.bad = bad;
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
  public Optional<String> getRegistryKey() {
    return registryKey;
  }

  @SqlPredicate
  public Optional<BuildStatus> getStatus() {
    return status;
  }

  @SqlPredicate
  public Optional<Integer> getNumber() {
    return number;
  }

  @SqlPredicate
  public Optional<Boolean> getBad() {
    return bad;
  }

  @SqlPredicate
  public Optional<String> getGitUser() {
    return gitUser;
  }

  @SqlPredicate
  public Optional<String> getGitRepo() {
    return gitRepo;
  }

  @SqlPredicate
  public Optional<String> getGitBranch() {
    return gitBranch;
  }

  @SqlPredicate
  public Optional<String> getLastGitCommitSha() {
    return lastGitCommitSha;
  }

  @SqlPredicate(columnName = "number", operator = Operator.LESS_THAN)
  public Optional<Integer> getNumberBefore() {
    return numberBefore;
  }

  @SqlPredicate(columnName = "number", operator = Operator.GREATER_THAN)
  public Optional<Integer> getNumberAfter() {
    return numberAfter;
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
