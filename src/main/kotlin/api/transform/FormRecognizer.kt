package api.transform

import com.azure.ai.formrecognizer.*
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder
import com.azure.ai.formrecognizer.documentanalysis.models.*
import com.azure.core.credential.AzureKeyCredential
import com.azure.core.util.BinaryData
import com.azure.core.util.polling.SyncPoller
import java.io.File

object FormRecognizer {
    // set `<your-endpoint>` and `<your-key>` variables with the values from the Azure portal
    private const val endpoint = "https://flashcarddocanalyzer.cognitiveservices.azure.com/"
    private const val key = "<API key>"

    fun extractTextFromFile(file: File): MutableMap<Int, List<String>> {

        // create your `DocumentAnalysisClient` instance and `AzureKeyCredential` variable
        val client: DocumentAnalysisClient = DocumentAnalysisClientBuilder()
            .credential(AzureKeyCredential(key))
            .endpoint(endpoint)
            .buildClient()

        val modelId = "prebuilt-document"
        val analyzeDocumentPoller: SyncPoller<OperationResult, AnalyzeResult> =
            client.beginAnalyzeDocument(modelId, BinaryData.fromBytes(file.readBytes()))
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