package org.realityforge.jeo.geolatte.jpa.mssql;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Point;
import org.realityforge.jeo.geolatte.jpa.SqlServerConverter;

@Entity
public class MsPointEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom", columnDefinition = "GEOMETRY" )
  @Convert( converter = SqlServerConverter.class )
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