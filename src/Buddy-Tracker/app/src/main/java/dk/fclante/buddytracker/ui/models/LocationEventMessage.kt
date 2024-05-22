package dk.fclante.buddytracker.ui.models

import android.location.Location

class LocationEventMessage(
    guid: String,
    title: String,
    description: String,
    payload: Location,
    date: String,
    time: String,
) : EventMessage<Location>(guid, title, description, payload, date, time)