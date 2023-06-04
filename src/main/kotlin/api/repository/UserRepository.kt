package api.repository

import api.model.User
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : ReactiveNeo4jRepository<User, String> {

    suspend fun findByUsername(username: String): User?

    suspend fun existsByUsername(username: String): Boolean

}