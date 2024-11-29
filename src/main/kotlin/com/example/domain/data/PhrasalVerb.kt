package com.example.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class PhrasalVerb(
    val phrasalVerb: String,
    val definitions: List<Definition>
)