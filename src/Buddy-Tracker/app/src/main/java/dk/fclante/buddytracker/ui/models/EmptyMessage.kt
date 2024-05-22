package dk.fclante.buddytracker.ui.models

import android.location.Location

class EmptyMessage(
    guid: String,
    title: String,
    description: String,
    payload: String,
    date: String,
    time: String,
) : EventMessage<String>(guid, title, description, payload, date, time)