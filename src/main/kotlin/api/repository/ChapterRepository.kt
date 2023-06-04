package api.repository

import api.model.Chapter
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : ReactiveNeo4jRepository<Chapter, String>