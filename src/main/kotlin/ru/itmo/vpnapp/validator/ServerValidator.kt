package ru.itmo.vpnapp.validator

import org.springframework.stereotype.Service
import ru.itmo.vpnapp.model.Server

@Service
class ServerValidator {
    fun getServerFromArgs(args: List<String>): Server {
        if (args.size != 4) {
            throw ServerValidationException("Неверное количество аргументов")
        }

        val serverHost = args[0]
        if (serverHost.isBlank()) {
            throw ServerValidationException("Неверный формат хоста")
        } else if (serverHost in listOf("localhost", "127.0.0.1")) {
            throw ServerValidationException("Запрещенный хост")
        }

        val serverPort = try {
            args[1].toInt()
        } catch (e: NumberFormatException) {
            throw ServerValidationException("Неверный формат порта")
        }
        if (serverPort !in 1..65535) {
            throw ServerValidationException("Неверный формат порта")
        }

        val login = args[2]
        if (login.isBlank()) {
            throw ServerValidationException("Неверный формат логина")
        }

        val password = args[3]
        if (password.isBlank()) {
            throw ServerValidationException("Неверный формат пароля")
        }

        return Server().apply {
            this.host = serverHost
            this.port = serverPort
            this.login = login
            setEncryptedPassword(password)
        }
    }
}

class ServerValidationException(message: String) : Exception(message)