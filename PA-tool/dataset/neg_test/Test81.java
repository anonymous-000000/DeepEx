package pkg;
public class Test {
@objid ("7f3a962c-784f-4cf7-93c5-647f64d6f2e9")
@SuppressWarnings("unchecked")
public <T extends MObject> T create(Class<T> metaclass, MObject referent) {
return (T) this.smFactory.createObject(this.metamodel.getMClass(metaclass), this.repoSupport.getRepository(referent));
}
}