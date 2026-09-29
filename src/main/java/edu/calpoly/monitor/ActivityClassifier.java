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
 * Classifies recent monitoring-system activity using a Tribuo model.
 * <p>
 * The classifier uses the message rate and the number of seconds since
 * the last message to return NO_ACTIVITY, NORMAL, or HIGH_ACTIVITY.</p>
 * @author tphan56
 * @version 1.0
 */



public final class ActivityClassifier {

    private static final String[] FEATURES = {
        "messageRate",
        "secondsSinceLastMessage"
    };

    private static final LabelFactory FACTORY = new LabelFactory();
    private static final Model<Label> MODEL = trainModel();

    private static Example<Label> example(
        String state,
        double messageRate,
        double secondsSinceLastMessage) {

    return new ArrayExample<>(
            FACTORY.generateOutput(state),
            FEATURES,
            new double[] {messageRate, secondsSinceLastMessage});
}
    private static Model<Label> trainModel() {

        List<Example<Label>> examples = new ArrayList<>();

        examples.add(example("NO_ACTIVITY", 0.0, 60.0));
        examples.add(example("NO_ACTIVITY", 0.0, 45.0));
        examples.add(example("NO_ACTIVITY", 0.1, 30.0));
        examples.add(example("NO_ACTIVITY", 0.2, 20.0));

        examples.add(example("NORMAL", 1.0, 2.0));
        examples.add(example("NORMAL", 2.0, 1.0));
        examples.add(example("NORMAL", 3.0, 1.0));
        examples.add(example("NORMAL", 4.0, 0.5));

        examples.add(example("HIGH_ACTIVITY", 8.0, 0.2));
        examples.add(example("HIGH_ACTIVITY", 10.0, 0.1));
        examples.add(example("HIGH_ACTIVITY", 12.0, 0.1));
        examples.add(example("HIGH_ACTIVITY", 15.0, 0.0));

        MutableDataset<Label> dataset = new MutableDataset<>(
                examples,
                new SimpleDataSourceProvenance(
                        "CSC 3100 Sprint 2 activity data", FACTORY),
                FACTORY);

        LogisticRegressionTrainer trainer =
                new LogisticRegressionTrainer();

        return trainer.train(dataset);
    }

    /**
     * Classifies the current level of system activity.
     *
     * @param messageRate messages received per second during the last minute
     * @param secondsSinceLastMessage seconds since the most recent message
     * @return NO_ACTIVITY, NORMAL, or HIGH_ACTIVITY
     * @author tphan56
     */


    public static String classify(
            double messageRate,
            double secondsSinceLastMessage) {

        Example<Label> input = new ArrayExample<>(
                FACTORY.getUnknownOutput(),
                FEATURES,
                new double[] {messageRate, secondsSinceLastMessage});

        Prediction<Label> prediction = MODEL.predict(input);

        return prediction.getOutput().getLabel();
    }
}