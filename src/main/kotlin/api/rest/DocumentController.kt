package api.rest

import api.repository.ChapterRepository
import api.repository.FlashcardRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.IOException


@RestController
class DocumentController(
    private val chapterRepository: ChapterRepository,
    private val flashcardRepository: FlashcardRepository
) {


    @PostMapping("/file/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Throws(IOException::class)
    suspend fun uploadFile(@RequestPart("file") file: FilePart, @PathVariable("id") id: String): String {
        println(id)
        println(file.filename())
        return "File Uploaded"
    }

}