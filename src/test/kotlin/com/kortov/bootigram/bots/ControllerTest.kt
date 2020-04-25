package com.kortov.bootigram.bots

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kortov.bootigram.bots.botlogic.Bot
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
@WebMvcTest(Controller::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class ControllerTest {

    @MockBean
    lateinit var bot: Bot

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

    @Throws(JsonProcessingException::class)
    private fun asJsonString(obj: Any?): String? {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(obj)
    }
}