package com.example.data.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import com.example.data.table.Vocabularies
import com.example.data.table.PhrasalVerbs
import com.example.data.table.PartOfSpeeches

class Vocabulary(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Vocabulary>(Vocabularies)

    var engVocab by Vocabularies.engVocab
    var ipa by Vocabularies.ipa
    val partOfSpeeches by PartOfSpeech referrersOn PartOfSpeeches.vocabulary
    val phrasalVerbs by PhrasalVerb referrersOn PhrasalVerbs.vocabulary
}




