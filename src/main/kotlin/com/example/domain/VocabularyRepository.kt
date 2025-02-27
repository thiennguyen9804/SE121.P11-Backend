package com.example.domain

import com.example.domain.data.Vocabulary


interface VocabularyRepository {
    suspend fun getVocabulary(word: String): Vocabulary?
    suspend fun translate(word: String): String
}