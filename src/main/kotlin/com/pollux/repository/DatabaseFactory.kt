package com.pollux.repository

import com.pollux.repository.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database.Companion.connect
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    private val config = HikariConfig("/hikari.properties")

    fun initDB() {
        val ds = HikariDataSource(config)

        transaction(connect(ds)) {
            SchemaUtils.create(
                AnimalTable,
                AnimalDetailsTable,
                SpeciesTable,
                ZookeeperTable,
                HabitatTable,
                DietTable
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) {
            addLogger(StdOutSqlLogger)
            block()
        }
}