package aima.core.search.framework.qsearch;

import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

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
 * <br>
 * This implementation is based on the template method
 * {@link TreeSearch#findNode(Problem, Queue)} of the superclass and provides
 * implementations for the primitive operations including node filtering mechanisms
 * to avoid that nodes of already explored states are selected for expansion.
 * In contrast to the pseudocode
 * above, here, nodes resulting from node expansion are added to the frontier
 * even if nodes for the same states already exist there. This makes it possible
 * to use the implementation also in combination with priority queue frontiers.
 * This implementation avoids linear costs for frontier node removal (compared
 * to {@link GraphSearchReducedFrontier}) and gets by without node comparator
 * knowledge.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public class GraphSearch<S, A> extends TreeSearch<S, A> {

	private Set<S> explored = new HashSet<>();

	public GraphSearch() {
		this(new NodeFactory<>());
	}

	public GraphSearch(NodeFactory<S, A> nodeFactory) {
		super(nodeFactory);
	}

	/**
	 * Clears the set of explored states and calls the search implementation of
	 * {@link TreeSearch}.
	 */
	@Override
	public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		return super.findNode(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * was not yet explored.
	 */
	@Override
	protected void addToFrontier(Node<S, A> node) {
		if (!explored.contains(node.getState())) {
			frontier.add(node);
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds the corresponding
	 * state to the explored set, and returns the node. Leading nodes of already
	 * explored states are dropped. So the resulting node state will always be
	 * unexplored yet.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node<S, A> removeFromFrontier() {
		cleanUpFrontier(); // not really necessary because isFrontierEmpty should be called before...
		Node<S, A> result = frontier.remove();
		explored.add(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Pops nodes of already explored states from the head of the frontier
	 * and checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		cleanUpFrontier();
		updateMetrics(frontier.size());
		return frontier.isEmpty();
	}

	/**
	 * Helper method which removes nodes of already explored states from the head
	 * of the frontier.
	 */
	private void cleanUpFrontier() {
		while (!frontier.isEmpty() && explored.contains(frontier.element().getState()))
			frontier.remove();
	}
}