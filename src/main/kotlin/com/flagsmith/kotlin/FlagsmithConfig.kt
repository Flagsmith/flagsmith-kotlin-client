package com.flagsmith.kotlin

import com.flagsmith.config.Retry
import com.flagsmith.threads.AnalyticsProcessor
import okhttp3.Interceptor
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import com.flagsmith.config.FlagsmithConfig as FlagsmithJavaConfig

class FlagsmithConfig private constructor(
    val config: FlagsmithJavaConfig
) {
    data class Builder(
        var configBuilder: FlagsmithJavaConfig.Builder? = null
    ) {
        init {
            configBuilder = FlagsmithJavaConfig.newBuilder()
        }

        fun baseUri(baseUri: String) = apply {
            configBuilder!!.baseUri(baseUri)
        }

        fun connectTimeout(connectTimeoutMillis: Int) = apply {
            configBuilder!!.connectTimeout(connectTimeoutMillis)
        }

        fun writeTimeout(writeTimeoutMillis: Int) = apply {
            configBuilder!!.writeTimeout(writeTimeoutMillis)
        }

        fun readTimeout(readTimeoutMillis: Int) = apply {
            configBuilder!!.readTimeout(readTimeoutMillis)
        }

        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory, trustManager: X509TrustManager) = apply {
            configBuilder!!.sslSocketFactory(sslSocketFactory, trustManager)
        }

        fun addHttpInterceptor(interceptor: Interceptor) = apply {
            configBuilder!!.addHttpInterceptor(interceptor)
        }

        fun retry(retry: Retry) = apply {
            configBuilder!!.retries(retry)
        }

        fun withLocalEvaluation(localEvaluation: Boolean) = apply {
            configBuilder!!.withLocalEvaluation(localEvaluation)
        }

        fun environmentRefreshIntervalSeconds(seconds: Int) = apply {
            configBuilder!!.withEnvironmentRefreshIntervalSeconds(seconds)
        }

        fun analyticsProcessor(processor: AnalyticsProcessor) = apply {
            configBuilder!!.withAnalyticsProcessor(processor)
        }

        fun enableAnalytics(enable: Boolean) = apply {
            configBuilder!!.withEnableAnalytics(enable)
        }

        fun build() = FlagsmithConfig(configBuilder!!.build())
    }
}