require 'buildr/git_auto_version'

desc 'Geolatte-Eclipselink Converters'
define 'geolatte-geom-eclipselink' do
  project.group = 'org.realityforge.jeo.geolatte.jpa.eclipselink'

  compile.options.source = '1.7'
  compile.options.target = '1.7'
  compile.options.lint = 'all'

  pom.add_apache2_license
  pom.add_github_project('realityforge/geolatte-geom-jpa')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat [:javax_jsr305, :javaee_api, :javax_javaee_endorsed, :eclipselink]
  pom.optional_dependencies.concat [:postgresql, :postgis_jdbc]

  compile.with :javax_jsr305,
               :javaee_api,
               :javax_javaee_endorsed,
               :eclipselink,
               :geolatte_geom,
               :postgresql,
               :postgis_jdbc

  test.using :testng, :excludegroups => ['sqlserver']

  test.with :jts,
            :slf4j_api,
            :jtds,
            :slf4j_jdk14

  package :jar
  package :sources
  package :javadoc
end
