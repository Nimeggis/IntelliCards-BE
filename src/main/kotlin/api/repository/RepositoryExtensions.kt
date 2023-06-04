package api.repository

import com.expediagroup.graphql.generator.scalars.ID
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

suspend fun <T> ReactiveNeo4jRepository<T, String>.findById(id: ID): T {
    return findById(id.value).awaitSingle()
}

suspend fun <T> ReactiveNeo4jRepository<T, String>.findAllById(ids: Iterable<ID>): List<T> {
    return findAllById(ids.map { it.value }).collectList().awaitSingle()
}