package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.sql.Blob;
import java.sql.SQLException;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectCollectionMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.sqlserver.Decoders;
import org.geolatte.geom.codec.sqlserver.Encoders;

public class SqlServerConverter
  implements Converter
{
  @Override
  public Object convertObjectValueToDataValue( final Object objectValue, final Session session )
  {
    if ( null == objectValue )
    {
      return null;
    }
    else
    {
      return Encoders.encode( (Geometry) objectValue );
    }
  }

  @Override
  public Geometry convertDataValueToObjectValue( final Object dataValue, final Session session )
  {
    if ( null == dataValue )
    {
      return null;
    }
    else
    {
      Throwable cause = null;
      if ( dataValue instanceof Blob )
      {
        try
        {
          final Blob blob = (Blob) dataValue;
          return Decoders.decode( blob.getBytes( 1, (int) blob.length() ) );
        }
        catch ( final SQLException sqle )
        {
          cause = sqle;
        }
      }
      else if ( dataValue instanceof byte[] )
      {
        return Decoders.decode( (byte[]) dataValue );
      }
      throw new IllegalStateException( "Unable to convert data value:" + dataValue, cause );
    }
  }

  @Override
  public boolean isMutable()
  {
    return false;
  }

  @Override
  public void initialize( final DatabaseMapping mapping, final Session session )
  {
    final DatabaseField field;
    if ( mapping instanceof DirectCollectionMapping )
    {
      field = ( (DirectCollectionMapping) mapping ).getDirectField();
    }
    else
    {
      field = mapping.getField();
    }
    field.setSqlType( java.sql.Types.OTHER );
    if ( null == field.getTypeName() )
    {
      field.setTypeName( "geometry" );
    }
    if ( null == field.getColumnDefinition() )
    {
      field.setColumnDefinition( "geometry" );
    }
  }
}
