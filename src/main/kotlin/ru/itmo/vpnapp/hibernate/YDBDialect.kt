package ru.itmo.vpnapp.hibernate

import org.hibernate.dialect.PostgreSQLDialect

class YDBDialect : PostgreSQLDialect() {
    override fun getQuerySequencesString(): String? {
        // Takes care of ERROR: relation “information_schema.sequences” does not exist
        return null
    }
}