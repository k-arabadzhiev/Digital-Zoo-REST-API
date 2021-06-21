package com.pollux.models

import kotlinx.serialization.Serializable

@Serializable
data class Animal(
    var id: Int? = null,
    val animalName: String,
    val animalNameOld: String? = null,
    var animalPhoto: String? = null,
    val zookeeper: String? = null,
    val species: String,
    //from details
    val info: String,
    val age: String,
    val weight: String,
    val food: String,
    val diet: String,
    val habitat: String
)

@Serializable
data class AnimalResponse(
    val status: String,
    val animals: List<Animal>
)
