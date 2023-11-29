package ru.itmo.vpnapp.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.itmo.vpnapp.model.Server
import ru.itmo.vpnapp.model.TgChat
import ru.itmo.vpnapp.repo.TgChatRepository


@Service
class TgMessageHandler(
    val tgChatRepository: TgChatRepository,
    val sshService: SshService
) {
    /**
     * /start
     */
    fun start(update: Update): SendMessage {
        val tgChat = TgChat().apply {
            id = update.message.chatId
        }
        tgChatRepository.save(tgChat)

        return SendMessage(
            update.message.chatId.toString(), "${update.message.chat.userName}, привет! " +
                    "Я бот для управления WireGuard. " +
                    "Добавь сервер командой /add_server и я смогу им управлять."
        )
    }

    /**
     * /add_server <server_host> <server_port> <login> <password>
     */
    fun addServer(update: Update): SendMessage {
        val message = update.message.text
        val chatId = update.message.chatId
        val args = message.substringAfter("/add_server").trim().split(" ")

        if (args.size != 4) {
            return SendMessage(chatId.toString(), "Неверное количество аргументов")
        }

        val serverHost = args[0]
        if (serverHost.isBlank()) {
            return SendMessage(chatId.toString(), "Неверный формат хоста")
        } else if (serverHost in listOf("localhost", "127.0.0.1")) {
            return SendMessage(chatId.toString(), "Запрещенный хост")
        }

        val serverPort = try {
            args[1].toInt()
        } catch (e: NumberFormatException) {
            return SendMessage(chatId.toString(), "Неверный формат порта")
        }
        if (serverPort !in 1..65535) {
            return SendMessage(chatId.toString(), "Неверный формат порта")
        }

        val login = args[2]
        if (login.isBlank()) {
            return SendMessage(chatId.toString(), "Неверный формат логина")
        }

        val password = args[3]
        if (password.isBlank()) {
            return SendMessage(chatId.toString(), "Неверный формат пароля")
        }

        if (!sshService.canConnect(
                Server().apply {
                    this.host = serverHost
                    this.port = serverPort
                    this.login = login
                    this.password = password
                }
            )
        ) {
            return SendMessage(chatId.toString(), "Не удалось подключиться к серверу")
        }
        
        return SendMessage(chatId.toString(), "Сервер успешно добавлен")
    }

    /**
     * /list
     */
    fun listServerUsers(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    /**
     * /add_user <username>
     */
    fun addVPNUser(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    /**
     * /remove_user <username>
     */
    fun removeVPNUser(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    /**
     * /remove
     */
    fun removeServer(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

}