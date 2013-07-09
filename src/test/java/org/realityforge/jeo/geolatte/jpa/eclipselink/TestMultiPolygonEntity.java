package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Polygon;

@Entity
public class TestMultiPolygonEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private MultiPolygon _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public MultiPolygon getGeom()
  {
    return _geom;
  }

  public void setGeom( final MultiPolygon geom )
  {
    _geom = geom;
  }
}