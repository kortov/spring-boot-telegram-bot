package com.kortov.bootigram.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated

@ConstructorBinding
@ConfigurationProperties("telegram")
@Validated
data class TelegramProperties
(val externalUrl: String,
 var internalUrl: String?,
 val creatorId: Int,
 var keyStore: String?,
 var keyStorePassword: String?,
 var pathToCertificate: String?,
 var proxyHost: String?,
 var proxyPort: Int?,
 var proxyUser: String?,
 var proxyPassword: String?,
 val botToken: String,
 val botUsername: String
) {
    fun hasKeyStoreWithPath(): Boolean {
        return hasUrls() && hasKeyStore() && !StringUtils.isEmpty(this.pathToCertificate)
    }

    fun hasKeyStore(): Boolean {
        return !StringUtils.isEmpty(this.keyStore) && !StringUtils.isEmpty(this.keyStorePassword)
    }

    fun hasUrls(): Boolean {
        return !StringUtils.isEmpty(this.externalUrl) && !StringUtils.isEmpty(this.internalUrl)
    }

    companion object {
        const val WEB_HOOK = "/bothook"
    }
}