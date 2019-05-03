package byow.hw4;

import byow.Core.WeightedEdge;

import java.util.List;

/**
 * Represents a graph of vertices.
 * Created by hug.
 */
public interface AStarGraph<Vertex> {
    List<WeightedEdge> neighbors(Vertex v);
    double estimatedDistanceToGoal(Vertex s, Vertex goal);
}
