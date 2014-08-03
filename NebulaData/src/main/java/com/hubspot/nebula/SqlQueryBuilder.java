package com.hubspot.nebula;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

// adapted from Query.Builder in HubSpotConnect by mprior.
public class SqlQueryBuilder {
  private static Joiner commaJoiner = Joiner.on(", ");
  private static Joiner tightCommaJoiner = Joiner.on(",");
  private static Joiner quotedTightCommaJoiner = Joiner.on("','");
  private static Joiner spaceJoiner = Joiner.on(" ");

  protected Optional<String> insert = Optional.absent();
  protected boolean ignoredInsert = false;
  protected boolean replace = false;
  protected Optional<String> update = Optional.absent();
  protected boolean delete = false;
  protected List<String> insertFields = Lists.newArrayList();
  protected List<String> currentRowInsertValues = Lists.newArrayList();
  protected List<List<String>> insertValues = Lists.newArrayList();
  protected List<Object> insertValueParams = Lists.newArrayList();
  protected List<String> updateSets = Lists.newArrayList();
  protected List<Object> updateSetParams = Lists.newArrayList();
  protected List<String> selects = Lists.newArrayList();
  protected boolean distinct = false;
  protected List<Object> selectParams = Lists.newArrayList();
  protected Optional<String> from = Optional.absent();
  protected List<String> joins = Lists.newArrayList();
  protected List<String> wheres = Lists.newArrayList();
  protected List<Object> whereParams = Lists.newArrayList();
  protected List<String> groupBys = Lists.newArrayList();
  protected Optional<String> orderBy = Optional.absent();
  protected Optional<String> limit = Optional.absent();
  protected Optional<Integer> offset = Optional.absent();
  protected boolean failUpsertWithoutChanges = true;

  public SqlQueryBuilder select(String selectFragment, Object... params) {
    this.selects.add(selectFragment);
    this.selectParams.addAll(Arrays.asList(params));
    return this;
  }

  public SqlQueryBuilder distinct() {
    this.distinct = true;
    return this;
  }

  public SqlQueryBuilder from(String fromClause) {
    this.from = Optional.of(fromClause);
    return this;
  }

  public SqlQueryBuilder join(String joinClause) {
    this.joins.add("JOIN " + joinClause);
    return this;
  }

  public SqlQueryBuilder leftJoin(String leftJoinClause) {
    this.joins.add("LEFT JOIN " + leftJoinClause);
    return this;
  }

  public SqlQueryBuilder upsert(String tableName) {
    this.insert = Optional.of(tableName);
    this.update = Optional.of(tableName);
    return this;
  }

  public SqlQueryBuilder insert(String tableName) {
    this.insert = Optional.of(tableName);
    return this;
  }

  public SqlQueryBuilder insertIgnore(String tableName) {
    this.ignoredInsert = true;
    return this.insert(tableName);
  }

  public SqlQueryBuilder replace(String tableName) {
    this.replace = true;
    this.insert = Optional.of(tableName);
    return this;
  }

  public SqlQueryBuilder update(String tableName) {
    this.update = Optional.of(tableName);
    return this;
  }

  public SqlQueryBuilder delete() {
    this.delete = true;
    return this;
  }

  public SqlQueryBuilder field(String insertFieldFragment) {
    Preconditions.checkArgument(this.insert.isPresent(), "only use this method in conjunction with an insert (or upsert)");
    this.insertFields.add(insertFieldFragment);
    return this;
  }

  public SqlQueryBuilder value(String insertValuesFragment, Object... params) {
    Preconditions.checkArgument(this.insert.isPresent(), "only use this method in conjunction with an insert (or upsert)");
    this.currentRowInsertValues.add(insertValuesFragment);
    this.insertValueParams.addAll(Arrays.asList(params));
    return this;
  }

  public SqlQueryBuilder rowValue(String insertValuesFragment, Object... params) {
    this.value(insertValuesFragment, params);
    this.insertValues.add(this.currentRowInsertValues);
    this.currentRowInsertValues = Lists.newArrayList();
    return this;
  }

  public SqlQueryBuilder set(String assignment, Object... params) {
    Preconditions.checkArgument(this.update.isPresent(), "only use this method in conjunction with an update (or upsert)");
    this.updateSets.add(assignment);
    this.updateSetParams.addAll(Arrays.asList(params));
    return this;
  }

  public SqlQueryBuilder acceptUpsertWithoutChanges() {
    failUpsertWithoutChanges = false;
    return this;
  }

  public SqlQueryBuilder where(String s, Object... params) {
    wheres.add(s);
    this.whereParams.addAll(Arrays.asList(params));
    return this;
  }

