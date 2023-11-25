package ru.itmo.vpnapp.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.itmo.vpnapp.model.TgUser

@Repository
interface TgUserRepository : PagingAndSortingRepository<TgUser, Int>, JpaRepository<TgUser, Int>