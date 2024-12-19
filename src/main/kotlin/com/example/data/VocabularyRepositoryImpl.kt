package com.example.data

import com.example.data.entity.Vocabulary
import com.example.data.entity.toDomainVocabulary
import com.example.data.table.Vocabularies
import com.example.domain.VocabularyRepository
import com.google.auth.oauth2.GoogleCredentials
import org.jetbrains.exposed.sql.transactions.transaction
import com.google.cloud.translate.*;
import io.github.cdimascio.dotenv.dotenv
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.inputStream
import kotlin.io.path.writeText

typealias DomainVocabulary = com.example.domain.data.Vocabulary
typealias DomainPartOfSpeech = com.example.domain.data.PartOfSpeech
typealias DomainPhrasalVerb = com.example.domain.data.PhrasalVerb
typealias DomainDefinition = com.example.domain.data.Definition

class VocabularyRepositoryImpl : VocabularyRepository {
    private var transService: Translate
    init {
//        For local use
//        val dotenv = dotenv {
//            filename = ".env"
//        }

//        For render use
        val credentialsJson = System.getenv("CREDENTIALS_JSON")
            ?: throw IllegalArgumentException("Environment variable CREDENTIALS_JSON is not set!")
        val tempFile = createTempFile("credentials", ".json").apply {
            writeText(credentialsJson)
        }
        val inputStream = tempFile.inputStream()
        transService = inputStream.use {
            val myCredentials = GoogleCredentials.fromStream(it)
            val translateOptions =
                TranslateOptions.newBuilder().setCredentials(myCredentials).build()
            translateOptions.service
        }
    }
    override suspend fun getVocabulary(word: String): DomainVocabulary? {
        return transaction {
            return@transaction Vocabulary.find {
                Vocabularies.engVocab eq word
            }.firstOrNull()?.toDomainVocabulary()
        }
    }

    override suspend fun translate(word: String): String {
        val translated = transService.translate(
            word,
            Translate.TranslateOption.targetLanguage("vi"),
            Translate.TranslateOption.model("base")
        )

        return translated.translatedText!!
    }
}