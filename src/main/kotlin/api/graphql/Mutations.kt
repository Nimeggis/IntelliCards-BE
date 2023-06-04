package api.graphql

import api.graphql.config.AutoPayloadType
import api.graphql.config.user
import api.graphql.input.CreateChapterInput
import api.graphql.input.CreateFlashcardInput
import api.graphql.input.CreateMaterialInput
import api.graphql.input.CreateUserInput
import api.model.Chapter
import api.model.Flashcard
import api.model.Material
import api.model.User
import api.repository.*
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component

@Component
class Mutations(
    private val chapterRepository: ChapterRepository,
    private val flashcardRepository: FlashcardRepository,
    private val userRepository: UserRepository,
    private val materialRepository: MaterialRepository
) : Mutation {

    @AutoPayloadType("The created chapter")
    suspend fun createChapter(
        input: CreateChapterInput
    ): Chapter {
        val material = materialRepository.findById(input.material)
        val chapter = Chapter(input.title)
        chapter.material().value = material
        return chapterRepository.save(chapter).awaitSingle()
    }

    @AutoPayloadType("The created flashcard")
    suspend fun createFlashcard(
        input: CreateFlashcardInput
    ): Flashcard {
        val chapter = chapterRepository.findById(input.chapter)
        val flashcard = Flashcard(input.question, input.answer)
        flashcard.chapter().value = chapter
        return flashcardRepository.save(flashcard).awaitSingle()
    }

    @AutoPayloadType("The created material")
    suspend fun createMaterial(
        input: CreateMaterialInput,
        dfe: DataFetchingEnvironment
    ): Material {
        val material = Material(input.title)
        material.user().value = dfe.user
        return materialRepository.save(material).awaitSingle()
    }

}