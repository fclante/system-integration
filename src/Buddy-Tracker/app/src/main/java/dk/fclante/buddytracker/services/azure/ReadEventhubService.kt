package dk.fclante.buddytracker.services.azure

import com.azure.core.credential.AzureSasCredential
import com.azure.messaging.eventhubs.EventData
import com.azure.messaging.eventhubs.EventHubClientBuilder
import com.azure.messaging.eventhubs.EventProcessorClient
import com.azure.messaging.eventhubs.EventProcessorClientBuilder
import com.azure.messaging.eventhubs.checkpointstore.blob.BlobCheckpointStore
import com.azure.messaging.eventhubs.models.ErrorContext
import com.azure.messaging.eventhubs.models.EventContext
import com.azure.messaging.eventhubs.models.PartitionContext
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.common.policy.RequestRetryOptions

import com.azure.storage.common.policy.RetryPolicyType
import dk.fclante.buddytracker.TokenFetcher
import java.util.function.Consumer


open class ReadEventhubService() {

    private var eventProcessorClient: EventProcessorClient? = null
    private val namespaceName = "frey-school.servicebus.windows.net"
    private val eventHubName = "tracker"

    open fun configure() {
// Create an AzureSasCredential
        val sasCredential = AzureSasCredential(TokenFetcher.fetchSasToken())

        val retryOptions =
            RequestRetryOptions(
                RetryPolicyType.EXPONENTIAL,
                3,
                30,
                90000,
                3000,
                null
            )
        // Create a blob container client that you use later to build an event processor client to receive and process events
        val blobContainerAsyncClient = BlobContainerClientBuilder()
            .credential(sasCredential)
            .endpoint("https://freyschoolstorage.blob.core.windows.net/frey-school-container")
            .retryOptions(retryOptions)
            .buildAsyncClient()

        // Create an event processor client to receive and process events and errors.
         eventProcessorClient = EventProcessorClientBuilder()
            .fullyQualifiedNamespace(namespaceName)
            .eventHubName(eventHubName)
            .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
            .processEvent(PARTITION_PROCESSOR)
            .processError(ERROR_HANDLER)
            .checkpointStore(BlobCheckpointStore(blobContainerAsyncClient))
            .buildEventProcessorClient()
    }

    fun stopReading() {
        println("Stopping event processor")
        eventProcessorClient?.stop()
        println("Event processor stopped.")
    }

    open fun startReading() {
        println("Starting event processor")
         eventProcessorClient?.start()
    }

    val PARTITION_PROCESSOR: Consumer<EventContext> =
        Consumer<EventContext> { eventContext ->
            val partitionContext: PartitionContext = eventContext.getPartitionContext()
            val eventData: EventData = eventContext.getEventData()

            System.out.printf(
                "Processing event from partition %s with sequence number %d with body: %s%n",
                partitionContext.partitionId,
                eventData.sequenceNumber,
                eventData.bodyAsString
            )

            // Every 10 events received, it will update the checkpoint stored in Azure Blob Storage.
            if (eventData.sequenceNumber % 10 == 0L) {
                eventContext.updateCheckpoint()
            }
        }

    val ERROR_HANDLER: Consumer<ErrorContext> = Consumer<ErrorContext> { errorContext ->
        System.out.printf(
            "Error occurred in partition processor for partition %s, %s.%n",
            errorContext.getPartitionContext().getPartitionId(),
            errorContext.getThrowable()
        )
    }
}


