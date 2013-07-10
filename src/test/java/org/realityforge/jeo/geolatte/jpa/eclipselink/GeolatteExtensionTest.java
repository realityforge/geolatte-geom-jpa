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

public final class GeolatteExtensionTest
{
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

  @Test
  public void basic()
    throws Exception
  {
    testType( TestGeometryEntity.class, Geometry.class, "POINT ( 1 1 )" );
    testType( TestGeometryCollectionEntity.class, GeometryCollection.class, "GEOMETRYCOLLECTION ( POINT ( 1 1 ) , POINT ( 1 1 ))" );
    testType( TestPointEntity.class, Point.class, "POINT ( 1 1 )" );
    testType( TestMultiPointEntity.class, MultiPoint.class, "MULTIPOINT ( (100.0 0.0) , (101.0 1.0) )" );
    testType( TestLineStringEntity.class, LineString.class, "LINESTRING ( 1 1, 2 1 )" );
    testType( TestMultiLineStringEntity.class, MultiLineString.class, "MULTILINESTRING ( ( 100.0 0.0, 101.0 1.0 ), ( 102.0 2.0, 103.0 3.0 ) )" );
    testType( TestPolygonEntity.class, Polygon.class, "POLYGON((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0))" );
    testType( TestMultiPolygonEntity.class, MultiPolygon.class, "MULTIPOLYGON(((100.0 0.0, 101.0 0.0, 101.0 1.0, 100.0 0.0)))" );
  }

  public void testType( final Class<?> entityType, final Class<?> geomType, final String value )
    throws Exception
  {
    final EntityManager em = DatabaseTestUtil.createEntityManager( "GeolatteEclipselinkTest" );

    assertEntityClassPresent( em, entityType );

    assertNull( em.find( entityType, 22 ) );

    em.getTransaction().begin();
    final String insertSQL =
      "INSERT INTO " + entityType.getSimpleName() + "(id,geom) VALUES (22,ST_GeometryFromText(?))";
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
