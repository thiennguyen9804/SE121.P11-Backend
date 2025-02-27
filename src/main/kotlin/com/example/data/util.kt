package com.example.data

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import com.example.data.raw.PartOfSpeech
import com.example.data.raw.PhrasalVerb
import com.example.data.raw.*
import com.example.fileName

typealias VocabularyDao = com.example.data.entity.Vocabulary
typealias PartOfSpeechDao = com.example.data.entity.PartOfSpeech
typealias PhrasalVerbDao = com.example.data.entity.PhrasalVerb
typealias DefinitionDao = com.example.data.entity.Definition
typealias ExampleDao = com.example.data.entity.Example

typealias RawVocabulary = Vocabulary
typealias RawPhrasalVerb = PhrasalVerb
typealias RawPartOfSpeech = PartOfSpeech
typealias RawDefinition = Definition


fun readFileFromResources(fileName: String): List<String> {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream(fileName)
        ?: throw IllegalArgumentException("File not found: $fileName")
    return inputStream.bufferedReader().use { it.readLines() }
}

fun preprocessing(): List<String> {
    val linesAfterPreProcessing = readFileFromResources(fileName = fileName).toMutableList()
    var lineBefore: String? = null
    var n = linesAfterPreProcessing.size
    var index = 0
    while (true) {
        if (index == n) {
            break
        }
        val line = linesAfterPreProcessing[index]
        if (lineBefore != null) {
            if (lineBefore.startsWith("@") && line.startsWith("-")) {
                linesAfterPreProcessing.add(index = index, element = "*  từ vựng")
                n++
            } else if (line.startsWith("@") && lineBefore.startsWith("-")) {
                linesAfterPreProcessing.add(index = index, element = " ")
                n++
            }
        }
        lineBefore = line
        index++
    }

    return linesAfterPreProcessing

}

fun dataProcess(
    lines: List<String>
): List<RawVocabulary> {
    var engText: String?
    var ipa: String?
    var partOfSpeech: String? = null
    var definition: String? = null
    var examples = emptyList<String>().toMutableList()
    var phrasalVerb: String? = null
    var currentlyAt: Class<*>? = null

    val vocabularyList = emptyList<RawVocabulary>().toMutableList()
    var vocabularyObject = RawVocabulary()
    var partOfSpeechObject = RawPartOfSpeech()
    var phrasalVerbObject = RawPhrasalVerb()

    for (line in lines) {
        if (line.isEmpty()) {
//        Xử lý những phần còn sót lại trước khi bước sang từ mới
            if (definition != null) {
                val definitionObject = Definition(
                    definition = definition,
                    examples = examples
                )
                if (currentlyAt == PartOfSpeech::class.java) {
                    partOfSpeechObject.definitions += definitionObject
                } else {
                    phrasalVerbObject.definitions += definitionObject
                }
                examples = emptyList<String>().toMutableList()
            }

            if (partOfSpeech != null) {
                vocabularyObject.partOfSpeeches += partOfSpeechObject
                partOfSpeechObject = PartOfSpeech()
                examples = emptyList<String>().toMutableList()
                definition = null
            } else if (phrasalVerb != null) {
                vocabularyObject.phrasalVerbs += phrasalVerbObject
                phrasalVerbObject = PhrasalVerb()
                examples = emptyList<String>().toMutableList()
                definition = null
            }

            if (vocabularyObject.engVocab != null) {
                vocabularyList += vocabularyObject
                vocabularyObject = Vocabulary()
            }

            partOfSpeech = null
            phrasalVerb = null
            currentlyAt = null

        } else if (line.startsWith("@")) {
//            Nếu gặp một từ vựng
            if (vocabularyObject.engVocab != null) {
                vocabularyList += vocabularyObject
                vocabularyObject = Vocabulary()
            }
            val pair = splitText(line)
            engText = pair.first
            ipa = pair.second
            vocabularyObject.engVocab = engText
            vocabularyObject.ipa = ipa
        } else if (line.startsWith("*")) {
//            Nếu gặp một từ loại
            if (definition != null) {
                val definitionObject = Definition(
                    definition = definition,
                    examples = examples
                )
                if (currentlyAt == PartOfSpeech::class.java) {
                    partOfSpeechObject.definitions += definitionObject
                } else {
                    phrasalVerbObject.definitions += definitionObject
                }
                examples = emptyList<String>().toMutableList()
            }
            if (currentlyAt == PartOfSpeech::class.java) {
                if (partOfSpeech != null) {
                    vocabularyObject.partOfSpeeches += partOfSpeechObject
                    partOfSpeechObject = PartOfSpeech()
                    examples = emptyList<String>().toMutableList()
                    definition = null
                }
            } else {
                if (phrasalVerb != null) {
                    vocabularyObject.phrasalVerbs += phrasalVerbObject
                    phrasalVerbObject = PhrasalVerb()
                    examples = emptyList<String>().toMutableList()
                    definition = null
                }
            }

            phrasalVerb = null
            currentlyAt = PartOfSpeech::class.java
            partOfSpeech = line.substring(1).trimIndent()
            partOfSpeechObject.partOfSpeech = partOfSpeech
        } else if (line.startsWith("!")) {
//            Nếu gặp một phrasal verb
            if (definition != null) {
                val definitionObject = Definition(
                    definition = definition,
                    examples = examples
                )
                if (currentlyAt == PartOfSpeech::class.java) {
                    partOfSpeechObject.definitions += definitionObject
                } else {
                    phrasalVerbObject.definitions += definitionObject
                }
                examples = emptyList<String>().toMutableList()
            }
            if (currentlyAt == PartOfSpeech::class.java) {
                if (partOfSpeech != null) {
                    vocabularyObject.partOfSpeeches += partOfSpeechObject
                    partOfSpeechObject = PartOfSpeech()
                    examples = emptyList<String>().toMutableList()
                    definition = null
                }
            } else {
                if (phrasalVerb != null) {
                    vocabularyObject.phrasalVerbs += phrasalVerbObject
                    phrasalVerbObject = PhrasalVerb()
                    examples = emptyList<String>().toMutableList()
                    definition = null
                }
            }

            partOfSpeech = null
            currentlyAt = PhrasalVerb::class.java
            phrasalVerb = line.substring(1).trimIndent()
            phrasalVerbObject.phrasalVerb = phrasalVerb
        } else if (line.startsWith("-")) {
//            Nếu gặp một định nghĩa
            if (currentlyAt == PartOfSpeech::class.java) {
                if (definition != null) {
                    val definitionObject = Definition(
                        definition = definition,
                        examples = examples
                    )
                    partOfSpeechObject.definitions += definitionObject
                    examples = emptyList<String>().toMutableList()
                }
            } else if (currentlyAt == PhrasalVerb::class.java) {
                if (definition != null) {
                    val definitionObject = Definition(
                        definition = definition,
                        examples = examples
                    )
                    phrasalVerbObject.definitions += definitionObject
                    examples = emptyList<String>().toMutableList()
                }
            }

            definition = line.substring(1).trimIndent()
        } else if (line.startsWith("=")) {
//            Nếu gặp một ví dụ
            val temp = line.substring(1).trimIndent().replace('+', ':')
            examples += temp
        }
    }

//    for (item in vocabularyList.) {
//        logVocabulary(item)
//    }

    return vocabularyList
}

