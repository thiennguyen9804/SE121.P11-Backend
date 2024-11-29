package com.example.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class Definition(
    val definition: String,
    val examples: List<String>?
)