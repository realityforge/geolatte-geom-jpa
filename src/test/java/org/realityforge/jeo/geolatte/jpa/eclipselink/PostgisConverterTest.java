package org.realityforge.jeo.geolatte.jpa.eclipselink;

import org.testng.annotations.Test;

public final class PostgisConverterTest
  extends AbstractConverterTest
{
  @Override
  protected boolean isPostgres()
  {
    return true;
  }

  @Test
  public void basic()
    throws Exception
  {
    performTests();
  }
}
