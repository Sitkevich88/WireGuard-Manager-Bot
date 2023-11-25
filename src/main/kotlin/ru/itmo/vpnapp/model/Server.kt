package ru.itmo.vpnapp.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "server")
class Server {
    @Id
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "host")
    var host = "192.168.0.1" //todo check on localhost

    @Column(name = "port")
    var port = 22

    @Column(name = "login")
    var login = "user"

    @Column(name = "password")
    var password = "" //todo hide it
}