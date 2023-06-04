package api.graphql

import api.graphql.config.user
import api.model.User
import api.repository.UserRepository
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class Queries(private val userRepository: UserRepository) : Query {

    suspend fun user(dfe: DataFetchingEnvironment): User {
        return dfe.user
    }

}