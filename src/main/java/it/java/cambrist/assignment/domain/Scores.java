package it.java.cambrist.assignment.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scores {

	private Map<Double, List<UserObject>> scores = new HashMap<>();
	
	public Scores() {
		this.scores = new HashMap<>();
	}

	/**
	 * @return the scores
	 */
	public Map<Double, List<UserObject>> getScores() {
		return scores;
	}

	/**
	 * @param scores the scores to set
	 */
	public void setScores(Map<Double, List<UserObject>> scores) {
		this.scores = scores;
	}

	public void putUserObject(Double score, UserObject user){
		if(scores.containsKey(score))
			scores.get(score).add(user);
		else
			scores.put(score, new ArrayList<>(Arrays.asList(new UserObject[]{user})));
	}
}
