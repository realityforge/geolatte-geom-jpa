package org.realityforge.jeo.geolatte.jpa.pg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.LineString;
import org.realityforge.jeo.geolatte.jpa.PostgisConverter;

@Entity
public class PgLineStringEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  @Convert( converter = PostgisConverter.class )
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
