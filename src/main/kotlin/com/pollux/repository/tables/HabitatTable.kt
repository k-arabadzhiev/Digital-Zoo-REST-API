package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table

object HabitatTable : Table("habitat") {
    val habitatId = integer("id").autoIncrement()
    val name = varchar("name", 50).uniqueIndex("HABITAT_NAME")
    override val primaryKey = PrimaryKey(habitatId, name = "PK_HABITAT")
}