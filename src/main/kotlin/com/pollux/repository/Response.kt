package com.pollux.repository

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val status: String,
    val body: String
)
