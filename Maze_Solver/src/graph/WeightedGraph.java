package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */

	private Map<V, HashMap<V, Integer>> weightedGraph;


	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;


	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		observerList = new HashSet<GraphAlgorithmObserver<V>>(); /* creates observerList collection,
	                                                         	used hashset because its fast */ 
		weightedGraph = new HashMap<V, HashMap<V, Integer>>(); /* creates the map that will be used to represent 
		                                                     the "weighted graph" */
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	/* Adds an observer to the collection of observers */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	/* adds vertex to the weighted graph could throw exception is element is already in it */ 
	public void addVertex(V vertex) {
		if (weightedGraph.containsKey(vertex)) {
			throw new IllegalArgumentException();
		} else {
			weightedGraph.put(vertex, new HashMap<V, Integer>());
		}
	}

	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	/* checks if element is in graph */ 
	public boolean containsVertex(V vertex) {
		if (weightedGraph.containsKey(vertex)) {
			return true;
		} else {
			return false; 
		}
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	/* creates an edge with a weight between two vertices */ 
	public void addEdge(V from, V to, Integer weight) {
		if ((!(weightedGraph.containsKey(from)) || (!(weightedGraph.containsKey(to))) || (weight < 0))) {
			throw new IllegalArgumentException(); 
		} else {
			weightedGraph.get(from).put(to, weight);
		}
	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	/* returns the weight of an edge going from one vertex to an other 
	 * because directed graph */ 
	
	public Integer getWeight(V from, V to) {
		if (!(weightedGraph.containsKey(from)) || (!(weightedGraph.containsKey(to)))) {
			throw new IllegalArgumentException();
		} else {
			if (!(weightedGraph.get(from).containsKey(to))) {
				return null;
			} else {
				return weightedGraph.get(from).get(to);
			}
		}
	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	/* does  breadth first search, looks at all the adjacent elements from the start 
	 * then the adjacent elements of those and so on until it finds the end element then stops */

	public void DoBFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList ) {
			observer.notifyBFSHasBegun();	
		}
		ArrayList<V> visitSet = new ArrayList<>();
		Queue<V> nextSet = new LinkedList<>();

		nextSet.add(start);
		while (nextSet.size()>0) {
			V cur = nextSet.remove();
			if (cur.equals(end)) {
				for (GraphAlgorithmObserver<V> observer : observerList ) {
					observer.notifySearchIsOver();	
				}
				break;
			}
			if (!(visitSet.contains(cur))) {
				visitSet.add(cur);
				for (GraphAlgorithmObserver<V> observer : observerList ) {
					observer.notifyVisit(cur);	
				}
				for (V adjacentElements : weightedGraph.get(cur).keySet()) {
					if (!(visitSet.contains(adjacentElements))) {
						nextSet.add(adjacentElements);
					}
				}
			}
		}
		return; 
	}

	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	/* does a depth first search, starts a starting vertex then goes along a path until 
	 * it can no longer go forward so it traverses back and takes a different path, until the end 
	 * vertex is reached */ 
	public void DoDFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList ) {
			observer.notifyDFSHasBegun();	
		}
		ArrayList<V> visitSet = new ArrayList<>();
		Stack<V> nextSet = new Stack<>();

		nextSet.add(start);
		while (nextSet.size()>0) {
			V cur = nextSet.pop();
			if (cur.equals(end)) {
				for (GraphAlgorithmObserver<V> observer : observerList ) {
					observer.notifySearchIsOver();	
				}
				break;
			}
			if (!(visitSet.contains(cur))) {
				visitSet.add(cur);
				for (GraphAlgorithmObserver<V> observer : observerList ) {
					observer.notifyVisit(cur);	
				}
				for (V adjacentElements : weightedGraph.get(cur).keySet()) {
					if (!(visitSet.contains(adjacentElements))) {
						nextSet.add(adjacentElements);
					}
				}
			}
		}
		return; 
	}

	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	/* creates a map of the smallest cost (least weight) it will take from the start vertex to every other vertex 
	 * and a seperate map of the proceeding vertex on the path of each element 
	 * then makes path from start to end using the two maps 
	 */
	public void DoDijsktra(V start, V end) {
		int curSmallestCost;
		V cur = start;
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraHasBegun();
		}
		ArrayList<V> visitSet = new ArrayList<>();
		Map<V, Integer> costMap = new HashMap<V, Integer>();
		Map<V,V> preMap = new HashMap<V,V>();
		for (V element : weightedGraph.keySet()) {
			costMap.put(element, 99999999);
			preMap.put(element, null);
		} // creates maps and adds elements to default values 
		costMap.put(start, 0);
		preMap.put(start, start);

		while (visitSet.size() < weightedGraph.size()) { // goes until every element is visited
			curSmallestCost = 99999;
			cur = start;
			for ( V element : costMap.keySet()) {
				if (!(visitSet.contains(element))) {
					if (costMap.get(element) < curSmallestCost) {
						curSmallestCost = costMap.get(element);
						cur = element;
					}
				}
			}
			visitSet.add(cur); // checks it off as minimal cost to get to element
			for (GraphAlgorithmObserver<V> observer : observerList ) {
				observer.notifyDijkstraVertexFinished(cur, curSmallestCost);
			}
			for (V element: weightedGraph.get(cur).keySet()) {
				if (!(visitSet.contains(element))) {
					if (costMap.get(cur) + this.getWeight(cur,  element) < costMap.get(element)) {
						costMap.put(element, costMap.get(cur) + this.getWeight(cur,  element));
						preMap.put(element, cur);
					} // adds to map if their cost is less then what is already there
				}
			}
		}
		/* finds lowest cost path from start to end */ 

		List<V> path = new ArrayList<V>();
		V current = end;
		while (current != start) {
			path.add(0,current);
			current = preMap.get(current);
		}
		for (GraphAlgorithmObserver<V> observer : observerList) {
			observer.notifyDijkstraIsOver(path);
		}
		return;
	}
}
