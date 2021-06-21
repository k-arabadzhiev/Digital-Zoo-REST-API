package com.pollux.models

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class Zookeeper(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
) : Principal

@Serializable
data class ZookeeperResponse(
    val status: String,
    val message: String? = null,
    val jwt: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)
