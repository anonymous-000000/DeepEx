package pkg;
public class Test {
@Test
public void getConversationTest2() throws Exception {
Conversation expectedResponse =
Conversation.newBuilder()
.setName(
ConversationName.ofProjectConversationName("[PROJECT]", "[CONVERSATION]")
.toString())
.setConversationProfile(
ConversationProfileName.ofProjectConversationProfileName(
"[PROJECT]", "[CONVERSATION_PROFILE]")
.toString())
.setPhoneNumber(ConversationPhoneNumber.newBuilder().build())
.setStartTime(Timestamp.newBuilder().build())
.setEndTime(Timestamp.newBuilder().build())
.build();
mockService.addResponse(expectedResponse);
String name = "projects/project-3460/conversations/conversation-3460";
Conversation actualResponse = client.getConversation(name);
Assert.assertEquals(expectedResponse, actualResponse);
List<String> actualRequests = mockService.getRequestPaths();
Assert.assertEquals(1, actualRequests.size());
String apiClientHeaderKey =
mockService
.getRequestHeaders()
.get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
.iterator()
.next();
Assert.assertTrue(
GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
.matcher(apiClientHeaderKey)
.matches());
}
}