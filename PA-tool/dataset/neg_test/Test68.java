package pkg;
public class Test {
@Test
public void noHeadRetentionErrorSize() {
PublishProcessor<Integer> source = PublishProcessor.create();
FlowableReplay<Integer> co = (FlowableReplay<Integer>)source
.replay(1);
co.test();
co.connect();
BoundedReplayBuffer<Integer> buf = (BoundedReplayBuffer<Integer>)(co.current.get().buffer);
source.onNext(1);
source.onNext(2);
source.onError(new TestException());
assertNull(buf.get().value);
Object o = buf.get();
buf.trimHead();
assertSame(o, buf.get());
}
}