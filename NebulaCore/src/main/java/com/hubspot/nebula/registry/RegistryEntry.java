package com.hubspot.nebula.registry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RegistryEntry {
  private final String key;

  private final String gitUser;
  private final String gitRepo;
  private final String gitBranch;
  private final String path;

  private final String lastGitCommitSha;
  private final long lastUpdated;

  private static String normalizeComponent(String component) {
    return CharMatcher.JAVA_LETTER_OR_DIGIT.negate().trimAndCollapseFrom(component, '_');
  }

  public static String buildKey(String gitUser, String gitRepo, String gitBranch, String path) {
    return Joiner.on('-').join(normalizeComponent(gitUser), normalizeComponent(gitRepo), normalizeComponent(gitBranch), normalizeComponent(path));
  }

  public static RegistryEntry build(String gitUser, String gitRepo, String gitBranch, String path, String lastCommit, long lastUpdated) {
    return new RegistryEntry(buildKey(gitUser, gitRepo, gitBranch, path), gitUser, gitRepo, gitBranch, path, lastCommit, lastUpdated);
  }

  @JsonCreator
  public RegistryEntry(@JsonProperty("key") String key,
                       @JsonProperty("gitUser") String gitUser,
                       @JsonProperty("gitRepo") String gitRepo,
                       @JsonProperty("gitBranch") String gitBranch,
                       @JsonProperty("path") String path,
                       @JsonProperty("lastGitCommitSha") String lastGitCommitSha,
                       @JsonProperty("lastUpdated") long lastUpdated) {
    this.key = key;
    this.gitUser = gitUser;
    this.gitRepo = gitRepo;
    this.gitBranch = gitBranch;
    this.path = path;
    this.lastGitCommitSha = lastGitCommitSha;
    this.lastUpdated = lastUpdated;
  }

  public String getKey() {
    return key;
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

  public String getPath() {
    return path;
  }

  public String getLastGitCommitSha() {
    return lastGitCommitSha;
  }

  public long getLastUpdated() {
    return lastUpdated;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("key", key)
        .add("gitUser", gitUser)
        .add("gitRepo", gitRepo)
        .add("gitBranch", gitBranch)
        .add("path", path)
        .add("lastGitCommitSha", lastGitCommitSha)
        .add("lastUpdated", lastUpdated)
        .toString();
  }
}
