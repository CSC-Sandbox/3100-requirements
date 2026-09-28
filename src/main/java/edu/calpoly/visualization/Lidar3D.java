package edu.calpoly.visualization;

import java.util.ArrayList;
import java.util.List;
import org.jzy3d.maths.Coord3d;

/**
 * User Story #135 -- Visualize LiDAR Data in 3D.
 *
 * Converts LiDAR measurements into Jzy3d coordinates and accumulates them
 * into a point cloud. Holds no GUI code: Lidar3DPanel displays this data.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */

public class Lidar3D {
    private final List<Coord3d> points = new ArrayList<>();

    public static Coord3d toCoord3d(double x, double y, double z) {
        return new Coord3d(x,y,z);
    }

    public static Coord3d[] toPointCloud(double[][] lidarData) { 
        if(lidarData == null) { 
            return new Coord3d[0]; 
        }

        List<Coord3d> pointCloud = new ArrayList<>();
        for(int row = 0; row < lidarData.length; row++) {
            if(lidarData[row] != null && lidarData[row].length >= 3) {
                double x = lidarData[row][0];
                double y = lidarData[row][1];
                double z = lidarData[row][2];
                Coord3d point = toCoord3d(x,y,z);
                pointCloud.add(point);
            }
        }
        return pointCloud.toArray(new Coord3d[0]);
    }

    public synchronized void addPoint(double x, double y, double z) {
        points.add(toCoord3d(x, y, z));
    }

    public synchronized List<Coord3d> getPoints() {
        return new ArrayList<>(points);
    }

    public synchronized int size() {
        return points.size();
    }

}
