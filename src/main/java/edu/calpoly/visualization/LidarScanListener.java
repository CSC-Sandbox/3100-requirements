package edu.calpoly.visualization;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.jzy3d.maths.Coord3d;

/**
 * Listens for lidar.scan events on the Blackboard and forwards each scan to a
 * ScanView on the Event Dispatch Thread.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */

public class LidarScanListener implements PropertyChangeListener {
    private final ScanView panel;

    public LidarScanListener(ScanView panel) {
        this.panel = panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (!(newValue instanceof Coord3d[])) {
            return;
        }
        
        Coord3d[] points = (Coord3d[]) newValue;
        SwingUtilities.invokeLater(() -> panel.updatePoints(points));
    }
}
