package com.flagsmith.kotlin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class FlagsmithConfigTest {

    @Test
    fun testBasicClientConfig() {
        val config = FlagsmithConfig.Builder().build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
    }

    @Test
    fun testBaseUri() {
        val baseUrl = "http://bad-uri"
        val config = FlagsmithConfig.Builder().baseUri(baseUrl).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertEquals(client.client.flagsmithSdk.config.baseUri.host, "bad-uri")
    }

    @Test
    fun testWriteTimeoutMillis() {
        val timeout = 10
        val config = FlagsmithConfig.Builder().writeTimeout(timeout).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertEquals(client.client.flagsmithSdk.config.httpClient.writeTimeoutMillis, timeout)
    }

    @Test
    fun testReadTimeoutMillis() {
        val timeout = 10
        val config = FlagsmithConfig.Builder().readTimeout(timeout).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertEquals(client.client.flagsmithSdk.config.httpClient.readTimeoutMillis, timeout)
    }

    @Test
    fun testConnectTimeoutMillis() {
        val timeout = 10
        val config = FlagsmithConfig.Builder().connectTimeout(timeout).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertEquals(client.client.flagsmithSdk.config.httpClient.connectTimeoutMillis, timeout)
    }

    @Test
    fun testDisableAnalyticsProcessor() {
        val config = FlagsmithConfig.Builder().enableAnalytics(false).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertNull(client.client.flagsmithSdk.config.analyticsProcessor)
    }

    @Test
    fun testEnableAnalyticsProcessor() {
        val config = FlagsmithConfig.Builder().enableAnalytics(true).build()
        val client = FlagsmithClient.Builder().configuration(config).build()
        assertNotNull(client)
        assertNotNull(client.client.flagsmithSdk.config.analyticsProcessor)
    }
}
