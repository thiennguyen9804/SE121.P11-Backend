package com.example.data.entity

fun Definition.toDomainDefiniton(): com.example.domain.data.Definition {
    val definition = this.definition
    val examples = this.examples.map {
        it.example ?: ""
    }.toList()
    return com.example.domain.data.Definition(
        definition = definition,
        examples = examples
    )
}

fun PartOfSpeech.toDomainPartOfSpeech(): com.example.domain.data.PartOfSpeech {
    val partOfSpeech = this.partOfSpeech
    val definitions = this.definitions.map {
        it.toDomainDefiniton()
    }

    return com.example.domain.data.PartOfSpeech(
        partOfSpeech = partOfSpeech,
        definitions = definitions
    )
}

fun PhrasalVerb.toDomainPhrasalVerb(): com.example.domain.data.PhrasalVerb {
    val phrasalVerb = this.phrasalVerb
    val definitions = this.definitions.map {
        it.toDomainDefiniton()
    }

    return com.example.domain.data.PhrasalVerb(
        phrasalVerb = phrasalVerb,
        definitions = definitions
    )
}

fun Vocabulary.toDomainVocabulary(): com.example.domain.data.Vocabulary {
    val engVocab = this.engVocab
    val ipa = this.ipa
    val partOfSpeeches = this.partOfSpeeches.map {
        it.toDomainPartOfSpeech()
    }

    val phrasalVerbs = this.phrasalVerbs.map {
        it.toDomainPhrasalVerb()
    }

    return com.example.domain.data.Vocabulary(
        phrasalVerbs = phrasalVerbs,
        partOfSpeeches = partOfSpeeches,
        engVocab = engVocab,
        ipa = ipa,
    )
}