package com.hubspot.nebula.build;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.hubspot.nebula.Result;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Build implements Result {
  private final long id;

  @NotNull
  private final String buildKey;

  @NotNull
  private final BuildStatus status;

  @Min(0)
  private final int number;

  @Min(0)
  private final long startTime;

  @Min(0)
  private final long duration;

  @NotNull
  private final String gitUser;

  @NotNull
  private final String gitRepo;

  @NotNull
  private final String gitBranch;

  @NotNull
  private final String lastGitCommitSha;

  @NotNull
  private final String artifactLocation;

  private final boolean bad;

  @JsonCreator
  public Build(@JsonProperty("id") long id,
               @JsonProperty("buildKey") String buildKey,
               @JsonProperty("status") BuildStatus status,
               @JsonProperty("number") int number,
               @JsonProperty("startTime") long startTime,
               @JsonProperty("duration") long duration,
               @JsonProperty("gitUser") String gitUser,
               @JsonProperty("gitRepo") String gitRepo,
               @JsonProperty("gitBranch") String gitBranch,
               @JsonProperty("lastGitCommitSha") String lastGitCommitSha,
               @JsonProperty("bad") boolean bad,
               @JsonProperty("artifactLocation") String artifactLocation) {
    this.id = id;
    this.buildKey = buildKey;
    this.status = status;
    this.number = number;
    this.startTime = startTime;
    this.duration = duration;
    this.gitUser = gitUser;
    this.gitRepo = gitRepo;
    this.gitBranch = gitBranch;
    this.lastGitCommitSha = lastGitCommitSha;
    this.bad = bad;
    this.artifactLocation = artifactLocation;
  }

  public long getId() {
    return id;
  }

  public String getBuildKey() {
    return buildKey;
  }

  public BuildStatus getStatus() {
    return status;
  }

  public int getNumber() {
    return number;
  }

  public String getGitUser() {
    return gitUser;
  }

  public String getGitRepo() {
    return gitRepo;
  }

  public String getGitBranch() {
    return gitBranch;
  }

  public String getLastGitCommitSha() {
    return lastGitCommitSha;
  }

  public boolean isBad() {
    return bad;
  }

  public long getStartTime() {
    return startTime;
  }

  public long getDuration() {
    return duration;
  }

  public String getArtifactLocation() {
    return artifactLocation;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("buildKey", buildKey)
        .add("status", status)
        .add("number", number)
        .add("startTime", startTime)
        .add("duration", duration)
        .add("gitUser", gitUser)
        .add("gitRepo", gitRepo)
        .add("gitBranch", gitBranch)
        .add("lastGitCommitSha", lastGitCommitSha)
        .add("bad", bad)
        .add("artifactLocation", artifactLocation)
        .toString();
  }
}
