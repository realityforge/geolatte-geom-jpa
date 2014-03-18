package org.realityforge.jeo.geolatte.jpa.eclipselink;

import javax.persistence.EntityManager;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;
import org.geolatte.geom.LineString;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.realityforge.jeo.geolatte.jpa.mssql.MsGeometryCollectionEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsGeometryEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsLineStringEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsMultiLineStringEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsMultiPointEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsMultiPolygonEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsPointEntity;
import org.realityforge.jeo.geolatte.jpa.mssql.MsPolygonEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgGeometryCollectionEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgGeometryEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgLineStringEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgMultiLineStringEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgMultiPointEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgMultiPolygonEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgPointEntity;
import org.realityforge.jeo.geolatte.jpa.pg.PgPolygonEntity;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractConverterTest
{
  protected abstract boolean isPostgres();

  @BeforeMethod
  public void setup()
    throws Exception
  {
    DatabaseTestUtil.setupDatabase( isPostgres() );
  }

  @AfterMethod
  public void tearDown()
    throws Exception
  {
    DatabaseTestUtil.tearDownDatabase( isPostgres() );
  }

  protected void performTests()
    throws Exception
  {
    doTests( isPostgres() );
  }

  private void doTests( final boolean postgres )
    throws Exception
  {
    testType( postgres,
              postgres ? PgGeometryEntity.class : MsGeometryEntity.class,
              Geometry.class,
              "POINT ( 1 1 )" );
    testType( postgres,
              postgres ? PgGeometryCollectionEntity.class : MsGeometryCollectionEntity.class,
              GeometryCollection.class,
              "GEOMETRYCOLLECTION ( POINT ( 1 1 ) , POINT ( 1 1 ))" );
    testType( postgres,
              postgres ? PgPointEntity.class : MsPointEntity.class,
              Point.class,
              "POINT ( 1 1 )" );
    testType( postgres,
              postgres ? PgMultiPointEntity.class : MsMultiPointEntity.class,
              MultiPoint.class,
              "MULTIPOINT ( (100.0 0.0) , (101.0 1.0) )" );
    testType( postgres,
              postgres ? PgLineStringEntity.class : MsLineStringEntity.class,
              LineString.class,
              "LINESTRING ( 1 1, 2 1 )" );
    testType( postgres,
              postgres ? PgMultiLineStringEntity.class : MsMultiLineStringEntity.class,
              MultiLineString.class,
              "MULTILINESTRING ( ( 100.0 0.0, 101.0 1.0 ), ( 102.0 2.0, 103.0 3.0 ) )" );
    testType( postgres,
              postgres ? PgPolygonEntity.class : MsPolygonEntity.class,
              Polygon.class,
              "POLYGON((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0))" );
    testType( postgres,
              postgres ? PgMultiPolygonEntity.class : MsMultiPolygonEntity.class,
              MultiPolygon.class,
              "MULTIPOLYGON(((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0)))" );
  }

  private void testType( final boolean postgres,
                         final Class<?> entityType,
                         final Class<?> geomType,
                         final String value )
    throws Exception
  {
    final EntityManager em = DatabaseTestUtil.createEntityManager( isPostgres(), "GeolatteEclipselinkTest" );

    assertEntityClassPresent( em, entityType );

    assertNull( em.find( entityType, 22 ) );

    em.getTransaction().begin();
    final String fromTextFunction = postgres ? "ST_GeometryFromText(?)" : "geometry::STGeomFromText(?,0)";
    final String insertSQL =
      "INSERT INTO " + entityType.getSimpleName() + "(id,geom) VALUES (22," + fromTextFunction + ")";
    em.createNativeQuery( insertSQL ).setParameter( 1, value ).executeUpdate();
    em.getTransaction().commit();

    final Object entity = em.find( entityType, 22 );
    final Object geom = entityType.getMethod( "getGeom" ).invoke( entity );

    assertNotNull( entity );
    em.remove( entity );
    assertNull( em.find( entityType, 22 ) );

    final Object e2 = entityType.newInstance();
    entityType.getMethod( "setId", Integer.class ).invoke( e2, 23 );
    entityType.getMethod( "setGeom", geomType ).invoke( e2, geom );

    em.persist( e2 );
    final Object e2FromDB = em.find( entityType, 23 );
    assertNotNull( e2FromDB );
    assertEquals( entityType.getMethod( "getGeom" ).invoke( entity ), geom );
    em.remove( e2 );

    em.close();
  }

  private void assertEntityClassPresent( final EntityManager em, final Class<?> entityClass )
  {
    assertNotNull( em.getMetamodel().managedType( entityClass ) );
  }
}
