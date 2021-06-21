package com.pollux.repository

import com.pollux.models.Animal
import com.pollux.models.Zookeeper
import com.pollux.repository.DatabaseFactory.dbQuery
import com.pollux.repository.tables.*
import com.pollux.repository.tables.ZookeeperTable.userName
import com.pollux.repository.tables.ZookeeperTable.zookeeperId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class ZooRepository : Repository {
    override suspend fun addAnimal(
        name: String, photo: String,
        details: Int, species: Int,
        zookeeper: Int
    ) {
        dbQuery {
            AnimalTable.insert {
                it[animalName] = name
                it[animalPhoto] = photo
                it[animalDetailsId] = details
                it[speciesId] = species
                it[zookeeperId] = zookeeper
            }
        }
    }

    override suspend fun findDietId(diet: String) = dbQuery {
        DietTable.select {
            DietTable.dietType eq diet
        }.map { it[DietTable.dietId] }.single()
    }

    override suspend fun findHabitatId(habitat: String): Int = dbQuery {
        HabitatTable.select {
            HabitatTable.name eq habitat
        }.map { it[HabitatTable.habitatId] }.single()
    }

    override suspend fun insertNewHabitat(habitat: String): Int = dbQuery {
        HabitatTable.insert {
            it[name] = habitat
        } get HabitatTable.habitatId
    }

    override suspend fun findSpecies(species: String): Int = dbQuery {
        SpeciesTable.select {
            SpeciesTable.animalClass eq species
        }.map { it[SpeciesTable.speciesId] }.single()
    }

    override suspend fun addAnimalDetails(
        animalAge: String, animalInfo: String, animalWeight: String,
        animalFood: String, diet: Int, habitat: Int
    ): Int = dbQuery {
        AnimalDetailsTable.insert {
            it[age] = animalAge
            it[info] = animalInfo
            it[weight] = animalWeight
            it[food] = animalFood
            it[dietId] = diet
            it[habitatId] = habitat
        } get AnimalDetailsTable.detailsId
    }

    override suspend fun getAllAnimals(): List<Animal?> = dbQuery {
        (AnimalTable innerJoin AnimalDetailsTable innerJoin
                DietTable innerJoin HabitatTable innerJoin SpeciesTable)
            .select {
                (AnimalTable.speciesId eq SpeciesTable.speciesId) and
                        (AnimalTable.animalDetailsId eq AnimalDetailsTable.detailsId) and
                        (AnimalDetailsTable.dietId eq DietTable.dietId) and
                        (AnimalDetailsTable.habitatId eq HabitatTable.habitatId)
            }.map {
                rowToAnimal(it)
            }
    }

    override suspend fun findSubphylum(species: String) = dbQuery {
        SpeciesTable.select {
            SpeciesTable.animalClass eq species
        }.map { it[SpeciesTable.subphylum] }.single()
    }
    /*override suspend fun findAnimalById(id: Int): Animal? = dbQuery {
        (AnimalTable innerJoin AnimalDetailsTable innerJoin
                DietTable innerJoin HabitatTable innerJoin SpeciesTable)
            .select {
                (AnimalTable.animalId.eq(id)) and
                        (AnimalTable.speciesId eq SpeciesTable.speciesId) and
                        (AnimalTable.animalDetailsId eq AnimalDetailsTable.detailsId) and
                        (AnimalDetailsTable.dietId eq DietTable.dietId) and
                        (AnimalDetailsTable.habitatId eq HabitatTable.habitatId)
            }.map {
                rowToAnimal(it)
            }.single()
    }*/

    override suspend fun findAnimalByName(name: String): Animal? = dbQuery {
        (AnimalTable innerJoin AnimalDetailsTable innerJoin
                DietTable innerJoin HabitatTable innerJoin SpeciesTable)
            .select {
                (AnimalTable.animalName like "$name") and
                        (AnimalTable.speciesId eq SpeciesTable.speciesId) and
                        (AnimalTable.animalDetailsId eq AnimalDetailsTable.detailsId) and
                        (AnimalDetailsTable.dietId eq DietTable.dietId) and
                        (AnimalDetailsTable.habitatId eq HabitatTable.habitatId)
            }.map {
                rowToAnimal(it)
            }.single()
    }

    override suspend fun addZookeeper(username: String, hash: String, fname: String, lname: String): Zookeeper? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = ZookeeperTable.insert {
                it[userName] = username
                it[password] = hash
                it[firstName] = fname
                it[lastName] = lname
            }
        }
        return rowToZookeeper(statement?.resultedValues?.get(0))
    }

    override suspend fun findZookeeper(userId: Int): Zookeeper? = dbQuery {
        ZookeeperTable.select { zookeeperId eq userId }
            .map { rowToZookeeper(it) }.single()
    }

    override suspend fun findZookeeperByUsername(username: String): Zookeeper? = dbQuery {
        ZookeeperTable.select { userName like username }
            .map { rowToZookeeper(it) }.single()
    }

    override suspend fun deleteZookeeper(id: Int) {
        dbQuery {
            ZookeeperTable.deleteWhere { zookeeperId eq id }
        }
    }

    override suspend fun updateAnimal(
        animalUpdate: Animal, speciesId: Int,
        regionId: Int, dietId: Int, oldPhoto: String?
    ) {
        dbQuery {
            (AnimalTable innerJoin AnimalDetailsTable innerJoin
                    DietTable innerJoin HabitatTable innerJoin SpeciesTable)
                .update({
                    (AnimalTable.animalId eq animalUpdate.id!!) and
                            (AnimalTable.speciesId eq SpeciesTable.speciesId) and
                            (AnimalTable.animalDetailsId eq AnimalDetailsTable.detailsId) and
                            (AnimalDetailsTable.dietId eq DietTable.dietId) and
                            (AnimalDetailsTable.habitatId eq HabitatTable.habitatId)
                }) {
                    it[AnimalTable.animalName] = animalUpdate.animalName
                    if (oldPhoto == null)
                        it[AnimalTable.animalPhoto] = animalUpdate.animalPhoto!!
                    else
                        it[AnimalTable.animalPhoto] = oldPhoto
                    it[AnimalTable.speciesId] = speciesId
                    it[AnimalDetailsTable.dietId] = dietId
                    it[AnimalDetailsTable.info] = animalUpdate.info
                    it[AnimalDetailsTable.age] = animalUpdate.age
                    it[AnimalDetailsTable.weight] = animalUpdate.weight
                    it[AnimalDetailsTable.food] = animalUpdate.food
                    it[AnimalDetailsTable.habitatId] = regionId
                }
        }
    }

    override suspend fun deleteAnimal(id: Int) {
        dbQuery {
            AnimalTable.deleteWhere {
                AnimalTable.animalId eq id
            }
        }
    }

    override suspend fun deleteDetails(id: Int) {
        dbQuery {
            AnimalDetailsTable.deleteWhere {
                AnimalDetailsTable.detailsId eq id
            }
        }
    }

    private fun rowToAnimal(row: ResultRow?): Animal? {
        if (row == null) return null
        return Animal(
            id = row[AnimalTable.animalId],
            animalName = row[AnimalTable.animalName],
            animalPhoto = row[AnimalTable.animalPhoto],
            zookeeper = null,
            species = row[SpeciesTable.animalClass],
            info = row[AnimalDetailsTable.info],
            age = row[AnimalDetailsTable.age],
            weight = row[AnimalDetailsTable.weight],
            food = row[AnimalDetailsTable.food],
            diet = row[DietTable.dietType],
            habitat = row[HabitatTable.name]
        )
    }

    private fun rowToZookeeper(row: ResultRow?): Zookeeper? {
        if (row == null) return null
        return Zookeeper(
            id = row[ZookeeperTable.zookeeperId],
            firstName = row[ZookeeperTable.firstName],
            lastName = row[ZookeeperTable.lastName],
            username = row[ZookeeperTable.userName],
            password = row[ZookeeperTable.password],
        )
    }
}