  private <T> String toInClause(Iterable<T> items, boolean stringifyItems) {
    if (stringifyItems) {
      return "IN ('" + quotedTightCommaJoiner.join(items) + "')";
    }
    return "IN (" + tightCommaJoiner.join(items) + ")";
  }

  public <T> SqlQueryBuilder whereIn(String key, Iterable<T> items, boolean stringifyItems) {
    if (Iterables.isEmpty(items)) {
      wheres.add("FALSE");
    } else {
      wheres.add(key + " " + toInClause(items, stringifyItems));
    }
    return this;
  }

  public <T> SqlQueryBuilder whereNotIn(String key, Iterable<T> items, boolean stringifyItems) {
    if (!Iterables.isEmpty(items)) {
      wheres.add(key + " NOT " + toInClause(items, stringifyItems));
    }
    return this;
  }

  public SqlQueryBuilder groupBy(String s) {
    groupBys.add(s);
    return this;
  }

  public SqlQueryBuilder orderBy(String orderBy) {
    this.orderBy = Optional.of(orderBy);
    return this;
  }

  public SqlQueryBuilder limit(int value) {
    return limit(String.valueOf(value));
  }

  public SqlQueryBuilder limit(String value) {
    this.limit = Optional.of(value);
    return this;
  }

  public SqlQueryBuilder offset(int value) {
    this.offset = Optional.of(value);
    return this;
  }

  private void buildSelectOrDelete(StringBuilder builder, List<Object> params) {
    if (!selects.isEmpty() || delete) {
      if (delete) {
        builder.append("DELETE");
      } else {
        builder.append("SELECT ");
        if (distinct) {
          builder.append("DISTINCT ");
        }
        builder.append(commaJoiner.join(selects));
        params.addAll(selectParams);
      }
      builder.append(" FROM ").append(from.get());
      if (!joins.isEmpty()) {
        builder.append(" ").append(spaceJoiner.join(joins));
      }
    }
    if (!wheres.isEmpty()) {
      builder.append(" WHERE ");
      builder.append(Joiner.on(" AND ").join(wheres));
      params.addAll(whereParams);
    }
    if (!groupBys.isEmpty()) {
      builder.append(" GROUP BY ");
      builder.append(Joiner.on(", ").skipNulls().join(groupBys));
    }
    if (orderBy.isPresent()) {
      builder.append(" ORDER BY ").append(orderBy.get());
    }
    if (limit.isPresent()) {
      builder.append(" LIMIT ").append(limit.get());
    }
    if (offset.isPresent()) {
      builder.append(" OFFSET ").append(offset.get());
    }
  }

  public String build() {
    StringBuilder builder = new StringBuilder();
    List<Object> params = Lists.newArrayList();
    if (insert.isPresent()) {
      if (!currentRowInsertValues.isEmpty()) {
        insertValues.add(currentRowInsertValues);
      }
      Preconditions.checkArgument(insertValues.isEmpty() || selects.isEmpty(), "must specify one of either values or selects for an insert - not both!");
      Preconditions.checkArgument(!failUpsertWithoutChanges || (!insertValues.isEmpty() || !selects.isEmpty()), "you've set up an insert with nothing to populate -- neither values nor select.  if you want to ignore queries without changes build with acceptUpsertWithoutChanges");
      if (replace) {
        builder.append("REPLACE ");
      } else if (ignoredInsert) {
        builder.append("INSERT IGNORE ");
      } else {
        builder.append("INSERT ");
      }
      builder.append(insert.get()).append(" (").append(commaJoiner.join(insertFields)).append(") ");
      if (!insertValues.isEmpty()) {
        builder.append("VALUES ");
        boolean first = true;
        for (List<String> rowValues : insertValues) {
          if (first) {
            first = false;
          } else {
            builder.append(", ");
          }
          builder.append("(").append(commaJoiner.join(rowValues)).append(")");
        }
        params.addAll(insertValueParams);
      }
      buildSelectOrDelete(builder, params);
      if (update.isPresent()) {
        builder.append(" ON DUPLICATE KEY UPDATE ");
      }
    } else if (update.isPresent()) {
      builder.append("UPDATE ").append(update.get()).append(" SET ");
    }
    if (update.isPresent()) {
      Preconditions.checkArgument(!failUpsertWithoutChanges || !updateSets.isEmpty(), "an update must have at least one set!  if you want to ignore queries without changes build with acceptUpsertWithoutChanges");
      if (insert.isPresent()) {
        Preconditions.checkArgument(!updateSets.isEmpty(), "an upsert must have at least one set!");
      }
      builder.append(commaJoiner.join(updateSets));
      params.addAll(updateSetParams);
    }
    if (!insert.isPresent()) {
      buildSelectOrDelete(builder, params);
    }
    return builder.toString();
  }
}
