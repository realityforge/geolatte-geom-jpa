require 'buildr/git_auto_version'

desc 'Geolatte-Eclipselink Converters'
define 'geolatte-geom-eclipselink' do
  project.group = 'org.realityforge.jeo.geolatte.jpa.eclipselink'

  compile.options.source = '1.6'
  compile.options.target = '1.6'
  compile.options.lint = 'all'

  compile.with :javax_persistence,
               :eclipselink,
               :geolatte_geom,
               :postgresql,
               :postgis_jdbc

  test.using :testng

  test.with :jts,
            :slf4j_api,
            :jtds,
            :slf4j_jdk14

  package :jar
  package :sources
end
