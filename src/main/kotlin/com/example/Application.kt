package com.example

import com.example.data.VocabularyRepositoryImpl
import com.example.domain.VocabularyRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database

const val fileName = "real.txt"


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
    // For render run
    Database.connect("jdbc:sqlite:/app/database/vocabulary.sqlite", driver = "org.sqlite.JDBC")
    // For local run
//    Database.connect("jdbc:sqlite:database/vocabulary.sqlite", driver = "org.sqlite.JDBC")
}


