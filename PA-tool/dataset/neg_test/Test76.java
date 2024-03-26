package pkg;
public class Test {
@Override
public void putRulesByParent(Ruleable parent, List<Rule> rules) {
parent = checkNotNull(parent, "parent is required");
String parentIdentifier = parent.getIdentifier();
if (Strings.isNullOrEmpty(parentIdentifier)) {
throw new IllegalArgumentException("Parent must have an identifier.");
}
rules = checkNotNull(rules, "Rules List is required");
for (Rule rule : rules) {
addRule(rule);
}
List<String> rulesIds = rules.stream().map(Rule::getId).collect(Collectors.toList());
cache.put(parentIdentifier, rulesIds, PARENT_RULES_CACHE);
}
}