package edu.calpoly.eye;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * Sprint 2 program
 *
 * @author Marshall Ramsey (mars-rams)
 * @version 1.0
 *
 **/
public final class GazeStatistics {
	
	private GazeStatistics(){
		// Utility class
	}

	public static double mean(double[] values) {
		DescriptiveStatistics statistics =
			new DescriptiveStatistics(values);
		return statistics.getMean();
	}

	public static double standardDeviation(double[] values) {
		DescriptiveStatistics statistics =
			new DescriptiveStatistics(values);
		return statistics.getStandardDeviation();
	}

}
