package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

final class DatabaseTestUtil
{
  private static final String URL_KEY = "javax.persistence.jdbc.url";
  private static final String DRIVER_KEY = "javax.persistence.jdbc.driver";
  private static final String USER_KEY = "javax.persistence.jdbc.user";
  private static final String PASSWORD_KEY = "javax.persistence.jdbc.password";

  private DatabaseTestUtil()
  {
  }

  static Properties initDatabaseProperties( final boolean control )
  {
    final String baseURL = System.getProperty( "test.db.base_url", "jdbc:postgresql://127.0.0.1:5432" );

    final Properties properties = new Properties();
    properties.put( DRIVER_KEY, "org.postgresql.Driver" );
    final String databaseUrl = baseURL + "/" + ( control ? "postgres" : "geolatte_test" );
    setProperty( properties, URL_KEY, "test.db.url", databaseUrl );
    setProperty( properties, USER_KEY, "test.db.user", "postgres" );
    setProperty( properties, PASSWORD_KEY, "test.db.password", null );
    properties.put( "eclipselink.session-event-listener", GeolatteExtension.class.getName() );
    return properties;
  }

  private static void setProperty( final Properties properties,
                                   final String key,
                                   final String systemPropertyKey,
                                   final String defaultValue )
  {
    final String value = System.getProperty( systemPropertyKey, defaultValue );
    if ( null != value )
    {
      properties.put( key, value );
    }
  }

  public static EntityManager createEntityManager( final String persistenceUnitName )
  {
    final Properties properties = initDatabaseProperties( false );
    final EntityManagerFactory factory = Persistence.createEntityManagerFactory( persistenceUnitName, properties );
    return factory.createEntityManager();
  }

  public static void setupDatabase()
    throws Exception
  {
    try
    {
      tearDownDatabase();
    }
    catch ( Exception e )
    {
      //Ignore
    }
    final Connection connection = initConnection( true );
    try
    {
      connection.createStatement().execute( "CREATE DATABASE geolatte_test" );
    }
    finally
    {
      disposeConnection( connection );
    }
    final Connection connection2 = initConnection( false );
    try
    {
      connection2.createStatement().execute( "CREATE EXTENSION postgis" );
    }
    finally
    {
      disposeConnection( connection2 );
    }
  }

  static void tearDownDatabase()
    throws Exception
  {
    final Connection connection = initConnection( true );
    try
    {
      connection.createStatement().execute( "SELECT pg_terminate_backend(pg_stat_activity.pid)\n" +
                                            "FROM pg_stat_activity\n" +
                                            "WHERE pg_stat_activity.datname = 'geolatte_test'" );
      connection.createStatement().execute( "DROP DATABASE geolatte_test" );
    }
    finally
    {
      disposeConnection( connection );
    }
  }
  private static void disposeConnection( final Connection connection )
  {
    if ( null != connection )
    {
      try
      {
        connection.close();
      }
      catch ( final Exception e )
      {
        throw new IllegalStateException( e.getMessage(), e );
      }
    }
  }

  private static Connection initConnection( final boolean control )
  {
    final Properties properties = initDatabaseProperties( control );
    try
    {
      Class.forName( properties.getProperty( DRIVER_KEY ) );
    }
    catch ( final Exception e )
    {
      throw new IllegalStateException( e.getMessage(), e );
    }
    try
    {
      return DriverManager.getConnection( properties.getProperty( URL_KEY ),
                                          properties.getProperty( USER_KEY ),
                                          properties.getProperty( PASSWORD_KEY ) );
    }
    catch ( final Exception e )
    {
      throw new IllegalStateException( e.getMessage(), e );
    }
  }
}
