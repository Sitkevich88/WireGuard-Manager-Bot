package ru.itmo.vpnapp.service

import com.jcraft.jsch.Channel
import com.jcraft.jsch.JSch
import org.springframework.stereotype.Service
import ru.itmo.vpnapp.model.Server

@Service
class SshService {
    fun connect(server: Server): Channel {
        val jsch = JSch()
        val session = jsch.getSession(server.login, server.host, server.port)
        session.setPassword(server.password)
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()
        val channel = session.openChannel("shell")
        channel.inputStream = System.`in`
        channel.outputStream = System.out
        channel.connect()
        
        return channel
    }
    
    fun canConnect(server: Server): Boolean {
        return try {
            connect(server).disconnect()
            true
        } catch (e: Exception) {
            false
        }
    }
}