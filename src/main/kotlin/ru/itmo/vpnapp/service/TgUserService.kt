package ru.itmo.vpnapp.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.itmo.vpnapp.model.TgUser
import ru.itmo.vpnapp.repo.TgUserRepository
import kotlin.jvm.optionals.getOrElse

@Service
class TgUserService(
    val tgUserRepository: TgUserRepository
) {
    fun getUser(id: Int): ResponseEntity<TgUser> {
        val tgUser = tgUserRepository.findById(id).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tg User is not found")
        }

        return ResponseEntity.ok(tgUser)
    }

    fun createUser(user: TgUser): ResponseEntity<TgUser> {
        tgUserRepository.save(user)
        return ResponseEntity(user, HttpStatus.CREATED)
    }
}