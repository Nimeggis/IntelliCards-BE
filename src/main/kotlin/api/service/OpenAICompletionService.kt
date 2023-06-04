package api.service

import api.ApiConfiguration
import com.theokanning.openai.completion.CompletionRequest
import com.theokanning.openai.service.OpenAiService
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

val enPromptPrefix = listOf(
    "Create three pairs of question and answer from the following text. ",
    "Format your response in JSON as follows: ",
    "[ { \"question\": \"This is the question.\", \"answer\": \"This is the answer.\" } ]\n",
    "This is the text: "
).joinToString("")

val dePromptPrefix = listOf(
    "Erstelle drei Paare von Frage und Antwort mit dem folgenden Text. ",
    "Formatiere deine Antwort in JSON wie folgt: ",
    "[ { \"question\": \"Das ist die Frage.\", \"answer\": \"Das ist die Antwort.\" } ]\n",
    "Das ist der Text: "
).joinToString("")

const val maxTries = 10

@Service
class OpenAICompletionService(private val config: ApiConfiguration) {

    private val openaiService = OpenAiService(config.openAIKey!!)

    @OptIn(ExperimentalTime::class)
    fun generateFlashcard(document: Map<Int, List<String>>): List<FlashcardDTO> {
        val res = document.entries.parallelStream().map { (index, page) ->
            val (flashcards, duration) = measureTimedValue {
                repeat(maxTries) {
                    val prompt = enPromptPrefix + page.joinToString(" ")
                    val completionRequest = CompletionRequest.builder()
                        .prompt(prompt)
                        .model("text-davinci-003")
                        .maxTokens(1024)
                        .temperature(0.5)
                        .build()
                    val responseText = openaiService.createCompletion(completionRequest).choices.first().text
                    val flashcardsForPage = decodeResponse(responseText, index)
                    if (flashcardsForPage != null) {
                        return@measureTimedValue flashcardsForPage
                    }
                }
                println("Failed to generate flashcards for page $index after $maxTries tries")
                null
            }
            if (flashcards != null) {
                println("Generated flashcards for page $index in ${duration.toString(DurationUnit.MILLISECONDS)}ms")
                flashcards
            } else {
                println("Failed to generate flashcards for page $index")
                emptyList()
            }
        }.toList()
        return res.flatten()
    }

    private fun decodeResponse(response: String, page: Int): List<FlashcardDTO>? {
        return try {
            Json.decodeFromString<List<FlashcardDTO>>(response)
        } catch (e: Exception) {
            val jsonStart = response.indexOf("[")
            if (jsonStart >= 0) {
                decodeResponse(response.substring(jsonStart), page)
            } else {
                print("Invalid JSON for page $page")
                null
            }
        }
    }

}

@Serializable
data class FlashcardDTO(val question: String, val answer: String)