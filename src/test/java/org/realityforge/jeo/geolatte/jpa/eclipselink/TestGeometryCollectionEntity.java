package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.GeometryCollection;

@Entity
public class TestGeometryCollectionEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private GeometryCollection _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public GeometryCollection getGeom()
  {
    return _geom;
  }

  public void setGeom( final GeometryCollection geom )
  {
    _geom = geom;
  }
}