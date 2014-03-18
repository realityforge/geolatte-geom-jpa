require 'buildr/git_auto_version'

PROVIDED_DEPS = [:javax_jsr305, :javaee_api, :javax_javaee_endorsed]
OPTIONAL_DEPS = [:postgresql, :postgis_jdbc]

desc 'Geolatte-JPA Converters'
define 'geolatte-geom-jpa' do
  project.group = 'org.realityforge.geolatte.jpa'

  compile.options.source = '1.7'
  compile.options.target = '1.7'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache2_license
  pom.add_github_project('realityforge/geolatte-geom-jpa')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat PROVIDED_DEPS
  pom.optional_dependencies.concat OPTIONAL_DEPS

  compile.with PROVIDED_DEPS, OPTIONAL_DEPS, :geolatte_geom

  test.using :testng, :excludegroups => ['sqlserver']

  test.with :eclipselink,
            :jts,
            :slf4j_api,
            :jtds,
            :slf4j_jdk14

  package :jar
  package :sources
  package :javadoc
end
