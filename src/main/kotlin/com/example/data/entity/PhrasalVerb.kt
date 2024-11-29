package com.example.data.entity

import com.example.data.table.Definitions
import com.example.data.table.PhrasalVerbs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PhrasalVerb(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PhrasalVerb>(PhrasalVerbs)

    var phrasalVerb by PhrasalVerbs.phrasalVerb
    var vocabulary by Vocabulary referencedOn PhrasalVerbs.vocabulary
    val definitions by Definition optionalReferrersOn Definitions.phrasalVerb
}

