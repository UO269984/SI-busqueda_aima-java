package aima.gui.demo.search;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.GraphSearch4e;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.framework.Metrics;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.*;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

/**
 * Demonsrates how different search algorithms perform on the NQueens problem.
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */

public class NQueensDemo {

	private static int boardSize = 16;
	private static final int NUM_REPS = 30;

	public static void main(String[] args) {
		startNQueensDemo();
	}

	private static void startNQueensDemo() {
		//solveNQueensWithDepthFirstSearch();
		//solveNQueensWithBreadthFirstSearch();
		//solveNQueensWithAStarSearch4e();
		//solveNQueensWithRecursiveDLS();
		//solveNQueensWithIterativeDeepeningSearch();
		//solveNQueensWithSimulatedAnnealingSearch();
		//solveNQueensWithHillClimbingSearch();
		//solveNQueensWithGeneticAlgorithmSearch();
		//solveNQueensWithRandomWalk();
		
		solveNQueensWithGeneticAlgorithmSearch();
		/*for (int i = 10; i <= 16; i += 8) {
			boardSize = i;
			System.out.println("Board size: " + boardSize);
			
			//getAveragePathCost();
			solveNQueensWithAStarSearch();
		}*/
	}
	
	private static void getAveragePathCost() {
		double pathCost = 0;
		double time = 0;
		int reexpandedNodes = 0;
		
		for (int rep = 0; rep < NUM_REPS; rep++) {
			Metrics m = solveNQueensWithAStarSearch();
			
			pathCost += m.getDouble("pathCost");
			time += m.getDouble("timeTaken");
			reexpandedNodes += m.getInt("nodesExpandedReinsertedInFrontier");
		}
		
		System.out.println("Average path cost: " + (pathCost / NUM_REPS)
			+ "\nAverage reexpanded nodes: " + (reexpandedNodes / NUM_REPS) + "\nAverage time: " + (time / NUM_REPS));
	}

	private static void solveNQueensWithDepthFirstSearch() {
		System.out.println("\n--- NQueensDemo DFS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new TreeSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithBreadthFirstSearch() {
		System.out.println("\n--- NQueensDemo BFS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new BreadthFirstSearch<>(new GraphSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static Metrics solveNQueensWithAStarSearch() {
		//System.out.println("\n--- NQueensDemo A* (complete state formulation, graph search 3e) ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		//Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		
		SearchForActions<NQueensBoard, QueenAction> search = new AStarSearch<>
				(new GraphSearch<>(), NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		//actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics() + "\n");
		return search.getMetrics();
	}

	private static void solveNQueensWithAStarSearch4e() {
		System.out.println("\n--- NQueensDemo A* (complete state formulation, graph search 4e) ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem
				(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new AStarSearch<>
				(new GraphSearch4e<>(), NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithRecursiveDLS() {
		System.out.println("\n--- NQueensDemo recursive DLS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(boardSize);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithIterativeDeepeningSearch() {
		System.out.println("\n--- NQueensDemo Iterative DS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new IterativeDeepeningSearch<>();
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithSimulatedAnnealingSearch() {
		System.out.println("\n--- NQueensDemo Simulated Annealing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SimulatedAnnealingSearch<NQueensBoard, QueenAction> search =
				new SimulatedAnnealingSearch<>(NQueensFunctions::getNumberOfAttackingPairs,
						new Scheduler(20, 0.045, 100));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		System.out.println("Final State:\n" + search.getLastState());
	}

	private static void solveNQueensWithHillClimbingSearch() {
		System.out.println("\n--- NQueensDemo HillClimbing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		HillClimbingSearch<NQueensBoard, QueenAction> search = new HillClimbingSearch<>
				(n -> -NQueensFunctions.getNumberOfAttackingPairs(n));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		System.out.println("Final State:\n" + search.getLastState());
	}

	private static void solveNQueensWithGeneticAlgorithmSearch() {
		System.out.println("\n--- NQueensDemo GeneticAlgorithm ---");
		
		final int popSize = 50;
		final double mutationProbability = 0.05;
		final double crossoverProbability = 0.5;
		final int numberOfGenerations = 100;
		
		FitnessFunction<Integer> fitnessFunction = NQueensGenAlgoUtil.getFitnessFunction();
		Predicate<Individual<Integer>> goalTest = NQueensGenAlgoUtil.getGoalTest();
		// Generate an initial population
		int numSolutions = 0;
		double fitness = 0;
		
		FitnessData[] averageFitness = new FitnessData[numberOfGenerations];
		for (int i = 0; i < numberOfGenerations; i++)
			averageFitness[i] = new FitnessData(0, 0);
		
		for (int rep = 0; rep < NUM_REPS; rep++) {
			Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
			for (int i = 0; i < popSize; i++)
				population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));
			
			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(boardSize,
				NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize), mutationProbability, crossoverProbability);
			
			// Run for a set amount of time
			List<FitnessData> fitnessHistoric = new ArrayList<FitnessData>();
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, numberOfGenerations, fitnessHistoric);
			int i = 0;
			for (FitnessData f : fitnessHistoric) {
				averageFitness[i].averageFitness += f.averageFitness;
				averageFitness[i++].bestFitness += f.bestFitness;
			}
			
			//printGeneticData(bestIndividual, boardSize, fitnessFunction.apply(bestIndividual), goalTest.test(bestIndividual), ga);
			fitness += fitnessFunction.apply(bestIndividual);
			if (goalTest.test(bestIndividual))
				numSolutions++;
		}
		
		System.out.println("Average fitness;Best fitness");
		for (FitnessData f : averageFitness)
			System.out.println((f.averageFitness / NUM_REPS) + ";" + (f.bestFitness / NUM_REPS));
		
		System.out.println("Population: " + popSize);
		System.out.println("Mutation Probability: " + mutationProbability);
		System.out.println("Crossover Probability: " + crossoverProbability);
		System.out.println("Num generations: " + numberOfGenerations + "\n");
		
		System.out.println("Average fitness: " + fitness / NUM_REPS);
		System.out.println("Num solutions: " + numSolutions);
		
		// Run till goal is achieved
		/*bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L, null);
		System.out.println("\nMax time unlimited, Best Individual:\n" +
			NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));*/
	}
	
	private static void printGeneticData(Individual<Integer> bestIndividual, int boardSize, double bestIndividualFitness, boolean isGoal, GeneticAlgorithm<Integer> ga) {
		System.out.println("Max time 1 second, Best Individual:\n"
			+ NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		System.out.println("Board Size      = " + boardSize);
		System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		System.out.println("Fitness         = " + bestIndividualFitness);
		System.out.println("Is Goal         = " + isGoal);
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}

	// Here, this trivial algorithm outperforms the genetic search approach as described in the textbook!
	private static void solveNQueensWithRandomWalk() {
		System.out.println("\n--- NQueensDemo RandomWalk ---");
		NQueensBoard board;
		int i = 0;
		long startTime = System.currentTimeMillis();
		do {
			i++;
			board = new NQueensBoard(boardSize, Config.QUEEN_IN_EVERY_COL);
		} while (board.getNumberOfAttackingPairs() > 0);
		long stopTime = System.currentTimeMillis();
		System.out.println("Solution found after generating " + i + " random configurations ("
				+ (stopTime - startTime) + " ms).");
	}
}
