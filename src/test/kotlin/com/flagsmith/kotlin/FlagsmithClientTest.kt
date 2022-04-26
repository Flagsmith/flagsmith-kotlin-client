package com.flagsmith.kotlin

import com.flagsmith.FlagsmithApiWrapper
import com.flagsmith.models.BaseFlag
import com.flagsmith.models.DefaultFlag
import com.flagsmith.threads.PollingManager
import kotlin.test.*

internal class FlagsmithClientTest {

    @Test
    fun testBasicClient() {
        val client = FlagsmithClient.Builder().build();
        assertNotNull(client)
    }

    @Test
    fun testIfApiKeyIsSet() {
        val apikey :String = "http://bad-url"
        val client = FlagsmithClient.Builder().apiKey(apikey).build();
        val apiWrapper = client.client.flagsmithSdk as FlagsmithApiWrapper?
        assertNotNull(apiWrapper)
        assertEquals(apiWrapper.apiKey, apikey)
    }

    @Test
    fun testIfApiUrlIsSet() {
        val url :String = "http://bad-url"
        val client = FlagsmithClient.Builder().apiUrl(url).build();
        assertNotNull(client.client.flagsmithSdk.config.baseUri.toUrl())
        assertEquals(client.client.flagsmithSdk.config.baseUri.toUrl().protocol, "http")
        assertEquals(client.client.flagsmithSdk.config.baseUri.toUrl().host, "bad-url")
    }

    @Test
    fun testIfCustomHeaderIsSet() {
        val headers :HashMap<String,String> = HashMap<String,String>()
        headers.put("name","Flagsmith")

        val client = FlagsmithClient.Builder().customHeaders(headers).build();
        val apiWrapper = client.client.flagsmithSdk as FlagsmithApiWrapper?
        assertNotNull(apiWrapper)
        assertEquals(apiWrapper.customHeaders.get("name"), "Flagsmith")
    }

    @Test
    fun testPollingManagerShouldNotBeSet() {
        val pollingManager = PollingManager(null, 10)

        val client = FlagsmithClient.Builder().pollingManager(pollingManager).build();
        assertNull(client.client.pollingManager)
    }

    @Test
    fun testDefaultFlagHandlerIsSet() {
        val client = FlagsmithClient.Builder().defaultFlagHandler(fun(_ :String) :BaseFlag { return DefaultFlag() }).build();
        assertNotNull(client.client.flagsmithSdk.config.flagsmithFlagDefaults)
        assertEquals(client.client.flagsmithSdk.config.flagsmithFlagDefaults.evaluateDefaultFlag("test"), DefaultFlag())
    }
}