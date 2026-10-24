package edu.calpoly.monitor;

import org.tribuo.Example;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.impl.ArrayExample;
import org.tribuo.provenance.SimpleDataSourceProvenance;

import java.util.ArrayList;
import java.util.List;

/**
 * This classifier uses the message rate and the number of seconds since
 * the last message to return NO_ACTIVITY, NORMAL, or HIGH_ACTIVITY.
 *
 * @author an-prata
 * @version 1.0
 */
public class DefaultActivityModel implements ActivityStrategy {
    private final LabelFactory factory = new LabelFactory();
    private final Model<Label> model = trainModel();

    /**
     * Perform a prediction using this strategy.
     *
     * @param input The input on which to form a prediction.
     * @return The prediction, one of "NO_ACTIVITY", "NORMAL", or
     *         "HIGH_ACTIVITY".
     */
    @Override
    public Prediction<Label> predict(Example<Label> input) {
        return model.predict(input);
    }

    private Model<Label> trainModel() {
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
                        "CSC 3100 Sprint 2 activity data",
                        factory
                ),
                factory
        );

        LogisticRegressionTrainer trainer = new LogisticRegressionTrainer();

        return trainer.train(dataset);
    }

    private Example<Label> example(
            String state,
            double messageRate,
            double secondsSinceLastMessage
    ) {
        return new ArrayExample<>(
                factory.generateOutput(state),
                ActivityStrategy.FEATURES,
                new double[] {messageRate, secondsSinceLastMessage}
        );
    }
}
