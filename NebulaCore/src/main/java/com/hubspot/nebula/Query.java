package com.hubspot.nebula;

import com.google.common.base.Optional;

public interface Query {
  public int getCount();
  public Optional<Long> getOffset();
  public boolean isAscending();
}
