package com.pollux.routes.user

import com.pollux.auth.JwtService
import com.pollux.auth.UserSession
import com.pollux.models.ZookeeperResponse
import com.pollux.repository.Repository
import com.pollux.utils.C
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.addZookeeper(db: Repository, jwtService: JwtService, hashFunction: (String) -> String) {
    post("/zookeeper/new") {
        val signupParameters = call.receiveParameters()
        val password = signupParameters["password"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val username = signupParameters["username"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val firstName = signupParameters["firstname"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val lastName = signupParameters["lastname"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )

        val hash = hashFunction(password)
        try {
            val newZookeeper = db.addZookeeper(username, hash, firstName, lastName)
            newZookeeper?.id?.let {
                call.sessions.set(UserSession(it))
                call.respondText(
                    jwtService.generateToken(newZookeeper),
                    status = HttpStatusCode.Created
                )
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register zookeeper", e)
            call.respond(HttpStatusCode.BadRequest, "Couldn't create a Zookeeper")
        }
    }
}

fun Route.zookeeperLogin(db: Repository, jwtService: JwtService, hashFunction: (String) -> String) {
    post("/zookeeper/login") {
        val signinParameters = call.receiveParameters()
        val password = signinParameters["password"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val username = signinParameters["username"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        val hash = hashFunction(password)

        try {
            val currentZookeeper = db.findZookeeperByUsername(username)
            currentZookeeper?.id?.let {
                if (currentZookeeper.password == hash) {
                    call.sessions.set(UserSession(it))
                    val jwt = jwtService.generateToken(currentZookeeper)
                    val zookeeperResponse = ZookeeperResponse(
                        status = HttpStatusCode.OK.toString(), jwt = jwt, firstName = currentZookeeper
                            .firstName, lastName = currentZookeeper.lastName
                    )
                    call.respond(HttpStatusCode.OK, zookeeperResponse)
                } else {
                    val zookeeperResponse = ZookeeperResponse(HttpStatusCode.Unauthorized.toString(), "Incorrect username or" +
                            " password!")
                    call.respond(zookeeperResponse)
                }
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register zookeeper", e)
            call.respond(HttpStatusCode.BadRequest, "Couldn't find Zookeeper")
        }
    }
}

fun Route.zookeeperLogout(db: Repository) {
    post("/zookeeper/logout") {
        val signinParameters = call.receiveParameters()
        val username = signinParameters["username"]
            ?: return@post call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )

        try {
            val currentZookeeper = db.findZookeeperByUsername(username)
            currentZookeeper?.id?.let {
                call.sessions.clear(call.sessions.findName(UserSession::class))
                call.respond(HttpStatusCode.OK)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to logout zookeeper", e)
            call.respond(HttpStatusCode.BadRequest, "Couldn't find Zookeeper")
        }
    }
}

fun Route.deleteZookeeper(db: Repository) {
    delete("/zookeeper/delete") {
        val signinParameters = call.receiveParameters()
        val username = signinParameters["username"]
            ?: return@delete call.respond(
                HttpStatusCode.Unauthorized, "Missing Fields"
            )
        try {
            val currentZookeeper = db.findZookeeperByUsername(username)
            currentZookeeper?.id?.let {
                db.deleteZookeeper(it)
                call.sessions.clear(call.sessions.findName(UserSession::class))
                call.respond(HttpStatusCode.OK)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to logout zookeeper", e)
            call.respond(HttpStatusCode.BadRequest, "Couldn't find Zookeeper")
        }
    }
}