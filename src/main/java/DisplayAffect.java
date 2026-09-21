import edu.calpoly.provided.Broker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.xy.*;

import java.awt.*;

public class DisplayAffect extends ApplicationFrame {
    private static final String CHART_NAME = "Affect Display";
    /** Maximum samples/width of x-axis to display at once */
    private static final int MAX_SAMPLES = 50;

    private final Broker broker;

    private final JFreeChart chart;

    private final XYSeries focusSeries;
    private final XYSeries excitementSeries;
    private final XYSeries engagementSeries;
    private final XYSeries interestSeries;
    private final XYSeries stressSeries;

    public DisplayAffect(Broker broker) {
        super(CHART_NAME);
        this.broker = broker;

        focusSeries = createSeries("Focus");
        excitementSeries = createSeries("Excitement");
        engagementSeries = createSeries("Engagement");
        interestSeries = createSeries("Interest");
        stressSeries = createSeries("Stress");

        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(focusSeries);
        dataSet.addSeries(excitementSeries);
        dataSet.addSeries(engagementSeries);
        dataSet.addSeries(interestSeries);
        dataSet.addSeries(stressSeries);

        chart = createChart(dataSet);
        ChartPanel panel = new ChartPanel(chart);

        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));

        setContentPane(panel);
    }

    /**
     * Receive a message from the broker and append it to existing data.
     */
    public void update() {
        String message = broker.receive();
        Affect affect = Affect.fromString(message);

        double focusSample = focusSeries.getMaxX();
        focusSeries.add(Double.isNaN(focusSample) ? 1.0 : focusSample + 1.0, affect.getFocus());

        double excitementSample = excitementSeries.getMaxX();
        excitementSeries.add(Double.isNaN(excitementSample) ? 1.0 : excitementSample + 1.0, affect.getExcitement());

        double engagementSample = engagementSeries.getMaxX();
        engagementSeries.add(Double.isNaN(engagementSample) ? 1.0 : engagementSample + 1.0, affect.getEngagement());

        double interestSample = interestSeries.getMaxX();
        interestSeries.add(Double.isNaN(interestSample) ? 1.0 : interestSample + 1.0, affect.getInterest());

        double stressSample = stressSeries.getMaxX();
        stressSeries.add(Double.isNaN(stressSample) ? 1.0 : stressSample + 1.0, affect.getStress());

        chart.clearSubtitles();
        chart.addSubtitle(0, new TextTitle("Focus: " + affect.getFocus()));
        chart.addSubtitle(1, new TextTitle("Excitement: " + affect.getExcitement()));
        chart.addSubtitle(2, new TextTitle("Engagement: " + affect.getEngagement()));
        chart.addSubtitle(3, new TextTitle("Interest: " + affect.getInterest()));
        chart.addSubtitle(4, new TextTitle("Stress: " + affect.getStress()));
        chart.addLegend(new LegendTitle(((XYPlot)chart.getPlot()).getRenderer()));
    }

    /**
     * Create a new chart using the given `XYDataset`. Updating the given data
     * set or series it contains will update the chart returned by this
     * function.
     */
    private static JFreeChart createChart(XYDataset dataSet) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                CHART_NAME,
                "Samples",
                "Affective Value",
                dataSet
        );

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();

        if (r instanceof XYLineAndShapeRenderer renderer) {
            renderer.setDrawSeriesLineAsPath(true);
        }

        return chart;
    }

    /**
     * Create a new series, applying relevant configuration for this class.
     */
    private static XYSeries createSeries(String name) {
        XYSeries s = new XYSeries(name);
        s.setMaximumItemCount(MAX_SAMPLES);
        return s;
    }

    public static void main(String[] args) {
        Broker broker = new Broker("localhost", 5000);
        DisplayAffect da = new DisplayAffect(broker);

        da.update();

        da.pack();
        UIUtils.centerFrameOnScreen(da);
        da.setVisible(true);

        for (;;) {
            da.update();
        }
    }
}
