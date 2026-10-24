package edu.calpoly.monitor;
import java.util.ArrayList;
import java.util.List;

import org.tribuo.Example;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.impl.ArrayExample;
import org.tribuo.provenance.SimpleDataSourceProvenance;


/**
 * Classifies recent monitoring-system activity using an `ActivityStrategy` model.
 *
 * @author tphan56
 * @version 2.0
 */
public final class ActivityClassifier {
    private static ActivityStrategy STRATEGY = new DefaultActivityModel();

    /**
     * Set the strategy to be used.
     * @param strategy The `ActivityStrategy` to classify with.
     */
    public static void setStrategy(ActivityStrategy strategy) {
        STRATEGY = strategy;
    }

    /**
     * Classifies the current level of system activity.
     *
     * @param messageRate messages received per second during the last minute
     * @param secondsSinceLastMessage seconds since the most recent message
     * @return Output classification of strategy.
     * @author tphan56
     */
    public static String classify(
            double messageRate,
            double secondsSinceLastMessage
    ) {
        Example<Label> input = new ArrayExample<>(
                new LabelFactory().getUnknownOutput(),
                ActivityStrategy.FEATURES,
                new double[] {messageRate, secondsSinceLastMessage}
        );

        Prediction<Label> prediction = STRATEGY.predict(input);

        return prediction.getOutput().getLabel();
    }
}