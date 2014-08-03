package com.hubspot.nebula.github;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class GitHubHelper {
  private static Pattern REFS_HEADS_REGEX = Pattern.compile("^refs/heads/(.*)$");

  private GitHubHelper() {

  }

  public static Optional<String> parseBranch(WebhookPayload payload) {
    if (!Strings.isNullOrEmpty(payload.getRef())) {
      final Matcher match = REFS_HEADS_REGEX.matcher(payload.getRef());

      if (match.matches()) {
        return Optional.of(match.group(1));
      }
    }

    return Optional.absent();
  }
}
