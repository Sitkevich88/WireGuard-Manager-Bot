package ru.itmo.vpnapp.model

import jakarta.persistence.*

@Entity
@Table(name = "chat")
class TgChat {
    @Id
    @Column(name = "id")
    var id: Long = 0
    
    @Column(name = "server_id")
    var serverId: String? = null
}