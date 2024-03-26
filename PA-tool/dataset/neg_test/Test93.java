package pkg;
public class Test {
public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn) {
return (TileEntitySpecialRenderer<T>) (tileEntityIn == null ? null : this.getSpecialRendererByClass(tileEntityIn.getClass()));
}
}