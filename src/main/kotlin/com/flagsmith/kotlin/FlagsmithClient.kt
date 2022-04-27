package com.flagsmith.kotlin

import com.flagsmith.FlagsmithLoggerLevel
import com.flagsmith.config.FlagsmithCacheConfig
import com.flagsmith.config.FlagsmithConfig
import com.flagsmith.models.BaseFlag
import com.flagsmith.threads.PollingManager
import java.util.function.Function
import com.flagsmith.FlagsmithClient as FlagsmithJavaClient

class FlagsmithClient private constructor(
    val client: FlagsmithJavaClient
) {
    fun updateEnvironment() = client.updateEnvironment()

    fun getEnvironmentFlags() = client.environmentFlags

    fun getIdentityFlags(identifier: String, traits: HashMap<String, Any>?) =
        client.getIdentityFlags(identifier, traits)

    data class Builder(
        var clientBuilder: FlagsmithJavaClient.Builder? = null
    ) {
        init {
            clientBuilder = FlagsmithJavaClient.newBuilder()
        }

        fun apiKey(apiKey: String) = apply {
            this.clientBuilder!!.setApiKey(apiKey)
        }

        fun defaultFlagHandler(handler: Function<String, BaseFlag>) = apply {
            this.clientBuilder!!.setDefaultFlagValueFunction(handler)
        }

        fun enableLogging() = apply {
            this.clientBuilder!!.enableLogging()
        }

        fun enableLogging(level: FlagsmithLoggerLevel) = apply {
            this.clientBuilder!!.enableLogging(level)
        }

        fun configuration(config: FlagsmithConfig) = apply {
            this.clientBuilder!!.withConfiguration(config)
        }

        fun configuration(config: com.flagsmith.kotlin.FlagsmithConfig) = apply {
            this.clientBuilder!!.withConfiguration(config.config)
        }

        fun apiUrl(apiUrl: String) = apply {
            this.clientBuilder!!.withApiUrl(apiUrl)
        }

        fun customHeaders(headers: HashMap<String, String>) = apply {
            this.clientBuilder!!.withCustomHttpHeaders(headers)
        }

        fun cache(cache: FlagsmithCacheConfig) = apply {
            this.clientBuilder!!.withCache(cache)
        }

        fun pollingManager(pollingManager: PollingManager) = apply {
            this.clientBuilder!!.withPollingManager(pollingManager)
        }

        fun build() = FlagsmithClient(clientBuilder!!.build())
    }
}
