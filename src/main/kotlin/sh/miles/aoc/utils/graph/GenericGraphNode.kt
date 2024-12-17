package sh.miles.aoc.utils.graph

interface GenericGraphNode: Iterable<GenericGraphNode> {

    fun connectWith(node: GenericGraphNode)


}
