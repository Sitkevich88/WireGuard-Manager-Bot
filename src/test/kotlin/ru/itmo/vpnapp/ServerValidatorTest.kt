package ru.itmo.vpnapp

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import ru.itmo.vpnapp.validator.ServerValidationException
import ru.itmo.vpnapp.validator.ServerValidator
import org.junit.jupiter.api.Test


class ServerValidatorTest {
    private lateinit var serverValidator: ServerValidator
    
    @BeforeEach
    fun setUp() {
        serverValidator = ServerValidator()
    }

    @Test
    fun notEnoughArguments() {
        val args = listOf("1", "2")
        val exception = assertThrows<ServerValidationException> { 
            serverValidator.getServerFromArgs(args)
        }
        assert(exception.message == "Неверное количество аргументов")
    }

    @Test
    fun localhost() {
        val args = listOf("localhost", "22", "login", "password")
        val exception = assertThrows<ServerValidationException> {
            serverValidator.getServerFromArgs(args)
        }
        assert(exception.message == "Запрещенный хост")
    }

    @Test
    fun invalidPort() {
        val args = listOf("192.169.0.1", "9999999999999999", "login", "password")
        val exception = assertThrows<ServerValidationException> {
            serverValidator.getServerFromArgs(args)
        }
        assert(exception.message == "Неверный формат порта")
    }
    
    @Test
    fun invalidLogin() {
        val args = listOf("192.168.0.1", "22", "", "password")
        val exception = assertThrows<ServerValidationException> {
            serverValidator.getServerFromArgs(args)
        }
        assert(exception.message == "Неверный формат логина")
    }

    @Test
    fun invalidPassword() {
        val args = listOf("192.168.0.1", "22", "login", "")
        val exception = assertThrows<ServerValidationException> {
            serverValidator.getServerFromArgs(args)
        }
        assert(exception.message == "Неверный формат пароля")
    }
    
    @Test
    fun validArguments() {
        val args = listOf("192.168.0.1", "22", "login", "password")
        val server = serverValidator.getServerFromArgs(args)
        assert(server.host == args[0])
        assert(server.port == args[1].toInt())
        assert(server.login == args[2])
        assert(server.encrypedPassword != args[3])
    }
}