package com.hubspot.nebula.github;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties( ignoreUnknown = true )
public class GitHubCommit {
  private final String id;
  private final List<String> added;
  private final List<String> removed;
  private final List<String> modified;

  @JsonCreator
  public GitHubCommit(@JsonProperty("id") String id,
                      @JsonProperty("added") List<String> added,
                      @JsonProperty("removed") List<String> removed,
                      @JsonProperty("modified") List<String> modified) {
    this.id = id;
    this.added = added;
    this.removed = removed;
    this.modified = modified;
  }

  public String getId() {
    return id;
  }

  public List<String> getAdded() {
    return added;
  }

  public List<String> getRemoved() {
    return removed;
  }

  public List<String> getModified() {
    return modified;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("added", added)
        .add("removed", removed)
        .add("modified", modified)
        .toString();
  }
}
