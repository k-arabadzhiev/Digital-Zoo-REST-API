package com.pollux.routes.animal

import com.pollux.repository.Repository
import io.ktor.application.*
import io.ktor.routing.*

fun Application.animalRoutes(db: Repository){
    routing {
        getAllAnimals(db)

        addAnimal(db)
        updateAnimal(db)
        deleteAnimal(db)
    }
}