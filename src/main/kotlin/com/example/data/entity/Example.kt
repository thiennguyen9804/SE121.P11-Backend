package com.example.data.entity

import com.example.data.table.Examples
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Example(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Example>(Examples)

    var example by Examples.example
    var definition by Definition referencedOn Examples.definition
}
