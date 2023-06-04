package api

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("api")
data class ApiConfiguration(
    val formRecognizerKey: String?,
    val formRecognizerEndpoint: String = "https://flashcarddocanalyzer.cognitiveservices.azure.com/",
    val openAIKey: String?,
)