package api.model

import io.github.graphglue.model.*

@DomainNode
class Material(@OrderProperty var title: String) : Node() {

    companion object {
        const val CHAPTERS = "CHAPTERS"
    }

    @NodeRelationship(CHAPTERS, Direction.OUTGOING)
    @FilterProperty
    val chapters by NodeSetProperty<Chapter>()

    @NodeRelationship(User.MATERIALS, Direction.INCOMING)
    val user by NodeProperty<User>()

}