fun splitText(input: String): Pair<String, String?> {
    val cleanedInput = input.removePrefix("@").trim()
    val slashIndex = cleanedInput.indexOf('/')

    return if (slashIndex == -1) {
        Pair(cleanedInput, null)
    } else {
        val textPart = cleanedInput.substring(0, slashIndex).trim()
        val ipaPart = cleanedInput.substring(slashIndex).trim()
        Pair(textPart, ipaPart)
    }
}

fun logVocabulary(vocabulary: Vocabulary) {
    println(vocabulary)
    println()
    println("Vocabulary: ${vocabulary.engVocab}")
    println("Ipa: ${vocabulary.ipa}")
    println("Part of speech:")
    for (partOfSpeech in vocabulary.partOfSpeeches) {
        println("   ${partOfSpeech.partOfSpeech}:")
        for (definition in partOfSpeech.definitions) {
            println("       ${definition.definition}")
            for (example in definition.examples) {
                println("           $example")
            }
        }
    }
    println("Phrasal verb:")
    for (phrasalVerb in vocabulary.phrasalVerbs) {
        println("   ${phrasalVerb.phrasalVerb}:")
        for (definition in phrasalVerb.definitions) {
            println("       ${definition.definition}")
            for (example in definition.examples) {
                println("           $example")
            }
        }
    }
}


fun addData(vocabularyItem: Vocabulary) {
    val vocabularyEntity = VocabularyDao.new {
        engVocab = vocabularyItem.engVocab!!
        ipa = vocabularyItem.ipa
    }
    for (partOfSpeechItem in vocabularyItem.partOfSpeeches) {
        val partOfSpeechEntity = PartOfSpeechDao.new {
            partOfSpeech = partOfSpeechItem.partOfSpeech ?: "từ vựng"
            vocabulary = vocabularyEntity
        }
        for (definitionItem in partOfSpeechItem.definitions) {
            val definitionEntity = DefinitionDao.new {
                definition = definitionItem.definition ?: ""
                partOfSpeech = partOfSpeechEntity
            }
            for (exampleItem in definitionItem.examples) {
                val exampleEntity = ExampleDao.new {
                    example = exampleItem
                    definition = definitionEntity
                }
            }
        }
    }
//
    for (phrasalVerbItem in vocabularyItem.phrasalVerbs) {
        val phrasalVerbEntity = PhrasalVerbDao.new {
            phrasalVerb = phrasalVerbItem.phrasalVerb!!
            vocabulary = vocabularyEntity
        }
        for (definitionItem in phrasalVerbItem.definitions) {
            val definitionEntity = DefinitionDao.new {
                definition = definitionItem.definition ?: ""
                phrasalVerb = phrasalVerbEntity
            }
            for (exampleItem in definitionItem.examples) {
                val exampleEntity = ExampleDao.new {
                    example = exampleItem
                    definition = definitionEntity
                }
            }
        }
    }
//    }

}