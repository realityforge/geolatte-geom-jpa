package org.realityforge.jeo.geolatte.jpa.eclipselink;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.converters.SerializedObjectConverter;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.LineString;
import org.geolatte.geom.LinearRing;
import org.geolatte.geom.MultiLineString;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;

public class GeolatteExtension
  extends SessionEventAdapter
{
  @Override
  public void preLogin( final SessionEvent event )
  {
    final Session session = event.getSession();
    final Map<Class, ClassDescriptor> descriptorMap = session.getDescriptors();

    // Walk through all descriptors...
    for ( final Map.Entry<Class, ClassDescriptor> entry : descriptorMap.entrySet() )
    {
      final ClassDescriptor desc = entry.getValue();
      final Vector<DatabaseMapping> mappings = desc.getMappings();
      // walk through all mappings for some class...
      for ( final DatabaseMapping mapping : mappings )
      {
        if ( mapping instanceof DirectToFieldMapping )
        {
          final DirectToFieldMapping dfm = (DirectToFieldMapping) mapping;
          if ( isCandidateConverter( dfm ) )
          {
            final Class type = entry.getKey();
            final String attributeName = dfm.getAttributeName();
            final Field field = getField( type, attributeName );
            final Class<?> fieldType = field.getType();
            if ( Point.class == fieldType ||
                 Polygon.class == fieldType ||
                 LineString.class == fieldType ||
                 MultiPoint.class == fieldType ||
                 MultiPolygon.class == fieldType ||
                 MultiLineString.class == fieldType ||
                 Geometry.class == fieldType ||
                 LinearRing.class == fieldType )
            {
              final Converter converter = new PostgisConverter();
              converter.initialize( mapping, session );
              dfm.setConverter( converter );
            }
          }
        }
      }
    }
  }

  private Field getField( final Class type, final String attributeName )
  {
    Class t = type;
    while ( t != Object.class )
    {
      try
      {
        return t.getDeclaredField( attributeName );
      }
      catch ( final NoSuchFieldException nsfe )
      {
        //Ignore
      }
      t = t.getSuperclass();
    }
    throw new IllegalStateException( "Unable to find field: " + attributeName );
  }

  // only consider mappings that are deemed to produce
  // byte[] database fields from objects...
  private boolean isCandidateConverter( final DirectToFieldMapping mapping )
  {
    final Converter converter = mapping.getConverter();
    return null != converter && converter instanceof SerializedObjectConverter;
  }
}