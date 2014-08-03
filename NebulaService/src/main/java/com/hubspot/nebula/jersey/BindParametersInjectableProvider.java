package com.hubspot.nebula.jersey;

import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

// shamelessly borrowed from dropwizard-hubspot
public class BindParametersInjectableProvider implements InjectableProvider<BindQueryParams, Parameter> {
  private final ParameterConverter converter;

  @Inject
  public BindParametersInjectableProvider(ParameterConverter converter) {
    this.converter = converter;
  }

  @Override
  public Injectable<Object> getInjectable(ComponentContext ic, final BindQueryParams bind, final Parameter p) {
    return new AbstractHttpContextInjectable<Object>() {
      @Override
      public Object getValue(HttpContext context) {
        Object parameterObject;

        try {
          parameterObject = converter.convert(p.getParameterType(), getParameterMap(bind, context));
        } catch (IllegalArgumentException e) {
          throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(Arrays.asList(e.getMessage())).build());
        }

        return parameterObject;
      }

      private Map<String, Object> getParameterMap(BindQueryParams bind, HttpContext context) {
        Map<String, Object> mergedParams = Maps.newHashMap();

        mergedParams.putAll(context.getRequest().getQueryParameters());

        return mergedParams;
      }
    };
  }

  @Override
  public ComponentScope getScope() {
    return ComponentScope.PerRequest;
  }

}