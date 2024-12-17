package sh.miles.aoc.utils.graph

class CardinalNode<E>(val value: E, private val emptyValue: E) : GenericGraphNode {
    companion object {
        const val NORTH = 0
        const val EAST = 1;
        const val SOUTH = 2;
        const val WEST = 3;
    }

    val direction: Array<CardinalNode<E>> = Array(4) { CardinalNode(emptyValue, emptyValue) }

    override fun connectWith(node: GenericGraphNode) {
        if (node !is CardinalNode<*>) throw IllegalArgumentException("can not input node of class ${node::class.java} into node of type ${this::class.java}")

        if (direction[NORTH].value == emptyValue) {
            direction[NORTH] = node as CardinalNode<E>
            node.direction[SOUTH] = this
        } else if (direction[EAST].value == emptyValue) {
            direction[EAST] = node as CardinalNode<E>
            node.direction[WEST] = this
        } else if (direction[SOUTH].value == emptyValue) {
            direction[SOUTH] = node as CardinalNode<E>
            node.direction[NORTH] = this
        } else if (direction[WEST].value == emptyValue) {
            direction[WEST] = node as CardinalNode<E>
            node.direction[EAST] = this
        } else {
            throw IllegalArgumentException("Can not connect node $node because all directions of this node $this are full")
        }
    }

    override fun iterator(): Iterator<GenericGraphNode> {
        return direction.iterator()
    }


}

class NaiveGraphNode<E>(val value: E) : GenericGraphNode {
    private val connections: MutableList<NaiveGraphNode<E>> = mutableListOf()

    override fun connectWith(node: GenericGraphNode) {
        if (node !is NaiveGraphNode<*>) throw IllegalArgumentException("can not input node of class ${node::class.java} into node of type ${this::class.java}")
        connections.add(node as NaiveGraphNode<E>)
    }


    override fun toString(): String {
        return "$value{${connections}}"
    }

    override fun iterator(): Iterator<NaiveGraphNode<E>> {
        return connections.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NaiveGraphNode<*>) return false

        if (value != other.value) return false
        if (connections != other.connections) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + connections.hashCode()
        return result
    }

}

