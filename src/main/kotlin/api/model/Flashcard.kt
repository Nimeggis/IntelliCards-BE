package api.model

import io.github.graphglue.model.*
import org.neo4j.cypherdsl.core.Relationship

@DomainNode
class Flashcard(
    @FilterProperty
    val question: String,
    @FilterProperty
    val answer: String
) : Node() {

    @NodeRelationship(Chapter.FLASHCARDS, Direction.INCOMING)
    val chapter by NodeProperty<Chapter>()

}