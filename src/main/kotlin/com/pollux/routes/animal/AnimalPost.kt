package com.pollux.routes.animal

import com.pollux.models.Animal
import com.pollux.repository.DatabaseFactory.dbQuery
import com.pollux.repository.Repository
import com.pollux.repository.Response
import com.pollux.repository.tables.*
import com.pollux.utils.C
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import java.io.File

fun Route.addAnimal(db: Repository) {
    authenticate("zookeeper") {
        post("/animal/new") {
            lateinit var newAnimalJson: Animal
            val multipartData = call.receiveMultipart()
            var fileName: String? = null

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        newAnimalJson = Json.decodeFromString(part.value)
                        //println(newAnimalJson)
                    }
                    is PartData.FileItem -> {
                        val species = newAnimalJson.species
                        val subphylum = db.findSubphylum(species)
                        val dir = File(C.BASE_URI + "\\${subphylum}\\${species}\\")
                        if (!dir.isDirectory)
                            dir.mkdirs()

                        fileName = dir.path + "\\${newAnimalJson.animalName}.jpeg"
                        //println(fileName)
                        val fileBytes = part.streamProvider().readBytes()
                        File(fileName!!).writeBytes(fileBytes)
                    }
                    else -> call.respond(HttpStatusCode.BadRequest, "Illegal Animal Data!\n")
                }
            }

            val zookeeperId = db.findZookeeperByUsername(newAnimalJson.zookeeper!!)?.id!!
            val newAnimalSpeciesId = db.findSpecies(newAnimalJson.species)
            val habitatId = try {
                db.findHabitatId(newAnimalJson.habitat)
            } catch (e: Throwable) {
                db.insertNewHabitat(newAnimalJson.habitat)
            }
            print(habitatId)
            val dietTypeId = db.findDietId(newAnimalJson.diet)

            val newAnimalDetailsId = db.addAnimalDetails(
                newAnimalJson.age, newAnimalJson.info,
                newAnimalJson.weight, newAnimalJson.food,
                dietTypeId, habitatId
            )

            try {
                var photoUri = fileName?.replace(C.BASE_URI, "photo\\") ?: C.PLACEHOLDER_PHOTO_PATH
                photoUri = photoUri.replace("\\", "/")
                print(photoUri)
                db.addAnimal(
                    name = newAnimalJson.animalName,
                    photo = photoUri,
                    details = newAnimalDetailsId,
                    species = newAnimalSpeciesId,
                    zookeeper = zookeeperId
                )
            } catch (e: Throwable) {
                val response = Response(
                    HttpStatusCode.BadRequest.toString(),
                    "${newAnimalJson.animalName} not added to database.\n"
                )
                return@post call.respond(response)
            }
            //println(newAnimal)
            val response =
                Response(HttpStatusCode.Accepted.toString(), "${newAnimalJson.animalName} added to database.")
            call.respond(response)
        }
    }
}

fun Route.updateAnimal(db: Repository) {
    authenticate("zookeeper") {
        post("/animal/update") {
            lateinit var animalJson: Animal
            val multipartData = call.receiveMultipart()
            var fileName: String
            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        animalJson = Json.decodeFromString(part.value)
                    }
                    is PartData.FileItem -> {
                        val species = animalJson.species
                        val subphylum = db.findSubphylum(species)
                        val dir = File(C.BASE_URI + "\\${subphylum}\\${species}\\")
                        if (!dir.isDirectory)
                            dir.mkdirs()

                        fileName = dir.path + "\\${animalJson.animalName}.jpeg"
                        val fileBytes = part.streamProvider().readBytes()
                        File(fileName).writeBytes(fileBytes)
                        animalJson.animalPhoto = fileName.replace(C.BASE_URI, "photo\\")
                    }
                    else -> call.respond(HttpStatusCode.BadRequest, "Illegal Animal Data!\n")
                }
            }
            //print(animalJson)
            animalJson.animalNameOld
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing old name")

            try {
                val speciesId = db.findSpecies(animalJson.species)
                val habitatId = try {
                    db.findHabitatId(animalJson.habitat)
                } catch (e: Throwable) {
                    db.insertNewHabitat(animalJson.habitat)
                }
                val dietId = db.findDietId(animalJson.diet)
                val animalId = db.findAnimalByName(animalJson.animalNameOld!!).let {
                    it?.id
                }
                animalJson.id = animalId

                if (animalJson.animalPhoto.isNullOrEmpty()) {
                    val oldPhoto = db.findAnimalByName(animalJson.animalNameOld!!).let {
                        it?.animalPhoto!!
                    }
                    db.updateAnimal(animalJson, speciesId, habitatId, dietId, oldPhoto)
                } else
                    db.updateAnimal(animalJson, speciesId, habitatId, dietId)
                val response =
                    Response(HttpStatusCode.Accepted.toString(), "${animalJson.animalName} updated!\n")
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Throwable) {
                val response = Response(
                    HttpStatusCode.BadRequest.toString(),
                    "${animalJson.animalNameOld} not added found!\n"
                )
                return@post call.respond(response)
            }
        }
    }
}

fun Route.deleteAnimal(db: Repository) {
    authenticate("zookeeper") {
        delete("/animal/delete/{id}") {
            if (call.parameters["id"] != null) {
                val animalId = call.parameters["id"]!!.toInt()
                try {
                    db.deleteAnimal(animalId)
                    db.deleteDetails(animalId)
                    call.respond(HttpStatusCode.OK, "Animal deleted!\n")
                } catch (e: Throwable) {
                    call.respond(HttpStatusCode.NotFound, "Animal not found!\n")
                }
            } else return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
        }
    }
}