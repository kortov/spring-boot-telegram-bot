package com.kortov.bootygram.config

import com.kortov.bootygram.bots.HelloBot
import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.net.Authenticator
import java.net.PasswordAuthentication

@Configuration
@ConditionalOnClass(TelegramBotsApi::class)
@EnableConfigurationProperties(TelegramProperties::class)
class TelegramBotConfig {

    @Bean
    @Throws(TelegramApiRequestException::class)
    fun helloBot(properties: TelegramProperties): HelloBot {
        val myAmazingBot = HelloBot(properties.botToken ?: "",
                properties.botUsername, TelegramProperties.WEB_HOOK, botOptions(properties), properties)
        myAmazingBot.setWebhook(properties.externalUrl + myAmazingBot.botPath, null)
        return myAmazingBot
    }

    @Bean
    fun botOptions(properties: TelegramProperties): DefaultBotOptions {
        val botOptions = DefaultBotOptions()
        Authenticator.setDefault(object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(properties.proxyUser, properties.proxyPassword?.toCharArray())
            }
        })
        botOptions.proxyHost = properties.proxyHost
        botOptions.proxyPort = properties.proxyPort ?: -1
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.proxyType = DefaultBotOptions.ProxyType.SOCKS5
        return botOptions
    }


    companion object : KLogging() {
    }


}