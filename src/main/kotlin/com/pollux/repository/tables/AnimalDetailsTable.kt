package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table

object AnimalDetailsTable : Table("animaldetails") {
    val detailsId = integer("id").autoIncrement()
    val info = text("info")
    val age = varchar("age", 50)
    val weight = varchar("weight", 50)
    val food = varchar("food", 100)
    val dietId = reference("diet_id", DietTable.dietId)
    val habitatId = reference("habitat_id", HabitatTable.habitatId)
    override val primaryKey = PrimaryKey(detailsId, name = "PK_ANIMAL_DETAILS")
}