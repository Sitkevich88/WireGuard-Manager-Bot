package ru.itmo.vpnapp.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "server")
class Server {
    @Id
    @Column(name = "id")
    var id: String = UUID.randomUUID().toString()

    @Column(name = "host")
    var host = ""

    @Column(name = "port")
    var port = 22

    @Column(name = "login")
    var login = "user"

    @Column(name = "password")
    var encrypedPassword = ""
    
    fun setEncryptedPassword(password: String) {
        this.encrypedPassword = Base64.getEncoder().encodeToString(password.toByteArray())
    }
    
    fun getDecryptedPassword(): String {
        return String(Base64.getDecoder().decode(encrypedPassword))
    }
}