package pkg;
public class Test {
private void handleError(PartitionOwnership claimedOwnership, PartitionPump partitionPump,
PartitionProcessor partitionProcessor, Throwable throwable, PartitionContext partitionContext) {
boolean shouldRethrow = true;
if (!(throwable instanceof PartitionProcessorException)) {
shouldRethrow = false;
LOGGER.atWarning()
.addKeyValue(PARTITION_ID_KEY, partitionContext.getPartitionId())
.log("Error receiving events from partition.", throwable);
partitionProcessor.processError(new ErrorContext(partitionContext, throwable));
}
CloseReason closeReason = CloseReason.LOST_PARTITION_OWNERSHIP;
partitionProcessor.close(new CloseContext(partitionContext, closeReason));
cleanup(claimedOwnership, partitionPump);
if (shouldRethrow) {
PartitionProcessorException exception = (PartitionProcessorException) throwable;
throw LOGGER.logExceptionAsError(exception);
}
}
}