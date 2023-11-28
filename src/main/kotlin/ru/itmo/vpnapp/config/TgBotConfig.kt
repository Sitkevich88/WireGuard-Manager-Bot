package ru.itmo.vpnapp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.itmo.vpnapp.api.WireGuardBot
import ru.itmo.vpnapp.service.TgMessageHandler


@Configuration
class TgBotConfig(
    private val tgMessageHandler: TgMessageHandler
) {
    @Value("\${tg.bot.token}")
    private lateinit var token: String

    @Value("\${tg.bot.username}")
    private lateinit var username: String

    @Value("\${tg.bot.creatorId}")
    private lateinit var creatorId: String

    @Bean
    fun wireGuardBot() = WireGuardBot(token, username, creatorId.toLong(), tgMessageHandler)
    
    @Bean
    fun telegramBotsApi() : TelegramBotsApi {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(wireGuardBot())
        
        return botsApi
    }
}