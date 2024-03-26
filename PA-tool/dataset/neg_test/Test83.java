package pkg;
public class Test {
public void testSortDescendingIndexed() {
testSortDescending(new long[] {}, 0, 0, new long[] {});
testSortDescending(new long[] {1}, 0, 1, new long[] {1});
testSortDescending(new long[] {1, 2}, 0, 2, new long[] {2, 1});
testSortDescending(new long[] {1, 3, 1}, 0, 2, new long[] {3, 1, 1});
testSortDescending(new long[] {1, 3, 1}, 0, 1, new long[] {1, 3, 1});
testSortDescending(
new long[] {GREATEST - 1, 1, GREATEST - 2, 2},
1,
3,
new long[] {GREATEST - 1, GREATEST - 2, 1, 2});
}
}