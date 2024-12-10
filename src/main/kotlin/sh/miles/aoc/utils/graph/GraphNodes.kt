package sh.miles.aoc.utils.graph

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

