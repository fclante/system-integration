package dk.fclante.buddytracker.ui.layouts

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dk.fclante.buddytracker.ui.models.EventMessage

@Composable
fun <T> EventListLayout(events: List<EventMessage<T>>) {
    LazyColumn {
        items(events) { event ->
            EventMessageCard(event, modifier = Modifier.padding(8.dp))
        }
    }

}




