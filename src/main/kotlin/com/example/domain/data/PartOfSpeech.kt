package com.example.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class PartOfSpeech(
    val partOfSpeech: String,
    val definitions: List<Definition>
)
