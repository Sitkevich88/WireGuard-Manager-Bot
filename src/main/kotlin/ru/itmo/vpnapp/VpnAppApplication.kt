package ru.itmo.vpnapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VpnAppApplication

fun main(args: Array<String>) {
    runApplication<VpnAppApplication>(*args)
}