package ru.itmo.vpnapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@SpringBootApplication
class VpnAppApplication

fun main(args: Array<String>) {
    runApplication<VpnAppApplication>(*args)
}