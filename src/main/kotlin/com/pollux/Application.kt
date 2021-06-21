package com.pollux

import com.pollux.auth.JwtService
import com.pollux.auth.UserSession
import com.pollux.auth.hash
import com.pollux.repository.DatabaseFactory.initDB
import com.pollux.repository.ZooRepository
import com.pollux.routes.animal.animalRoutes
import com.pollux.routes.user.userRoutes
import com.pollux.utils.C
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }
    install(Sessions) {
        cookie<UserSession>("Zookeeper") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    initDB()
    val db = ZooRepository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication) {
        jwt("zookeeper") {
            verifier(jwtService.verifier)
            realm = C.SERVER
            validate { credential ->
                val payload = credential.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asInt()
                val user = db.findZookeeper(claimString)
                user
            }
        }
    }

    animalRoutes(db)
    userRoutes(db, jwtService, hashFunction)
}
