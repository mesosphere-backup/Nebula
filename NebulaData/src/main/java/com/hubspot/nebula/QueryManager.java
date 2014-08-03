package com.hubspot.nebula;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.hubspot.rosetta.jdbi.RosettaResultSetMapperFactory;

public class QueryManager<T extends Result, Q extends Query> {
  private final DBI dbi;
  private final String tableName;
  private final Class<T> resultClass;

  protected QueryManager(DBI dbi, String tableName, Class<T> resultClass) {
    this.dbi = dbi;
    this.tableName = tableName;
    this.resultClass = resultClass;
  }

  // i got sick of if-statement copypasta
  private void handleSqlPredicates(SqlQueryBuilder builder, Q query) {
    try {
      final BeanInfo beanInfo = Introspector.getBeanInfo(query.getClass());

      for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
        final Method method = propertyDescriptor.getReadMethod();

        final SqlPredicate predicate = method.getAnnotation(SqlPredicate.class);

        if (predicate == null) {
          continue;
        }

        if (propertyDescriptor.getPropertyType().equals(Optional.class)) {
          if (propertyDescriptor.getReadMethod().invoke(query) == Optional.absent()) {
            continue;
          }
        }

        final String columnName = predicate.columnName().equals("") ? propertyDescriptor.getName() : predicate.columnName();

        builder.where(String.format("%s %s :%s", columnName, predicate.operator().getValue(), propertyDescriptor.getName()));
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public QueryResult<T, Q> query(Q query) {
    try (final Handle handle = dbi.open()) {
      // build query
      SqlQueryBuilder builder = new SqlQueryBuilder();

      builder.select("*").from(tableName);

      if (query.getOffset().isPresent()) {
        builder.where(query.isAscending() ? "id >= :offset" : "id <= :offset");
      }

      handleSqlPredicates(builder, query);

      builder.orderBy(query.isAscending() ? "id ASC" : "id DESC");

      builder.limit(query.getCount() + 1);


      // execute query
      final List<T> fullList = handle.createQuery(builder.build())
          .bindFromProperties(query)
          .map(RosettaResultSetMapperFactory.mapperFor(resultClass))
          .list();


      // massage results
      final List<T> objects;
      final Optional<Long> nextOffset;

      if (fullList.isEmpty() || (fullList.size() <= query.getCount())) {
        objects = fullList;
        nextOffset = Optional.absent();
      } else {
        if (query.getCount() < 1) {
          objects = Collections.emptyList();
        } else {
          objects = fullList.subList(0, query.getCount());
        }
        nextOffset = Optional.of(fullList.get(query.getCount()).getId());
      }

      return new QueryResult<>(query, objects, nextOffset);
    }
  }
}
