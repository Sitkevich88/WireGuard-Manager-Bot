package ru.itmo.vpnapp.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.vpnapp.model.TgUser
import ru.itmo.vpnapp.service.TgUserService

@RestController
@CrossOrigin("*") //todo remove it
@RequestMapping("bot")
class BotController(
    val tgUserService: TgUserService
) {
    @GetMapping("user/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<TgUser> {
        return tgUserService.getUser(id);
    }

    @PostMapping("user")
    fun createUser(@RequestBody user: TgUser): ResponseEntity<TgUser> {
        return tgUserService.createUser(user);
    }
}