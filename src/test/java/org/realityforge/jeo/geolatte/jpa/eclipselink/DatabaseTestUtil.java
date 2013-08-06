package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

  private static Properties initDatabaseProperties( final boolean postgres, final boolean control )
  {
    final Properties properties = new Properties();

    final String defaultURL;
    if ( postgres )
    {
      properties.put( DRIVER_KEY, "org.postgresql.Driver" );
      defaultURL = "jdbc:postgresql://127.0.0.1:5432";
    }
    else
    {
      properties.put( DRIVER_KEY, "net.sourceforge.jtds.jdbc.Driver" );
      properties.put( "eclipselink.ddl-generation", "drop-and-create-tables" );
      defaultURL = "jdbc:jtds:sqlserver://127.0.0.1:1432/";
    }

    final String baseURL = System.getProperty( "test.db.base_url", defaultURL );
    final String databaseUrl = baseURL + "/" + ( control ? ( postgres ? "postgres" : "master" ) : "geolatte_test" );
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

  static EntityManager createEntityManager( final boolean postgres, final String persistenceUnitName )
  {
    final Properties properties = initDatabaseProperties( postgres, false );
    final EntityManagerFactory factory = Persistence.createEntityManagerFactory( persistenceUnitName, properties );
    return factory.createEntityManager();
  }

  static void setupDatabase( final boolean postgres )
    throws Exception
  {
    try
    {
      tearDownDatabase( postgres );
    }
    catch ( Exception e )
    {
      //Ignore
    }
    final Connection connection = initConnection( postgres, true );
    try
    {
      execute( connection, "CREATE DATABASE geolatte_test" );
    }
    finally
    {
      disposeConnection( connection );
    }
    if ( postgres )
    {
      final Connection connection2 = initConnection( postgres, false );
      try
      {
        execute( connection2, "CREATE EXTENSION postgis" );
      }
      finally
      {
        disposeConnection( connection2 );
      }
    }
  }

  static void tearDownDatabase( final boolean postgres )
    throws Exception
  {
    final Connection connection = initConnection( postgres, true );
    try
    {
      if ( "PostgreSQL Native Driver".equals( connection.getMetaData().getDriverName() ) )
      {
        // Post 9.1 terminate option
        final int majorVersion = connection.getMetaData().getDatabaseMajorVersion();
        final int minorVersion = connection.getMetaData().getDatabaseMinorVersion();
        final int version = majorVersion * 100 + minorVersion;
        if ( version >= 902 )
        {
          execute( connection,
                   "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'geolatte_test'" );
        }
        else
        {
          execute( connection,
                   "SELECT pg_terminate_backend(pg_stat_activity.procpid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'geolatte_test'" );
        }
      }
      execute( connection, "DROP DATABASE geolatte_test" );
    }
    finally
    {
      disposeConnection( connection );
    }
  }

  private static void execute( final Connection connection, final String sql )
    throws SQLException
  {
    final Statement statement = connection.createStatement();
    statement.execute( sql );
    statement.close();
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

  private static Connection initConnection( final boolean postgres, final boolean control )
  {
    final Properties properties = initDatabaseProperties( postgres, control );
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
