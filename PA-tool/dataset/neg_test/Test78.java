package pkg;
public class Test {
void notifyOnChildAdded(Resource child) {
List<FolderListener> l2 = new ArrayList<FolderListener>(folderListeners);
for (FolderListener l : l2) {
l.onChildAdded(this, child);
}
cache.remove(this);
}
}