package dk.fclante.buddytracker

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object TokenFetcher {
    private const val FUNCTION_URL = "https://schoolsastokengenerator.azurewebsites.net/api/generatesastoken"

    @Throws(IOException::class)
    fun fetchSasToken(): String {
        val client: OkHttpClient = OkHttpClient()

        val request: Request = Request.Builder()
            .url(FUNCTION_URL)
            .build()

        val response: Response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        val responseBody: String = response.body?.string() ?: throw IOException("Response body is null")
        val jsonObject = JSONObject(responseBody)
        return fetchSasToken(jsonObject)
    }

    fun fetchSasToken(jsonObject: JSONObject): String {
        return jsonObject.getString("token")
    }
}