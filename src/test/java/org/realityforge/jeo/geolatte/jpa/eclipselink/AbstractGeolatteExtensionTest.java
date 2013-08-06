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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public abstract class AbstractGeolatteExtensionTest
{
  protected abstract boolean isPostgres();

  @BeforeMethod
  public void setup()
    throws Exception
  {
    DatabaseTestUtil.setupDatabase();
  }

  @AfterMethod
  public void tearDown()
    throws Exception
  {
    DatabaseTestUtil.tearDownDatabase();
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
              TestGeometryEntity.class,
              Geometry.class,
              "POINT ( 1 1 )" );
    testType( postgres,
              TestGeometryCollectionEntity.class,
              GeometryCollection.class,
              "GEOMETRYCOLLECTION ( POINT ( 1 1 ) , POINT ( 1 1 ))" );
    testType( postgres,
              TestPointEntity.class,
              Point.class,
              "POINT ( 1 1 )" );
    testType( postgres,
              TestMultiPointEntity.class,
              MultiPoint.class,
              "MULTIPOINT ( (100.0 0.0) , (101.0 1.0) )" );
    testType( postgres,
              TestLineStringEntity.class,
              LineString.class,
              "LINESTRING ( 1 1, 2 1 )" );
    testType( postgres,
              TestMultiLineStringEntity.class,
              MultiLineString.class,
              "MULTILINESTRING ( ( 100.0 0.0, 101.0 1.0 ), ( 102.0 2.0, 103.0 3.0 ) )" );
    testType( postgres,
              TestPolygonEntity.class,
              Polygon.class,
              "POLYGON((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0))" );
    testType( postgres,
              TestMultiPolygonEntity.class,
              MultiPolygon.class,
              "MULTIPOLYGON(((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0)))" );
  }

  private void testType( final boolean postgres,
                         final Class<?> entityType,
                         final Class<?> geomType,
                         final String value )
    throws Exception
  {
    final EntityManager em = DatabaseTestUtil.createEntityManager( "GeolatteEclipselinkTest" );

    assertEntityClassPresent( em, entityType );

    assertNull( em.find( entityType, 22 ) );

    em.getTransaction().begin();
    final String fromTextFunction = postgres ? "ST_GeometryFromText(?)" : "geometry::STGeomFromText(?)" ;
    final String insertSQL =
      "INSERT INTO " + entityType.getSimpleName() + "(id,geom) VALUES (22," + fromTextFunction + ")" ;
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
