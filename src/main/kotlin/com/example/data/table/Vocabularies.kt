package com.example.data.table


import org.jetbrains.exposed.dao.id.IntIdTable

object Vocabularies : IntIdTable() {
    val engVocab = text("eng_vocab")
    val ipa = text("ipa").nullable()
}