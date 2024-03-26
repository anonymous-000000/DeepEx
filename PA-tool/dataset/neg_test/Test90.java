package pkg;
public class Test {
@Test
public void deleteEntityTypeTest2() throws Exception {
Empty expectedResponse = Empty.newBuilder().build();
Operation resultOperation =
Operation.newBuilder()
.setName("deleteEntityTypeTest")
.setDone(true)
.setResponse(Any.pack(expectedResponse))
.build();
mockFeaturestoreService.addResponse(resultOperation);
String name = "name3373707";
client.deleteEntityTypeAsync(name).get();
List<AbstractMessage> actualRequests = mockFeaturestoreService.getRequests();
Assert.assertEquals(1, actualRequests.size());
DeleteEntityTypeRequest actualRequest = ((DeleteEntityTypeRequest) actualRequests.get(0));
Assert.assertEquals(name, actualRequest.getName());
Assert.assertTrue(
channelProvider.isHeaderSent(
ApiClientHeaderProvider.getDefaultApiClientHeaderKey(),
GaxGrpcProperties.getDefaultApiClientHeaderPattern()));
}
}