package com.hubspot.nebula.deploy;

public enum DeployStatus {
  PENDING,
  IN_PROGRESS,
  SUCCESS,
  FAILED_ROLLBACK_OK,
  FAILED_ROLLBACK_FAILED,
}
