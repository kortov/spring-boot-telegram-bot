package com.kortov.bootigram.config

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@ConstructorBinding
@ConfigurationProperties("telegram")
@Validated
data class TelegramProperties
(@URL val externalUrl: String,
 @URL var internalUrl: String?,
 val creatorId: Int,
 var keyStore: String?,
 var keyStorePassword: String?,
 var pathToCertificate: String?,
 @URL var proxyHost: String?,
 var proxyPort: Int?,
 var proxyUser: String?,
 var proxyPassword: String?,
 val botToken: String,
 val botUsername: String
) {

    companion object {
        const val WEB_HOOK = "/bothook"
    }
}