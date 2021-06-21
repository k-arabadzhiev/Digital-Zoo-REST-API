package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table

object AnimalTable : Table("animal") {
    val animalId = integer("id").autoIncrement()
    val animalName = varchar("name", 50)
    val animalPhoto = varchar("photo", 500)
    val zookeeperId = reference("zookeeper_id", ZookeeperTable.zookeeperId)
    val speciesId = reference("species_id", SpeciesTable.speciesId)
    val animalDetailsId = reference("details_id", AnimalDetailsTable.detailsId)
    override val primaryKey = PrimaryKey(animalId, name = "PK_ANIMAL")

    init {
        uniqueIndex("ANIMAL_NAME", animalName)
    }
}