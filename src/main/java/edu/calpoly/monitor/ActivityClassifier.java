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