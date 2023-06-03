import java.io.File

fun main(args: Array<String>) {
    val fileName = "gdpr_sample.pdf"
    val file = File(fileName);

    var contents = FormRecognizer.extractTextFromFile(file)
    contents = ContentTransformations.filterOutShortPages(contents)
}