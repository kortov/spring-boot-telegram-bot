package com.kortov.bootygram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
class BootygramApplication

fun main(args: Array<String>) {
	runApplication<BootygramApplication>(*args)
}
