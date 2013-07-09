package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Point;

@Entity
public class TestPointEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private Point _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public Point getGeom()
  {
    return _geom;
  }

  public void setGeom( final Point geom )
  {
    _geom = geom;
  }
}