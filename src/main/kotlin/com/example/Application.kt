package com.example

import com.example.data.VocabularyRepositoryImpl
import com.example.data.entity.Vocabulary
import com.example.data.entity.toDomainVocabulary
import com.example.data.table.Vocabularies
import com.example.domain.VocabularyRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction


fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val repository: VocabularyRepository = VocabularyRepositoryImpl()
    configureDatabase()
    configureSerialization()
    configureRouting(repository)
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

fun configureDatabase() {
    Database.connect("jdbc:sqlite:vocabulary.sqlite", "org.sqlite.JDBC")
}


