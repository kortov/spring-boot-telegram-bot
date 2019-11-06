package com.kortov.bootigram.integration

import com.kortov.bootigram.bots.HelloBot
import com.kortov.bootigram.bots.HelloController
import com.kortov.bootigram.config.TelegramBotConfig
import com.kortov.bootigram.config.TelegramProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.telegram.telegrambots.meta.api.objects.Update


@ExtendWith(SpringExtension::class)
@WebFluxTest(HelloController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class HelloControllerTest {

    @MockBean
    lateinit var helloBot: HelloBot

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var config: TelegramBotConfig

    @Test
    fun fooControllerTest() {
        client.get().uri("/foo").exchange()
                .expectStatus().isOk
                .expectBody<String>().isEqualTo("foo")
    }

    @Test
    fun postUpdate() {
        val update = Update()
        client
                .mutateWith(csrf())
                .post().uri(TelegramProperties.WEB_HOOK)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .exchange()
                .expectStatus().isOk
    }
}