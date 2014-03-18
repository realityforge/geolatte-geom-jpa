package org.realityforge.jeo.geolatte.jpa;

import java.sql.Blob;
import java.sql.SQLException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.sqlserver.Decoders;
import org.geolatte.geom.codec.sqlserver.Encoders;

@Converter
public class SqlServerConverter<T extends Geometry>
  implements AttributeConverter<T, Object>
{
  @Override
  public Object convertToDatabaseColumn( final T attribute )
  {
    if ( null == attribute )
    {
      return null;
    }
    else
    {
      return Encoders.encode( attribute );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T convertToEntityAttribute( final Object dbData )
  {
    if ( null == dbData )
    {
      return null;
    }
    else
    {
      Throwable cause = null;
      if ( dbData instanceof Blob )
      {
        try
        {
          final Blob blob = (Blob) dbData;
          return (T) Decoders.decode( blob.getBytes( 1, (int) blob.length() ) );
        }
        catch ( final SQLException sqle )
        {
          cause = sqle;
        }
      }
      else if ( dbData instanceof byte[] )
      {
        return (T) Decoders.decode( (byte[]) dbData );
      }
      throw new IllegalStateException( "Unable to convert data value:" + dbData, cause );
    }
  }
}
