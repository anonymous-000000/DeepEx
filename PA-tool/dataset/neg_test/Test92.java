package pkg;
public class Test {
public void testEjbComplianceVersionThreeWithDescriptor()
throws Exception
{
final MavenProjectResourcesStub project = createTestProject( "compliance-descriptor-3" );
final EjbMojo mojo = lookupMojoWithDefaultSettings( project );
project.addFile( "META-INF/ejb-jar.xml", MavenProjectResourcesStub.OUTPUT_FILE );
project.addFile( "pom.xml", MavenProjectResourcesStub.ROOT_FILE );
project.setupBuildEnvironment();
setVariableValueToObject( mojo, "generateClient", Boolean.FALSE );
setVariableValueToObject( mojo, "ejbVersion", "3.0" );
mojo.execute();
assertJarCreation( project, true, false );
}
}