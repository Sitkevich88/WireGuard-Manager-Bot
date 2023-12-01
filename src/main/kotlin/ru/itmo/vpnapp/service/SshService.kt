package ru.itmo.vpnapp.service

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import org.springframework.stereotype.Service
import ru.itmo.vpnapp.model.Server
import ru.itmo.vpnapp.repo.ServerRepository
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class SshService(
    val serverRepository: ServerRepository
) {
    private fun createSession(server: Server): Session {
        val jsch = JSch()
        val session = jsch.getSession(server.login, server.host, server.port)
        session.setPassword(server.password)
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()
        return session
    }

    private fun connect(server: Server): Channel {
        val session = createSession(server)
        val channel = session.openChannel("shell")
        channel.inputStream = System.`in`
        channel.outputStream = System.out
        channel.connect()

        return channel
    }

    private fun runCommand(session: Session, command: String): String {
        val channel = session.openChannel("exec") as ChannelExec
        channel.setCommand(command)
        channel.connect()

        // Read the command output
        val inputStream = channel.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = reader.readLine()
        }
        channel.disconnect()

        return stringBuilder.toString()
    }

    fun canConnect(server: Server): Boolean {
        return try {
            connect(server).disconnect()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun listUsers(server: Server): List<String> {
        val session = createSession(server)
        val command = "cat /etc/wireguard/wg0.conf | grep BEGIN_PEER"
        val output = runCommand(session, command)
        session.disconnect()

        return output
            .replace("# BEGIN_PEER ", "")
            .split("\n")
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .sorted()
            .map { "- $it" }
    }

    fun addUser(server: Server, username: String): String {
        val session = createSession(server)
        val command = "wg set wg0 peer ${username} allowed-ips 10.0.0.${username}/32"
        val output = runCommand(session, command)
        session.disconnect()

        return output
    }

    fun saveServer(server: Server) = serverRepository.save(server)
    fun deleteServer(serverId: Int) = serverRepository.deleteById(serverId)
}