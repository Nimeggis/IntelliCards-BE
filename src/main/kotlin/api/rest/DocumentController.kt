package api.rest

import api.model.Flashcard
import api.repository.ChapterRepository
import api.repository.FlashcardRepository
import api.service.ContentTransformationsService
import api.service.FormRecognizerService
import api.service.OpenAICompletionService
import com.azure.core.util.BinaryData
import jakarta.transaction.Transactional
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.IOException


@RestController
class DocumentController(
    private val chapterRepository: ChapterRepository,
    private val flashcardRepository: FlashcardRepository,
    private val formRecognizerService: FormRecognizerService,
    private val contentTransformationsService: ContentTransformationsService,
    private val openAICompletionService: OpenAICompletionService
) {


    @PostMapping("/file/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Throws(IOException::class)
    suspend fun uploadFile(@RequestPart("file") file: FilePart, @PathVariable("id") id: String): String {
        val content = file.content()
        val dataBuffer = content.collectList().block()?.reduce { buffer1, buffer2 -> buffer1.write(buffer2) }
        val byteBuffer = dataBuffer!!.toByteBuffer()
        val binaryDataFile = BinaryData.fromBytes(byteBuffer.array())

        var contents = formRecognizerService.extractTextFromFile(binaryDataFile)
        contents = contentTransformationsService.filterOutShortPages(contents)
        contents = contentTransformationsService.filterOutRecurringText(contents)
        val flashcards = openAICompletionService.generateFlashcard(contents)

        val chapter = chapterRepository.findById(id).awaitSingle()
        chapter.flashcards() += flashcards.map { Flashcard(it.question, it.answer) }
        println(id)
        println(file.filename())
        return "File Uploaded"
    }

}