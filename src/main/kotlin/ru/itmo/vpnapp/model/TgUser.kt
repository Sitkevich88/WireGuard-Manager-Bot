package ru.itmo.vpnapp.model

import jakarta.persistence.*

@Entity
@Table(name = "tg_user")
class TgUser {
    @Id
    @Column(name = "id")
    var id: Int = 0
    
    @Column(name = "name")
    var name = "user"
    
    @Column(name = "chat_id")
    var chatId = 0
}