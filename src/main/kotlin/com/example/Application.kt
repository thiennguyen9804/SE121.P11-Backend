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
    Database.connect("jdbc:sqlite:database/vocabulary.sqlite", "org.sqlite.JDBC")
//    Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
//    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
//    transaction {
//        SchemaUtils.create(Vocabularies, PartOfSpeeches, PhrasalVerbs, Definitions, Examples)
//        val x = preprocessing()
//        val vocabularyList = dataProcess(x)
//
//        for (vocabulary in vocabularyList) {
//            transaction {
//                addData(vocabulary)
//                println("Add successfully ${vocabulary.engVocab}")
//            }
//            val heapSize = Runtime.getRuntime().totalMemory()
//            val heapMaxSize = Runtime.getRuntime().maxMemory()
//            println("heap size: ${heapSize / 1024 / 1024}")
//            println("heap max size: ${heapMaxSize / 1024 / 1024}")
//        }
//    }
//    Database.connect(
//        url = "jdbc:postgresql://se121-p11-thiennguyen9804-c654.e.aivencloud.com:16268/defaultdb",
//        driver = "org.postgresql.Driver",
//        user = "avnadmin",
//        password = "AVNS_duK78wPXleZtn_VKtVb",
//    )
//    transaction {
//        SchemaUtils.create(Vocabularies, PartOfSpeeches, PhrasalVerbs, Definitions, Examples)
//        val x = preprocessing()
//        val vocabularyList = dataProcess(x)
//
//        for (vocabulary in vocabularyList) {
//            transaction {
//
//                addData(vocabulary)
//                println("Add successfully ${vocabulary.engVocab}")
//            }
//        }
//    }
}


