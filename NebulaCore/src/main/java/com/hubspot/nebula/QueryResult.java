package com.hubspot.nebula;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

@JsonIgnoreProperties( ignoreUnknown = true )
public class QueryResult<R extends Result, Q extends Query> {
  private final Q query;
  private final List<R> objects;
  private final Optional<Long> nextOffset;

  @JsonCreator
  public QueryResult(@JsonProperty("query") Q query,
                     @JsonProperty("objects") List<R> objects,
                     @JsonProperty("nextOffset") Optional<Long> nextOffset) {
    this.query = query;
    this.objects = objects;
    this.nextOffset = nextOffset;
  }

  public Q getQuery() {
    return query;
  }

  public List<R> getObjects() {
    return objects;
  }

  public Optional<Long> getNextOffset() {
    return nextOffset;
  }
}
