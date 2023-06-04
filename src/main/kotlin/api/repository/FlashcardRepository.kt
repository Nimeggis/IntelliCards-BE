package api.repository

import api.model.Flashcard
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface FlashcardRepository : ReactiveNeo4jRepository<Flashcard, String>