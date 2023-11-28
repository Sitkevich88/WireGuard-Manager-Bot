package ru.itmo.vpnapp.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.itmo.vpnapp.model.TgChat
import ru.itmo.vpnapp.repo.TgChatRepository


@Service
class TgMessageHandler(
    val tgChatRepository: TgChatRepository
) {
    companion object {}

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

    fun addServer(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    fun listServerUsers(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    fun addVPNUser(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    fun removeVPNUser(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

    fun removeServer(update: Update): SendMessage {
        return SendMessage(update.message.chatId.toString(), "TODO")
    }

}