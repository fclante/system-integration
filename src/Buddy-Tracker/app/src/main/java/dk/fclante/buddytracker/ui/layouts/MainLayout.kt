package dk.fclante.buddytracker.ui.layouts

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.fclante.buddytracker.services.azure.EventhubService
import dk.fclante.buddytracker.services.azure.ReadEventhubService
import dk.fclante.buddytracker.services.location.LocationService
import dk.fclante.buddytracker.ui.models.EmptyMessage
import dk.fclante.buddytracker.ui.models.EventMessage
import dk.fclante.buddytracker.ui.models.LocationEventMessage
import dk.fclante.buddytracker.ui.theme.BuddytrackerTheme
import java.util.Collections


@Composable
fun MainLayout(
    modifier: Modifier = Modifier,
    locationService: LocationService,
    eventhubService: EventhubService,
    readEventhubService: ReadEventhubService
) {
    val deviceEventMessages = Collections.synchronizedList(mutableListOf<EventMessage<Any>>())
    val AzureEventMessages = Collections.synchronizedList(mutableListOf<EventMessage<Any>>())
    val layoutState = remember { mutableStateOf("default") }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                layoutState.value = "coarseLocationLayout"
                onReadCoarseLocationClicked(locationService)
            }) {
                Text("Read Coarse Location")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                layoutState.value = "fineLocationLayout"
                onReadFineLocationClicked(locationService)
            }) {
                Text("Read Fine Location")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                layoutState.value = "default"
                onStopLocationTrackingClicked(locationService)
            }) {
                Text("Stop Location Tracking")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                layoutState.value = "SendToEventhubLayout"
                onSendToEventhubClicked(
                    eventhubService,
                    deviceEventMessages
                )
            }) {
                Text("Send To Eventhub")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                layoutState.value = "ReadFromEventhubLayout"
                onReadFromEventhubClicked(
                    readEventhubService
                )
            }) {
                Text("Read from Eventhub")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Left) {
            /*  EventListLayout(
                  events = generateRandomEventMessages()
              )*/

            when (layoutState.value) {
                "coarseLocationLayout" -> EventListLayout(events = deviceEventMessages)
                "fineLocationLayout" -> EventListLayout(events = deviceEventMessages)
                "ReadFromEventhubLayout" -> EventListLayout(events = AzureEventMessages)
                "SendToEventhubLayout" -> EventListLayout(events = deviceEventMessages)
                else -> EventListLayout(
                    events = listOf(
                        EmptyMessage(
                            "123",
                            "Title",
                            "Description",
                            "",
                            "2021-10-10",
                            "10:10"
                        )
                    )
                )
            }
            // Can  i switch the layout here?


        }
    }
}


fun generateRandomEventMessages(): List<LocationEventMessage> {
    return List(10) { index ->
        LocationEventMessage(
            guid = "123$index",
            title = "Title$index",
            description = "Description$index",
            payload = Location("provider").apply {
                latitude = 55.0 + index
                longitude = 12.0 + index
            },
            date = "2021-10-${10 + index}",
            time = "${10 + index}:10"
        )
    }
}

fun onReadCoarseLocationClicked(locationService: LocationService) {
    locationService.startCoarseLocationUpdates { location ->
        Log.d("Location", "Coarse location: $location")
    }
}

fun onReadFineLocationClicked(locationService: LocationService) {
    locationService.startFineLocationUpdates { location ->
        Log.d("Location", "Fine location: $location")
    }
}

fun onStopLocationTrackingClicked(locationService: LocationService) {
    locationService.stopLocationUpdates()
}

fun <T> onSendToEventhubClicked(
    eventhubService: EventhubService,
    eventMessages: List<EventMessage<T>>
) {
    eventhubService.publishEvents(eventMessages)
}

fun onReadFromEventhubClicked(
    eventhubService: ReadEventhubService
) {
    eventhubService.configure()
    eventhubService.startReading()
}

@Preview
@Composable
fun MainLayoutPreview() {
    BuddytrackerTheme {
        // Create a mock LocationService
        val mockLocationService = object : LocationService(null) {
            override fun startCoarseLocationUpdates(callback: (Location) -> Unit) {
                // Mock implementation
            }

            override fun startFineLocationUpdates(callback: (Location) -> Unit) {
                // Mock implementation
            }
        }

        // Mock EventhubSender
        val mockEventhubService = object : EventhubService() {
            override fun <T> publishEvents(eventMessages: List<EventMessage<T>>) {
                // Mock implementation
            }
        }

        // Mock ReadEventhubService
        val mockReadEventhubService = object : ReadEventhubService() {
            override fun configure() {
                // Mock implementation
            }

            override fun startReading() {
                // Mock implementation
            }
        }

        MainLayout(
            locationService = mockLocationService,
            eventhubService = mockEventhubService,
            readEventhubService = mockReadEventhubService
        )
    }
}