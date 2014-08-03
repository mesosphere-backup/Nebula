package com.hubspot.nebula.config;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class GitHubConfiguration {
  public static final String DEFAULT_GITHUB_BASE_URL = "https://api.github.com";

  private String baseUrl = DEFAULT_GITHUB_BASE_URL;

  private String accessToken = null;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Optional<String> getAccessToken() {
    return Optional.fromNullable(Strings.nullToEmpty(accessToken));
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
