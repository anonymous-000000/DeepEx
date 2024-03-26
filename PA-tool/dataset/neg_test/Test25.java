package pkg;
public class Test {
@After
public void tearDown() throws IOException, StorageEngineException {
super.tearDown();
Thread.currentThread().setName(oldThreadName);
for (TsFileResource tsFileResource : seqResources) {
FileReaderManager.getInstance().closeFileAndRemoveReader(tsFileResource.getTsFilePath());
}
for (TsFileResource tsFileResource : unseqResources) {
FileReaderManager.getInstance().closeFileAndRemoveReader(tsFileResource.getTsFilePath());
}
}
}