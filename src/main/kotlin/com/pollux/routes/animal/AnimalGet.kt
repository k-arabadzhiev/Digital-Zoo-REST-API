package com.pollux.routes.animal

import com.pollux.models.AnimalResponse
import com.pollux.repository.Repository
import com.pollux.utils.C
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.html.*
import java.io.File

fun Route.getAllAnimals(db: Repository) {
    get("/animals/all") {
        val animalsList = db.getAllAnimals()
        //print("\n\n${animalsList}\n\n")
        if (animalsList.isEmpty()) {
            val animalResponse =
                AnimalResponse(
                    HttpStatusCode.NotFound.toString(),
                    animalsList.requireNoNulls()
                )
            call.respond(animalResponse)
        } else {
            val animalResponse =
                AnimalResponse(
                    HttpStatusCode.OK.toString(),
                    animalsList.requireNoNulls()
                )
            call.respond(animalResponse)
        }

        /*
        //debug
        call.respondHtml {
            head {
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
            }
            body {
                h1 { +"GET - /animals" }
                table {
                    for (animal in animalsList) {
                        tr {
                            td {
                                h1 {
                                    +animal!!.animalName
                                }
                            }
                        }
                        tr {
                            td {
                                animal!!.apply {
                                    img {
                                        src = animalPhoto.toString()
                                        height = "200"
                                    }
                                    br
                                    b { +"age: " }; +age; br
                                    b { +"information: " }; +info; br
                                    b { +"diet: " }; +diet; br
                                    b { +"food: " }; +food; br
                                    b { +"weight: " }; +weight; br
                                    b { +"species: " }; +species; br
                                }
                            }
                        }
                    }
                }
            }
        }*/
    }

    get("/animals/photo/{photoPath...}") {
        val photoPath = call.parameters.getAll("photoPath")
        val filePath = C.BASE_URI + photoPath?.joinToString("\\")

        val file = File(filePath)
        call.respondFile(file)
    }

   /* get("/styles.css") {
        call.respondCss {
            table {
                tableLayout = TableLayout.fixed
                width = LinearDimension("300px")
            }
        }
    }*/

}

/*suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}*/
