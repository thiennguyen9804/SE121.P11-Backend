package com.example

import com.example.data.VocabularyRepositoryImpl
import com.example.data.addData
import com.example.data.dataProcess
import com.example.data.preprocessing
import com.example.data.table.*
import com.example.domain.VocabularyRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

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
    Database.connect("jdbc:sqlite:memory:vocabulary", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction {
        SchemaUtils.create(Vocabularies, PartOfSpeeches, PhrasalVerbs, Definitions, Examples)
        val x = preprocessing()
        val vocabularyList = dataProcess(x)

        for (vocabulary in vocabularyList) {
            transaction {
                addData(vocabulary)
                println("Add successfully ${vocabulary.engVocab}")
            }
        }
    }
}


