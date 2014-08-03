package com.hubspot.nebula.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class GitHubOwner {
  private final String name;
  private final String email;

  @JsonCreator
  public GitHubOwner(@JsonProperty("name") String name,
                     @JsonProperty("email") String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("name", name)
        .add("email", email)
        .toString();
  }
}
