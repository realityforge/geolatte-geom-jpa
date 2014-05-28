package org.realityforge.jeo.geolatte.jpa.mssql;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.MultiLineString;
import org.realityforge.jeo.geolatte.jpa.SqlServerConverter;

@Entity
public class MsMultiLineStringEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom", columnDefinition = "GEOMETRY" )
  @Convert( converter = SqlServerConverter.class )
  private MultiLineString _geom;

  public Integer getId()
  {
    return _id;
  }

  public void setId( final Integer id )
  {
    _id = id;
  }

  public MultiLineString getGeom()
  {
    return _geom;
  }

  public void setGeom( final MultiLineString geom )
  {
    _geom = geom;
  }
}
