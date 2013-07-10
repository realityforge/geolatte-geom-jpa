package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

final class DatabaseTestUtil
{
  static Properties initDatabaseProperties()
  {
    final Properties properties = new Properties();
    properties.put( "javax.persistence.jdbc.driver", "org.postgresql.Driver" );
    final String databaseUrl = "jdbc:postgresql://127.0.0.1:5432/geolatte_test";
    setProperty( properties, "javax.persistence.jdbc.url", "test.db.url", databaseUrl );
    setProperty( properties, "javax.persistence.jdbc.user", "test.db.user", "geolatte" );
    setProperty( properties, "javax.persistence.jdbc.password", "test.db.password", null );
    properties.put( "eclipselink.session-event-listener", GeolatteExtension.class.getName() );
    return properties;
  }

  private static void setProperty( final Properties properties,
                                   final String key,
                                   final String systemPropertyKey, final String defaultValue )
  {
    final String value = System.getProperty( systemPropertyKey, defaultValue );
    if ( null != value )
    {
      properties.put( key, value );
    }
  }

  public static EntityManager createEntityManager( final String persistenceUnitName )
  {
    final Properties properties = initDatabaseProperties();
    final EntityManagerFactory factory = Persistence.createEntityManagerFactory( persistenceUnitName, properties );
    return factory.createEntityManager();
  }
}
