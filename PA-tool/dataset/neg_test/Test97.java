package pkg;
public class Test {
public int getPresentationSector() {
if (_rightSector != null)
return _rightSector.getSectorNumber();
else
return _leftSector.getSectorNumber() + 1;
}
}