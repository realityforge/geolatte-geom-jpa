geolatte-geom-jpa
=================

[![Build Status](https://secure.travis-ci.org/realityforge/geolatte-geom-jpa.svg?branch=master)](http://travis-ci.org/realityforge/geolatte-geom-jpa)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.geolatte.jpa/geolatte-geom-jpa.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.geolatte.jpa%22%20a%3A%22geolatte-geom-jpa%22)

[Geolatte](http://www.geolatte.org/) is a small, focused java GIS project. This library adds
converters for persisting and loading Geolatte types using the jpa provider shipped with EE 7.
For support for EE6 using the EclipseLink JPA provider see the [geolatte-geom-eclipselink](https://github.com/realityforge/geolatte-geom-eclipselink) project.

Usage
-----

The user should be able to use any of the Geolatte spatial types within a persistent entity. To activate the relevant
converter needs to be added to the persistence.xml file.

For Postgres use;

    <class>org.realityforge.jeo.geolatte.jpa.PostgisConverter</class>

For Sql Server use;

    <class>org.realityforge.jeo.geolatte.jpa.SqlServerConverter</class>

Then you simply annotate the jpa field with a convert annotation as appropriate.

For Postgres use;

```java
  @Column( name = "geom1" )
  @Convert( converter = PostgisConverter.class )
  private Geometry _geom1;

  @Column( name = "geom2" )
  @Convert( converter = PostgisConverter.class )
  private Point _geom2;
```

For Sql Server use;

```java
  @Column( name = "geom1" )
  @Convert( converter = SqlServerConverter.class )
  private Geometry _geom1;

  @Column( name = "geom2" )
  @Convert( converter = SqlServerConverter.class )
  private Point _geom2;

```
