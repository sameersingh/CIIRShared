package edu.umass.ciir.models;

import java.util.Collection;
import java.util.HashMap;

/**
 * Provides an aggregated view of the term statistics for a collection.
 * 
 * @author Jeff Dalton
 *
 */
public class CollectionTermStatistics {
	
	/**
	 * Contains information about every term in the collection.
	 */
	HashMap<String, LanguageModelStatEntry> m_index = new HashMap<String, LanguageModelStatEntry>();
	
	/**
	 * The number of language models in the collection
	 */
	private int m_numLanguageModels = 0;
	
	/**
	 * Keeps track of the length of each language model
	 */
	private long m_languageModelLengthSum = 0;
	
	public CollectionTermStatistics() {
		
	}
	
	/**
	 * Adds the language model 
	 * @param lm
	 */
	public void addLanguageModel(LanguageModel lm) {
		Collection<TermEntry> vocabulary = lm.getEntries();
		for (TermEntry termEntry : vocabulary) {
			String term = termEntry.getTerm();
			LanguageModelStatEntry languageModelStats = m_index.get(term);
			if (languageModelStats == null) {
				languageModelStats = new LanguageModelStatEntry(term);
				m_index.put(term, languageModelStats);
			}
			languageModelStats.incrementDocFrequency();
			languageModelStats.addTf(termEntry.getFrequency());
		}
		m_numLanguageModels++;
		m_languageModelLengthSum += lm.getCollectionFrequency();
	}
	
	public double getAverageModelLength() {
		return m_languageModelLengthSum / m_numLanguageModels;
	}
	
	/**
	 * Returns the number of language models that contain that term
	 * 
	 * @param term
	 * @return
	 */
	public int getDocumentFrequency(String term) {
		LanguageModelStatEntry stats = m_index.get(term);
		if (stats == null) {
			return 0;
		} else {
			return stats.getDocumentFrequency();
		}
	}
	
	public Collection<LanguageModelStatEntry> getLanguageModelStats() {
		return m_index.values();
	}
	
	public class LanguageModelStatEntry {
		String m_term;
		public String getTerm() {
			return m_term;
		}

		int m_documentFrequency=0;
		long m_termFrequency=0;
		
		public LanguageModelStatEntry(String term) {
			m_term = term;
		}
		
		public int getDocumentFrequency() {
			return m_documentFrequency;
		}

		public long getTermFrequency() {
			return m_termFrequency;
		}

		public void incrementDocFrequency() {
			m_documentFrequency++;
		}
		
		public void addTf(long tf) {
			m_termFrequency+= tf;
		}
	}

}
