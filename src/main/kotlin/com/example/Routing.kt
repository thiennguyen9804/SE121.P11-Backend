package com.example

import com.example.data.entity.Vocabulary
import com.example.data.entity.toDomainVocabulary
import com.example.data.table.Vocabularies
import com.example.domain.VocabularyRepository
import com.example.domain.data.TranslateResult
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting(
    repository: VocabularyRepository
) {

    routing {
        get("/") {
            call.respondText("Hello Kiana-chan!!!")
        }

        get("api/words/{word}") {
            val word = call.parameters["word"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val res = repository.getVocabulary(word)
            if(res == null) {
                return@get call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(res)
            }
        }

        get("api/translate/{word}") {
            val word = call.parameters["word"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            try {
                val text = repository.translate(word)
                val res = TranslateResult(text)
                call.respond(res)
            } catch(e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }
    }
}



