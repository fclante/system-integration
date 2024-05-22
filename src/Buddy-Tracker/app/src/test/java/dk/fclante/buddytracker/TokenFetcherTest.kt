import dk.fclante.buddytracker.TokenFetcher
import org.json.JSONObject
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TokenFetcherTest {

    @Test
    fun fetchSasToken() {
        val mockJson = mock(JSONObject::class.java)
        `when`(mockJson.getString("token")).thenReturn("mockToken")

        val token = TokenFetcher.fetchSasToken(mockJson)
        assertNotNull(token)
    }
}