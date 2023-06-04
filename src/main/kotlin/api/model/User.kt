package api.model

import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.Node
import io.github.graphglue.model.NodeRelationship

@DomainNode
class User(val username: String) : Node() {

    companion object {
        const val MATERIALS = "MATERIALS"
    }

    @NodeRelationship(MATERIALS, Direction.OUTGOING)
    val materials by NodeSetProperty<Material>()

}