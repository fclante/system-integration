package dk.fclante.buddytracker.services.azure

import com.azure.messaging.eventhubs.EventData
import com.azure.messaging.eventhubs.EventHubClientBuilder
import dk.fclante.buddytracker.ui.models.EventMessage


open class EventhubService() {

    /**
     * Code sample for publishing events.
     * @throws IllegalArgumentException if the EventData is bigger than the max batch size.
     */
    open fun <T> publishEvents(eventMessages: List<EventMessage<T>>) {

        val connectionString =
            "Endpoint=sb://frey-school.servicebus.windows.net/;SharedAccessKeyName=ReadWriteToEventHub;SharedAccessKey=u4hRMZlAbD7arGUnajQowy3nBS3aJ+KBd+AEhJ0Fq3g=;EntityPath=tracker"

        val producer = EventHubClientBuilder()
            .connectionString(connectionString)
            .buildProducerClient()


        // sample events in an array
        val allEvents = listOf(EventData("Foo"), EventData("Bar"))

        // create a batch
        var eventDataBatch = producer.createBatch()

        for (eventData in allEvents) {
            // try to add the event from the array to the batch
            if (!eventDataBatch.tryAdd(eventData)) {
                // if the batch is full, send it and then create a new batch
                producer.send(eventDataBatch)
                eventDataBatch = producer.createBatch()

                // Try to add that event that couldn't fit before.
                require(eventDataBatch.tryAdd(eventData)) {
                    ("Event is too large for an empty batch. Max size: "
                            + eventDataBatch.maxSizeInBytes)
                }
            }
        }
        // send the last batch of remaining events
        if (eventDataBatch.count > 0) {
            producer.send(eventDataBatch)
        }
        producer.close()
    }
}