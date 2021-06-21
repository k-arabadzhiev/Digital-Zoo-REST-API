package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.or

object DietTable : Table("diet") {
    val dietId = integer("id").autoIncrement()
    val dietType = varchar("type", 15).uniqueIndex("DIET")
    override val primaryKey = PrimaryKey(dietId, name = "PK_DIET")

    init {
        dietType.check("DIET_TYPE") {
            (it like "Carnivores") or (it like "Herbivores") or (it like "Omnivores")
        }
    }
}