package api.service

import api.ApiConfiguration
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder
import com.azure.ai.formrecognizer.documentanalysis.models.*
import com.azure.core.credential.AzureKeyCredential
import com.azure.core.util.BinaryData
import com.azure.core.util.polling.SyncPoller
import org.springframework.stereotype.Service
import java.io.File

@Service
class FormRecognizerService(private val config: ApiConfiguration) {
    fun extractTextFromFile(file: BinaryData): Map<Int, List<String>> {

        // create your `DocumentAnalysisClient` instance and `AzureKeyCredential` variable
        val client: DocumentAnalysisClient = DocumentAnalysisClientBuilder()
            .credential(AzureKeyCredential(config.formRecognizerKey!!))
            .endpoint(config.formRecognizerEndpoint)
            .buildClient()

        val modelId = "prebuilt-document"
        val analyzeDocumentPoller: SyncPoller<OperationResult, AnalyzeResult> =
            client.beginAnalyzeDocument(modelId, file)
        val analyzeResult: AnalyzeResult = analyzeDocumentPoller.getFinalResult()

        val contents = mutableMapOf<Int, List<String>>();
        // pages
        analyzeResult.getPages().forEach { documentPage ->

            var page_content = emptyList<String>()

            // lines
            documentPage.getLines().forEach { documentLine ->
                page_content = page_content + documentLine.content
            }

            contents[documentPage.pageNumber] = page_content
        }

        return contents
    }
}