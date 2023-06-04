package api.repository

import api.model.Material
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialRepository : ReactiveNeo4jRepository<Material, String>