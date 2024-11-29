package com.example.data

import com.example.data.entity.Vocabulary
import com.example.data.entity.toDomainVocabulary
import com.example.data.table.Vocabularies
import com.example.domain.VocabularyRepository
import org.jetbrains.exposed.sql.transactions.transaction

typealias DomainVocabulary = com.example.domain.data.Vocabulary
typealias DomainPartOfSpeech = com.example.domain.data.PartOfSpeech
typealias DomainPhrasalVerb = com.example.domain.data.PhrasalVerb
typealias DomainDefinition = com.example.domain.data.Definition

class VocabularyRepositoryImpl : VocabularyRepository {
    override suspend fun getVocabulary(word: String): DomainVocabulary? {
        return transaction {
            return@transaction Vocabulary.find {
                Vocabularies.engVocab eq word
            }.firstOrNull()?.toDomainVocabulary()
        }
    }
}