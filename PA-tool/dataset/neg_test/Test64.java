package pkg;
public class Test {
public Component getListCellRendererComponent(JList<?> list,
Object value,
int index,
boolean isSelected,
boolean cellHasFocus) {
if(textMenu.getSelectedIndex() == fp.RANGE_TEXT) {
super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
if(index == -1) {
index = choice.getSelectedIndex();
}
if(choice.getBit(index)) {
setIcon(yesImage);
}
else {
setIcon(blankImage);
}
} else {
super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
setIcon(blankImage);
}
return this;
}
}