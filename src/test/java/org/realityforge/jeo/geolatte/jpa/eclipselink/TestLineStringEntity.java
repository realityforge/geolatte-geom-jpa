package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;

@Entity
public class TestLineStringEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private LineString _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public LineString getGeom()
  {
    return _geom;
  }

  public void setGeom( final LineString geom )
  {
    _geom = geom;
  }
}