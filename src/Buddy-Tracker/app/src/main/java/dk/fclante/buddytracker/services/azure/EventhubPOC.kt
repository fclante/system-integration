package dk.fclante.buddytracker.services.azure

import dk.fclante.buddytracker.TokenFetcher.fetchSasToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class EventhubPOC {

    @Throws(IOException::class)
    fun sendEvent(eventData: String) {
        val sasToken: String = fetchSasToken()
        val eventHubUrl = "https://frey-school.servicebus.windows.net/tracker/messages"

        val client = OkHttpClient()
        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val body = "{\"body\":\"$eventData\"}".toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url(eventHubUrl)
            .post(body)
            .addHeader("Authorization", sasToken)
            .addHeader("Content-Type", "application/json")
            .build()

        val response: Response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
    }

}