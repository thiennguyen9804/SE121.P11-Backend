package com.example.data.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Definitions : IntIdTable() {
    val partOfSpeech = reference("part_of_speech_id", PartOfSpeeches).nullable()
    val phrasalVerb = reference("phrasal_verb_id", PhrasalVerbs).nullable()
    val definition = text("definition")
}