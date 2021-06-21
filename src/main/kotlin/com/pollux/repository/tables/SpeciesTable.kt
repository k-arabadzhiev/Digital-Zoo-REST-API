package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.or

object SpeciesTable : Table("species") {
    val speciesId = integer("id").autoIncrement()
    val subphylum = varchar("subphylum", 25)
    val animalClass = varchar("class", 100).uniqueIndex("CLASS_IDX")
    override val primaryKey = PrimaryKey(speciesId, name = "PK_SPECIES")

    init {
        subphylum.check("SUBPHYLUM") {
            (it like "Vertebrates") or (it like "Invertebrates")
        }
        animalClass.check("CLASS") {
            (it like "Amphibians") or (it like "Birds") or (it like "Fish") or
                    (it like "Mammals") or (it like "Reptiles") or
                    (it like "Arachnids") or (it like "Crustaceans") or (it like "Insects") or
                    (it like "Mollusks") or (it like "Myriapoda") or (it like "Echinoderms")
        }
    }
}