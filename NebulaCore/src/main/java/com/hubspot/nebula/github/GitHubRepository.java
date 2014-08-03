package com.hubspot.nebula.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

public class GitHubRepository {
  private final String id;
  private final String name;
  private final Optional<String> organization;
  private final GitHubOwner owner;

  @JsonCreator
  public GitHubRepository(@JsonProperty("id") String id,
                          @JsonProperty("name") String name,
                          @JsonProperty("organization") Optional<String> organization,
                          @JsonProperty("owner") GitHubOwner owner) {
    this.id = id;
    this.name = name;
    this.organization = organization;
    this.owner = owner;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Optional<String> getOrganization() {
    return organization;
  }

  public GitHubOwner getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("organization", organization)
        .add("owner", owner)
        .toString();
  }
}
