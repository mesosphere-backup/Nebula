package com.hubspot.nebula.config;

import org.hibernate.validator.constraints.NotEmpty;

public class RegistryConfiguration {
  public static RegistryConfiguration disabled() {
    final RegistryConfiguration configuration = new RegistryConfiguration();
    configuration.setEnabled(false);
    return configuration;
  }

  private boolean enabled = true;

  @NotEmpty
  private String regex;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }
}
