package com.hubspot.nebula.github;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.nebula.NebulaDataModule;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.Response;

@Singleton
public class GitHubClient {
  private final AsyncHttpClient asyncHttpClient;
  private final String baseUrl;
  private final Optional<String> accessToken;

  @Inject
  public GitHubClient(AsyncHttpClient asyncHttpClient,
                      @Named(NebulaDataModule.GITHUB_BASE_URL) String baseUrl,
                      @Named(NebulaDataModule.GITHUB_ACCESS_TOKEN) Optional<String> accessToken) {
    this.asyncHttpClient = asyncHttpClient;
    this.baseUrl = baseUrl;
    this.accessToken = accessToken;
  }

  public Optional<String> getContents(String user, String repo, String path, Optional<String> ref) {
    try {
      final BoundRequestBuilder builder = asyncHttpClient.prepareGet(String.format("%s/repos/%s/%s/contents/%s", baseUrl, user, repo, path))
          .addHeader("Accept", "application/vnd.github.v3.raw");

      if (ref.isPresent()) {
        builder.addQueryParameter("ref", ref.get());
      }

      if (accessToken.isPresent()) {
        builder.addQueryParameter("access_token", accessToken.get());
      }

      final Response response = builder.execute().get();

      if (response.getStatusCode() == 200) {
        return Optional.of(response.getResponseBody());
      } else if (response.getStatusCode() == 404) {
        return Optional.absent();
      } else {
        throw new RuntimeException(response.getResponseBody());  // TODO: better exception
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
}
