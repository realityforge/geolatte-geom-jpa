package org.realityforge.jeo.geolatte.jpa.pg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.Geometry;
import org.realityforge.jeo.geolatte.jpa.PostgisConverter;

@Entity

public class PgGeometryEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  @Convert( converter = PostgisConverter.class )
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
