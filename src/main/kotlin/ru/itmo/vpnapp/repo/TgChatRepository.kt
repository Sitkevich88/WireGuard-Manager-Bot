package ru.itmo.vpnapp.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.itmo.vpnapp.model.TgChat

@Repository
interface TgChatRepository : PagingAndSortingRepository<TgChat, Long>, JpaRepository<TgChat, Long>