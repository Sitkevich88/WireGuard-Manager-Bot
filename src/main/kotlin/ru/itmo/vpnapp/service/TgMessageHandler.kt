package ru.itmo.vpnapp.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.itmo.vpnapp.model.Server
import ru.itmo.vpnapp.model.TgChat
import ru.itmo.vpnapp.repo.TgChatRepository
import kotlin.jvm.optionals.getOrElse


@Service
class TgMessageHandler(
    val tgChatRepository: TgChatRepository,
    val sshService: SshService
) {
    private val log = LoggerFactory.getLogger(TgMessageHandler::class.java)

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

        val server = Server().apply {
            this.host = serverHost
            this.port = serverPort
            this.login = login
            this.password = password
        }
        if (!sshService.canConnect(server)) {
            return SendMessage(chatId.toString(), "Не удалось подключиться к серверу")
        }
        try {
            val chat = tgChatRepository.findById(chatId).get()
            chat.serverId = sshService.saveServer(server).id
            tgChatRepository.save(chat)
        } catch (e: Exception) {
            log.error("Failed to save server", e)
            return SendMessage(chatId.toString(), "Не удалось сохранить сервер. Попробуйте еще раз. error: ${e.message}")
        }
        
        return SendMessage(chatId.toString(), "Сервер успешно добавлен")
    }

    /**
     * /list
     */
    fun listServerUsers(update: Update): SendMessage {
        val chatId = update.message.chatId
        val server = getServerByChat(chatId) ?: return SendMessage(chatId.toString(), "Сервер не найден")
        
        val users = try {
            sshService.listUsers(server)
        } catch (e: Exception) {
            log.error("Failed to list users", e)
            return SendMessage(chatId.toString(), "Не удалось получить список пользователей. error: ${e.message}")
        }

        return SendMessage(chatId.toString(), users.joinToString(separator = "\n"))
    }

    /**
     * /add_user <username>
     */
    fun addVPNUser(update: Update): SendMessage {
        val chatId = update.message.chatId
        val server = getServerByChat(chatId) ?: return SendMessage(chatId.toString(), "Сервер не найден")
        val username = update.message.text.substringAfter("/add_user").trim().replace(Regex("[^\\w\\d-]"), "_")
        
        if (username.isBlank()) {
            return SendMessage(chatId.toString(), "Неверный формат имени пользователя")
        }

        val existingUsers = try {
            sshService.listUsers(server)
        } catch (e: Exception) {
            log.error("Failed to list users", e)
            return SendMessage(chatId.toString(), "Не удалось получить список пользователей. error: ${e.message}")
        }
        
        if (existingUsers.contains(username)) {
            return SendMessage(chatId.toString(), "Пользователь уже существует")
        }
        
        val config = try {
            sshService.addUser(server, username)
        } catch (e: Exception) {
            log.error("Failed to add user", e)
            return SendMessage(chatId.toString(), "Не удалось добавить пользователя. error: ${e.message}")
        }
        
        return SendMessage(update.message.chatId.toString(), config)
    }

    /**
     * /remove_user <username>
     */
    fun removeVPNUser(update: Update): SendMessage {
        val chatId = update.message.chatId
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    /**
     * /logout
     */
    fun removeServer(update: Update): SendMessage {
        val chatId = update.message.chatId
        val chat = tgChatRepository.findById(chatId).getOrElse { 
            return SendMessage(chatId.toString(), "Сервер не найден")
        }
        chat.serverId = null
        
        try {
            tgChatRepository.save(chat)
        } catch (e: Exception) {
            log.error("Failed to remove serverId from chat", e)
            return SendMessage(chatId.toString(), "Не удалось забыть сервер. Попробуйте еще раз. error: ${e.message}")
        }
        
        return SendMessage(chatId.toString(), "Сервер успешно забыт. Для добавления нового сервера введите команду /add_server с параметрами сервера")
    }
    
    private fun getServerByChat(chatId: Long): Server? {
        val chat = tgChatRepository.findById(chatId).get()
        
        return chat.serverId?.let { sshService.serverRepository.findById(it).get() }
    }
}