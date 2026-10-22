package edu.calpoly.visualization;

import org.jzy3d.maths.Coord3d;

/**
 * A view that renders a LiDAR point cloud. Lets the scan listener depend on
 * the rendering contract instead of a concrete Swing component.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */

public interface ScanView {
    
    void updatePoints(Coord3d[] points);
}
