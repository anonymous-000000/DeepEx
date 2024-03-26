package pkg;
public class Test {
private static Component makeEditButtonBar(List<AbstractButton> list) {
int size = list.size();
JPanel p = new JPanel(new GridLayout(1, size, 0, 0)) {
@Override public Dimension getMaximumSize() {
return super.getPreferredSize();
}
};
list.forEach(b -> {
b.setIcon(new ToggleButtonBarCellIcon());
p.add(b);
});
p.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
p.setOpaque(false);
return new JLayer<>(p, new EditMenuLayerUI<>(list.get(size - 1)));
}
}