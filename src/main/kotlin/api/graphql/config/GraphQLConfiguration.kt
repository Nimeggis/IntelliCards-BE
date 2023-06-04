package api.graphql.config


import api.model.User
import api.repository.UserRepository
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.spring.execution.DefaultSpringGraphQLContextFactory
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContextFactory
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLRequestParser
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLServer
import com.expediagroup.graphql.server.types.GraphQLResponse
import com.expediagroup.graphql.server.types.GraphQLServerError
import com.expediagroup.graphql.server.types.GraphQLServerResponse
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.reactor.awaitSingle
import org.neo4j.driver.Driver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

/**
 * Contains bean necessary for GraphQL configuration
 * Provides additional scalars
 */
@Configuration
class GraphQLConfiguration {

    @Bean
    fun reactiveTransactionManager(
        driver: Driver, databaseNameProvider: ReactiveDatabaseSelectionProvider
    ): ReactiveNeo4jTransactionManager {
        return ReactiveNeo4jTransactionManager(driver, databaseNameProvider)
    }

    @Bean
    fun schemaGeneratorHooks() = DefaultSchemaGeneratorHooks

    @Bean
    fun contextFactory(userRepository: UserRepository) = object : DefaultSpringGraphQLContextFactory() {
        override suspend fun generateContextMap(request: ServerRequest): Map<*, Any> {
            val username = request.headers().firstHeader("Authorization") ?: "Test"
            var user = userRepository.findByUsername(username)
            if (user == null) {
                user = userRepository.save(User(username)).awaitSingle()
            }
            return super.generateContextMap(request) + mapOf(User::class to user!!)
        }
    }

}

val DataFetchingEnvironment.user get() = this.graphQlContext.get<User>(User::class)