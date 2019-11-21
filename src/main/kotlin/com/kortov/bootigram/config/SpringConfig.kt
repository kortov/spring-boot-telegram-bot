package com.kortov.bootigram.config

import com.beust.klaxon.Klaxon
import com.kortov.bootigram.bots.HelloBot
import mu.KLogging
import org.mapdb.DBMaker
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.abilitybots.api.db.DBContext
import org.telegram.abilitybots.api.db.MapDBContext
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import java.net.Authenticator
import java.net.PasswordAuthentication

@Configuration
@ConditionalOnClass(TelegramBotsApi::class)
@EnableConfigurationProperties(TelegramProperties::class)
class SpringConfig(val properties: TelegramProperties) {

    @Bean(destroyMethod = "close")
    @Throws(TelegramApiRequestException::class)
    fun helloBot(): HelloBot {
        val helloBot = HelloBot(properties, dbForBot(), botOptions())
        helloBot.setWebhook(properties.externalUrl + helloBot.botPath, null)
        return helloBot
    }

    @Bean
    fun dbForBot(): DBContext {
        val db = DBMaker
                .tempFileDB()
//                .fileDB(properties.botUsername)
                .fileMmapEnableIfSupported()
                .closeOnJvmShutdown()
                .transactionEnable()
                .fileDeleteAfterClose()
                .make()

        return MapDBContext(db)
    }

    @Bean
    fun botOptions(): DefaultBotOptions {
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

    @Bean
    fun klakson(): Klaxon {
        return Klaxon()
    }

    companion object : KLogging()


}