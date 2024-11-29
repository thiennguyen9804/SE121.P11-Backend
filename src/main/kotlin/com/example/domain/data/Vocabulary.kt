package com.example.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class Vocabulary(
    val engVocab: String,
    val ipa: String?,
    val partOfSpeeches: List<PartOfSpeech>,
    val phrasalVerbs: List<PhrasalVerb>?
)
