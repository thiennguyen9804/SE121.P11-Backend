package com.example.data.entity

import com.example.data.table.Definitions
import com.example.data.table.PartOfSpeeches
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PartOfSpeech(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PartOfSpeech>(PartOfSpeeches)

    var partOfSpeech by PartOfSpeeches.partOfSpeech
    var vocabulary by Vocabulary referencedOn PartOfSpeeches.vocabulary
    val definitions by Definition optionalReferrersOn Definitions.partOfSpeech
}
