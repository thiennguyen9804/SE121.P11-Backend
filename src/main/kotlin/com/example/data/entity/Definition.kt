package com.example.data.entity

import com.example.data.table.Definitions
import com.example.data.table.Examples
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Definition(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Definition>(Definitions)

    var definition by Definitions.definition
    var partOfSpeech by PartOfSpeech optionalReferencedOn Definitions.partOfSpeech
    var phrasalVerb by PhrasalVerb optionalReferencedOn Definitions.phrasalVerb
    val examples by Example referrersOn Examples.definition
}