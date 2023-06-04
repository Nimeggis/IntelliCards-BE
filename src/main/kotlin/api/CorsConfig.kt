package api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.server.WebFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): WebFilter {
        val config = CorsConfiguration()
        config.addAllowedOriginPattern("*")  // Allow requests from all origins
        config.addAllowedMethod("*")  // Allow all HTTP methods
        config.addAllowedHeader("*")  // Allow all headers
        config.allowCredentials = true  // Allow cookies, authorization headers, etc.

        val filter = CorsWebFilter { exchange ->
            config
        }
        return filter
    }
}
