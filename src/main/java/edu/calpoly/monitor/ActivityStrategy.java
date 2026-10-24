package edu.calpoly.monitor;

import org.tribuo.Example;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;

/**
 * Interface for strategies used by `ActivityClassifier`.
 *
 * @author an-prata
 * @version 1.0
 */
public interface ActivityStrategy {
    /**
     * Inputs expected in the input of `predict`.
     */
    public static final String[] FEATURES = {
            "messageRate",
            "secondsSinceLastMessage"
    };

    /**
     * Perform a prediction with this strategy.
     *
     * @param input Input to predict on, expected fields from `FEATURES`.
     * @return The made prediction.
     */
    Prediction<Label> predict(Example<Label> input);
}
