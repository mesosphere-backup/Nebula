package com.hubspot.nebula.github;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class WebhookPayload {
  private final String ref;
  private final List<GitHubCommit> commits;
  private final GitHubRepository repository;

  @JsonCreator
  public WebhookPayload(@JsonProperty("ref") String ref,
                        @JsonProperty("commits") List<GitHubCommit> commits,
                        @JsonProperty("repository") GitHubRepository repository) {
    this.ref = ref;
    this.commits = commits;
    this.repository = repository;
  }

  public String getRef() {
    return ref;
  }

  public List<GitHubCommit> getCommits() {
    return commits;
  }

  public GitHubRepository getRepository() {
    return repository;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("ref", ref)
        .add("commits", commits)
        .add("repository", repository)
        .toString();
  }
}
