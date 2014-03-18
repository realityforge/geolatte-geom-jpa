package org.realityforge.jeo.geolatte.jpa.mssql;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.MultiPolygon;
import org.realityforge.jeo.geolatte.jpa.SqlServerConverter;

@Entity
public class MsMultiPolygonEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom", columnDefinition = "GEOMETRY" )
  @Convert( converter = SqlServerConverter.class )
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