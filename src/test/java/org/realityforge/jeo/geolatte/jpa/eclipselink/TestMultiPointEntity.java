package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.Point;

@Entity
public class TestMultiPointEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  private MultiPoint _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public MultiPoint getGeom()
  {
    return _geom;
  }

  public void setGeom( final MultiPoint geom )
  {
    _geom = geom;
  }
}