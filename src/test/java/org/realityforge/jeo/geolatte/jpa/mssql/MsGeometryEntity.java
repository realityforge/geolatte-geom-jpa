package org.realityforge.jeo.geolatte.jpa.mssql;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Geometry;
import org.realityforge.jeo.geolatte.jpa.SqlServerConverter;

@Entity
public class MsGeometryEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom", columnDefinition = "GEOMETRY")
  @Convert( converter = SqlServerConverter.class )
  private Geometry _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public Geometry getGeom()
  {
    return _geom;
  }

  public void setGeom( final Geometry geom )
  {
    _geom = geom;
  }
}