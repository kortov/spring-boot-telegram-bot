package com.kortov.bootigram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BootigramApplication

fun main(args: Array<String>) {
    runApplication<BootigramApplication>(*args)
}
