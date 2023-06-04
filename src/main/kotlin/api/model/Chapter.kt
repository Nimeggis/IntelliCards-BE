package api.model

import io.github.graphglue.model.*

@DomainNode
class Chapter(
    @FilterProperty
    var title: String
) : Node() {

    companion object {
        const val FLASHCARDS = "FLASHCARDS"
    }

    @NodeRelationship(FLASHCARDS, Direction.OUTGOING)
    @FilterProperty
    val flashcards by NodeSetProperty<Flashcard>()

    @NodeRelationship(Material.CHAPTERS, Direction.INCOMING)
    val material by NodeProperty<Material>()

}