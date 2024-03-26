package pkg;
public class Test {
@Override
public boolean isDefined( Definitions rootDefinitions, IFile sourceFile, String def )
{
return _buildConfigSyms.get().containsKey( def );
}
}