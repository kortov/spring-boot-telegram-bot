package com.kortov.bootigram.bots

import com.fasterxml.jackson.databind.ObjectMapper
import com.kortov.bootigram.config.TelegramProperties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.telegram.telegrambots.meta.api.objects.Update


@ExtendWith(SpringExtension::class)
@WebMvcTest(HelloController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class HelloControllerTest {

    @MockBean
    lateinit var helloBot: HelloBot

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun fooControllerTest() {
        mvc.perform(get("/foo")
                .contentType("application/json"))
                .andExpect(status().isOk)
    }

    @Test
    fun postUpdate() {
        val update = Update()
        mvc.perform(post(TelegramProperties.WEB_HOOK)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(update)!!)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
    }

    private fun asJsonString(obj: Any?): String? {
        return try {
            val mapper = ObjectMapper()
            mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}