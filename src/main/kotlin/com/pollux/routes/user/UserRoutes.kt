package com.pollux.routes.user

import com.pollux.auth.JwtService
import com.pollux.repository.Repository
import io.ktor.application.*
import io.ktor.routing.*

fun Application.userRoutes(db: Repository, jwtService: JwtService, hashFunction: (String) -> String) {
    routing {
        addZookeeper(db, jwtService, hashFunction)
        zookeeperLogin(db, jwtService, hashFunction)
        zookeeperLogout(db)
        deleteZookeeper(db)
    }
}