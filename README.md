geolatte-geom-eclipselink
=========================

[![Build Status](https://secure.travis-ci.org/realityforge/geolatte-geom-eclipselink.png?branch=master)](http://travis-ci.org/realityforge/geolatte-geom-eclipselink)

[Geolatte](http://www.geolatte.org/) is a small, focused java GIS project. This library adds
converters for persisting and loading Geolatte types using the Eclipselink jpa provider.

Currently support is restricted to Postgres but it is expected that SQL Server support will
be added in the near future.

Usage
-----

The user should be able to use any of the Geolatte spatial types within a persistent entity. The
user then just needs to specify the session event listener. This can be set using a property in the
persistent.xml file using a section such as;

    <properties>
      <property name="eclipselink.session-event-listener" value="org.realityforge.jeo.geolatte.jpa.eclipselink.GeolatteExtension"/>
    </properties>
