package com.pollux.repository

import com.pollux.models.Animal
import com.pollux.models.Zookeeper

interface Repository {
    suspend fun addAnimal(
        name: String, photo: String, details: Int,
        species: Int, zookeeper: Int
    )

    suspend fun addAnimalDetails(
        animalAge: String, animalInfo: String, animalWeight: String,
        animalFood: String, diet: Int, habitat: Int
    ): Int

    suspend fun getAllAnimals(): List<Animal?>
    //suspend fun findAnimalById(id: Int): Animal?
    suspend fun findAnimalByName(name: String):  Animal?
    suspend fun findSubphylum(species: String): String

    suspend fun findDietId(diet: String): Int
    suspend fun findHabitatId(habitat: String): Int
    suspend fun insertNewHabitat(habitat: String): Int
    suspend fun findSpecies(species: String): Int

    suspend fun addZookeeper(username: String, hash: String, fname: String, lname: String): Zookeeper?
    suspend fun findZookeeper(userId: Int): Zookeeper?
    suspend fun findZookeeperByUsername(username: String): Zookeeper?
    suspend fun deleteZookeeper(id: Int)
    suspend fun updateAnimal(animalUpdate: Animal, speciesId: Int, regionId: Int, dietId: Int, oldPhoto: String? = null)
    suspend fun deleteAnimal(id: Int)
    suspend fun deleteDetails(id: Int)
}