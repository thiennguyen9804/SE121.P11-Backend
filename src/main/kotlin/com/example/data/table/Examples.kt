package com.example.data.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Examples : IntIdTable() {
    val example = text("examples").nullable()
    val definition = reference("definition_id", Definitions)
}
