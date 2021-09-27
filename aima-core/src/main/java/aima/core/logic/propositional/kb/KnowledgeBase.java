package aima.core.logic.propositional.kb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.inference.TTEntails;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.ConjunctionOfClauses;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.transformations.ConvertToConjunctionOfClauses;
import aima.core.logic.propositional.transformations.SymbolCollector;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class KnowledgeBase {
	private List<Sentence> sentences = new ArrayList<>();
	private ConjunctionOfClauses asCNF = new ConjunctionOfClauses(Collections.emptySet());
	private Set<PropositionSymbol> symbols = new LinkedHashSet<>();
	private PLParser parser = new PLParser();


	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(String aSentence) {
		tell((Sentence) parser.parse(aSentence));
		
	}
	
	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(Sentence aSentence) {
		if (!(sentences.contains(aSentence))) {
			sentences.add(aSentence);
			asCNF = asCNF.extend(ConvertToConjunctionOfClauses.apply(aSentence).getClauses());
			symbols.addAll(SymbolCollector.getSymbolsFrom(aSentence));
		}
	}

	/**
	 * Each time the agent program is called, it TELLS the knowledge base what
	 * it perceives.
	 * 
	 * @param percepts
	 *            what the agent perceives
	 */
	public void tellAll(String[] percepts) {
		for (String percept : percepts) {
			tell(percept);
		}

	}

	/**
	 * Returns the number of sentences in the knowledge base.
	 * 
	 * @return the number of sentences in the knowledge base.
	 */
	public int size() {
		return sentences.size();
	}

	/**
	 * Returns the list of sentences in the knowledge base chained together as a
	 * single sentence.
	 * 
	 * @return the list of sentences in the knowledge base chained together as a
	 *         single sentence.
	 */
	public Sentence asSentence() {
		return Sentence.newConjunction(sentences);
	}
	
	/**
	 * 
	 * @return a Conjunctive Normal Form (CNF) representation of the Knowledge Base.
	 */
	public Set<Clause> asCNF() {
		return asCNF.getClauses();
	}
	
	/**
	 * 
	 * @return a unique set of the symbols currently contained in the Knowledge Base.
	 */
	public Set<PropositionSymbol> getSymbols() {
		return symbols;
	}

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean askWithTTEntails(String queryString) {
		PLParser parser = new PLParser();

		Sentence alpha = parser.parse(queryString);

		return new TTEntails().isEntailed(this, alpha);
	}

	@Override
	public String toString() {
		return sentences.isEmpty() ? "" : asSentence().toString();
	}

	/**
	 * Returns the list of sentences in the knowledge base.
	 * 
	 * @return the list of sentences in the knowledge base.
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}
}