package aima.core.search.framework.qsearch;

import java.util.*;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeFactory;
import aima.core.search.framework.problem.Problem;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
 * <br>
 * 
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of problem
 *   initialize the explored set to be empty
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     add the node to the explored set
 *     expand the chosen node, adding the resulting nodes to the frontier
 *       only if not in the frontier or explored set
 * </pre>
 * 
 * Figure 3.7 An informal description of the general graph-search algorithm.
 * 
 * <br>
 * This implementation is based on the template method
 * {@link TreeSearch#findNode(Problem, Queue)} of the superclass and provides
 * implementations for the needed primitive operations. It implements a special
 * version of graph search which keeps the frontier short by focusing on the
 * best node for each state only. It should only be used in combination with
 * priority queue frontiers. If a node is added to the frontier, this
 * implementation checks whether another node for the same state already exists
 * and decides whether to replace it or ignore the new node depending on the
 * node's costs (comparator of priority queue is used, if available).
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class GraphSearchReducedFrontier<S, A> extends TreeSearch<S, A> {

	private Set<S> explored = new HashSet<>();
	private Map<S, Node<S, A>> frontierNodeLookup = new HashMap<>();
	private Comparator<? super Node<S, A>> nodeComparator = null;

	public GraphSearchReducedFrontier() {
		this(new NodeFactory<>());
	}

	public GraphSearchReducedFrontier(NodeFactory<S, A> nodeFactory) {
		super(nodeFactory);
	}

	/**
	 * Sets the comparator if a priority queue is used, resets explored list and
	 * state map and calls the inherited version of search.
	 */
	@Override
	public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
		// initialize the explored set to be empty
		if (frontier instanceof PriorityQueue<?>)
			nodeComparator = ((PriorityQueue<Node<S, A>>) frontier).comparator();
		explored.clear();
		frontierNodeLookup.clear();
		return super.findNode(problem, frontier);
	}

	public Comparator<? super Node<S, A>> getNodeComparator() {
		return nodeComparator;
	}

	/**
	 * Inserts the node into the frontier if the node's state is not yet
	 * explored and not present in the frontier. If a second node for the same
	 * state is already part of the frontier, it is checked, which node is
	 * better (with respect to priority). Depending of the result, the existing
	 * node is replaced or the new node is dropped.
	 */
	@Override
	protected void addToFrontier(Node<S, A> node) {
		if (!explored.contains(node.getState())) {
			Node<S, A> frontierNode = frontierNodeLookup.get(node.getState());
			if (frontierNode == null) {
				// child.STATE is not in frontier and not yet explored
				frontier.add(node);
				frontierNodeLookup.put(node.getState(), node);
				updateMetrics(frontier.size());
			} else if (nodeComparator != null && nodeComparator.compare(node, frontierNode) < 0) {
				// child.STATE is in frontier with higher cost
				// replace that frontier node with child
				if (frontier.remove(frontierNode))
					frontierNodeLookup.remove(frontierNode.getState());
				frontier.add(node);
				frontierNodeLookup.put(node.getState(), node);
			}
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds the corresponding
	 * state to the explored set, and returns the node.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		frontierNodeLookup.remove(result.getState());
		// add the node to the explored set
		explored.add(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Checks whether the frontier contains not yet expanded nodes.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		return frontier.isEmpty();
	}
}