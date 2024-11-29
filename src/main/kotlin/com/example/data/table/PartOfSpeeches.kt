package com.example.data.table

import org.jetbrains.exposed.dao.id.IntIdTable

object PartOfSpeeches : IntIdTable() {
    val vocabulary = reference("vocabulary_id", Vocabularies)
    val partOfSpeech = text("part_of_speech")
}
