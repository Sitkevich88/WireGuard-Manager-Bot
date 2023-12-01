package ru.itmo.vpnapp.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.itmo.vpnapp.model.Server
import java.util.Optional

@Repository
interface ServerRepository : PagingAndSortingRepository<Server, Int>, JpaRepository<Server, Int> {
    fun findById(id: String): Optional<Server>
}