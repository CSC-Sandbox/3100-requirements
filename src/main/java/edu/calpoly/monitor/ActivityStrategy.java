package edu.calpoly.monitor;

import org.tribuo.Example;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;

public interface ActivityStrategy {
    public static final String[] FEATURES = {
            "messageRate",
            "secondsSinceLastMessage"
    };

    Prediction<Label> predict(Example<Label> input);
}
