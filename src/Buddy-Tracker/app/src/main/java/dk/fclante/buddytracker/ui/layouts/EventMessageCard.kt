package dk.fclante.buddytracker.ui.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dk.fclante.buddytracker.ui.models.EventMessage


@Composable
fun <T>EventMessageCard(event: EventMessage<T>, modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = event.title,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = event.description,
                color = Color.White,
            )
        }
    }
}
