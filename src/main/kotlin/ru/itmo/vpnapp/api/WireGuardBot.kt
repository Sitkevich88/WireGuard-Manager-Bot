package ru.itmo.vpnapp.api

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.itmo.vpnapp.service.TgMessageHandler


class WireGuardBot(
    botToken: String,
    private val tgBotUsername: String,
    private val creatorId: Long,
    private val messageHandler: TgMessageHandler
) : TelegramLongPollingBot(botToken) {
    companion object {
        const val HELP_TEXT = """
/start - начать работу с ботом
/help - помощь
/creator - узнать, кто создатель бота
/add_server host port login password - добавить VPN сервер
/list - список пользователей VPN
/add_user username - добавить пользователя VPN
/remove_user username - удалить пользователя VPN
/remove - забыть сервер
        """
        const val CREATOR_TEXT = "Создатель: @gorkiy_mensch"
    }

    override fun getBotUsername() = tgBotUsername

    override fun onUpdateReceived(update: Update) {
        val chatId = update.message.chatId.toString()
        val message = update.message.text

        val response = when (message) {
            "/start" -> {
                messageHandler.start(update)
            }

            "/help" -> {
                SendMessage(chatId, HELP_TEXT)
            }

            "/creator" -> {
                SendMessage(chatId, CREATOR_TEXT)
            }

            else -> {
                if (message.startsWith("/add_server")) {
                    messageHandler.addServer(update)
                } else if (message.startsWith("/list")) {
                    messageHandler.listServerUsers(update)
                } else if (message.startsWith("/add_user")) {
                    messageHandler.addVPNUser(update)
                } else if (message.startsWith("/remove_user")) {
                    messageHandler.removeVPNUser(update)
                } else if (message.startsWith("/remove")) {
                    messageHandler.removeServer(update)
                } else {
                    SendMessage(chatId, "Неизвестная команда.\n\r$HELP_TEXT")
                }
            }
        }
        execute(response)
    }
}