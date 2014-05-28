package org.realityforge.jeo.geolatte.jpa.pg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.geolatte.geom.MultiPolygon;
import org.realityforge.jeo.geolatte.jpa.PostgisConverter;

@Entity
public class PgMultiPolygonEntity
  implements Serializable
{
  @Id
  @Column( name = "id" )
  private Integer _id;

  @Column( name = "geom" )
  @Convert( converter = PostgisConverter.class )
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
