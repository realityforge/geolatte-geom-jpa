package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;

@Entity
public class TestPolygonEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private Polygon _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public Polygon getGeom()
  {
    return _geom;
  }

  public void setGeom( final Polygon geom )
  {
    _geom = geom;
  }
}