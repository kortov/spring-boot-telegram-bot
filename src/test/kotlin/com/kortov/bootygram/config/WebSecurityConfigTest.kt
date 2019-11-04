package com.kortov.bootygram.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@ExtendWith(SpringExtension::class)
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableConfigurationProperties(TelegramProperties::class)
class WebSecurityConfigTest(val webTestClient: WebTestClient) {

//            @Autowired
//            private val webTestClient: WebTestClient? = null

    @Test
    fun getMovieEvents() {
        val result = webTestClient.get().uri(TelegramProperties.WEB_HOOK)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
    }
}
