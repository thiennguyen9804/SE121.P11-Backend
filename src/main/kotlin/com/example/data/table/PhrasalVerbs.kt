package com.example.data.table

import org.jetbrains.exposed.dao.id.IntIdTable

object PhrasalVerbs : IntIdTable() {
    val vocabulary = reference("vocabulary_id", Vocabularies)
    val phrasalVerb = text("phrasal_verb")
}
