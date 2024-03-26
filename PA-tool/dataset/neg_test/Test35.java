package pkg;
public class Test {
@Override
public void mouseReleased(MouseEvent me)
{
if (startModel != null && moved)
{
Point p = me.getPoint();
JList<ListItem> endList = yesList;
DefaultListModel<ListItem> endModel = yesModel;
if (me.getComponent() == yesList)
{
if (!yesList.contains (p))
{
endList = noList;
endModel = noModel;
}
}
else
{
if (noList.contains (p))
{
setCursor(Cursor.getDefaultCursor());
moved = false;
return;
}
p = SwingUtilities.convertPoint (noList, p, yesList);
}
int index = endList.locationToIndex(p);
if (index > -1)
{
startModel.removeElement(selObject);
endModel.add(index, selObject);
startList.clearSelection();
endList.clearSelection();
endList.setSelectedValue(selObject, true);
setIsChanged(true);
}
}
startList = null;
startModel = null;
selObject = null;
moved = false;
setCursor(Cursor.getDefaultCursor());
}
}