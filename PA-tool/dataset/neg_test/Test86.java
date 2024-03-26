package pkg;
public class Test {
public TraversalConfig addFilters(Collection<CatalogFilter> filters) {
if (filters == null) { return this; }
this.filters.addAll(filters);
return this;
}
}