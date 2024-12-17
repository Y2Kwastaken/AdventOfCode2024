package sh.miles.aoc.year.y2024

import org.jgrapht.Graph
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm
import org.jgrapht.alg.shortestpath.ALTAdmissibleHeuristic
import org.jgrapht.alg.shortestpath.BidirectionalAStarShortestPath
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.builder.GraphTypeBuilder
import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.charGridOf
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day16 : Day {
    private val NEXT_GRAPH: () -> Graph<GridCoord, DefaultEdge> = {
        GraphTypeBuilder.undirected<GridCoord, DefaultEdge>()
            .allowingMultipleEdges(true)
            .allowingSelfLoops(false)
            .edgeClass(DefaultEdge::class.java)
            .weighted(false)
            .buildGraph()
    }
    private const val END = 'E'
    private const val START = 'S'
    private const val EMPTY_SPACE = '.'
    private val VALID_LAND_POINTS = listOf(START, END, EMPTY_SPACE)

    override fun run(file: Path): ResultUnion {
        val grid = charGridOf(file.readLines())

        return ResultUnion(
            timeFunction { partOne(grid.copy(), NEXT_GRAPH()) },
            timeFunction { partTwo(grid.copy(), NEXT_GRAPH()) }
        )
    }

    private fun partOne(grid: Grid<Char>, graph: Graph<GridCoord, DefaultEdge>): Int {
        fillGraph(grid, graph)
        val algorithm = DijkstraShortestPath(graph, Double. POSITIVE_INFINITY)
        val path = algorithm.getPath(grid.findFirst(START), grid.findFirst(END))
        println(path.length)
        for (gridCoord in path.vertexList) {
            grid[gridCoord] = 'O'
        }

        println(grid)
        return 0
    }

    private fun partTwo(grid: Grid<Char>, graph: Graph<GridCoord, DefaultEdge>): Int {
        return 0
    }

    private fun fillGraph(grid: Grid<Char>, graph: Graph<GridCoord, DefaultEdge>) {
        grid.findAll(VALID_LAND_POINTS).forEach { coord ->
            if (graph.containsVertex(coord)) return@forEach
            graph.addVertex(coord)
            GridDirection.CARDINAL_DIRECTIONS.map { coord.withDirection(it) }
                .filter { grid.contains(it) && grid[it] in VALID_LAND_POINTS }
                .forEach { peeked ->
                    graph.addVertex(peeked)
                    graph.addEdge(coord, peeked)
                }
        }
    }

}
