package edu.calpoly.test.sprint2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jzy3d.maths.Coord3d;

/**
 * Sprint 2 test program for Issue #135 -- Visualize LiDAR Data in 3D.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * Expected student interface:
 *
 *   public static Coord3d toCoord3d(double x, double y, double z)
 *   public static Coord3d[] toPointCloud(double[][] lidarData)
 *
 * This program tests the data transformation used by the Jzy3d visualization.
 * The Swing/Jzy3d GUI is verified manually.
 *
 * This program intentionally uses main() rather than JUnit. JUnit will be
 * introduced in a later activity.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.1
 */
public class TestLidar3D {

    private static final String IMPLEMENTATION_CLASS =
            "edu.calpoly.visualization.Lidar3D";

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - LiDAR 3D Test");

        testPoint(1.25, -2.50, 0.75);
        testPoint(0.0, 0.0, 0.0);
        testPoint(-4.0, 3.5, 2.0);

        testPointCloud();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("LiDAR 3D tests failed.");
        }
    }

    private static void testPoint(double x, double y, double z) {
        try {
            Class<?> clazz = Class.forName(IMPLEMENTATION_CLASS);
            Method method = clazz.getMethod(
                    "toCoord3d", double.class, double.class, double.class);

            if (!Modifier.isStatic(method.getModifiers())) {
                fail("toCoord3d must be static");
                return;
            }

            Object result = method.invoke(null, x, y, z);

            if (!(result instanceof Coord3d)) {
                fail("toCoord3d must return Coord3d");
                return;
            }

            Coord3d point = (Coord3d) result;

            if (same(point.x, x) && same(point.y, y) && same(point.z, z)) {
                pass("Coord3d(" + x + ", " + y + ", " + z + ")");
            } else {
                fail("Expected (" + x + ", " + y + ", " + z
                        + ") but received (" + point.x + ", "
                        + point.y + ", " + point.z + ")");
            }
        } catch (ClassNotFoundException e) {
            fail("Missing class " + IMPLEMENTATION_CLASS);
        } catch (NoSuchMethodException e) {
            fail("Missing public static method: "
                    + "toCoord3d(double, double, double)");
        } catch (Exception e) {
            fail("Unexpected error: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
        }
    }

    private static void testPointCloud() {
        double[][] lidarData = {
                {1.0, 2.0, 3.0},
                {-4.5, 0.0, 1.25},
                {7.0, -8.0, 0.5}
        };

        try {
            Class<?> clazz = Class.forName(IMPLEMENTATION_CLASS);
            Method method = clazz.getMethod("toPointCloud", double[][].class);

            if (!Modifier.isStatic(method.getModifiers())) {
                fail("toPointCloud must be static");
                return;
            }

            Object result = method.invoke(null, (Object) lidarData);

            if (!(result instanceof Coord3d[])) {
                fail("toPointCloud must return Coord3d[]");
                return;
            }

            Coord3d[] points = (Coord3d[]) result;

            if (points.length != lidarData.length) {
                fail("Expected " + lidarData.length + " points but received "
                        + points.length);
                return;
            }

            for (int i = 0; i < lidarData.length; i++) {
                if (!same(points[i].x, lidarData[i][0])
                        || !same(points[i].y, lidarData[i][1])
                        || !same(points[i].z, lidarData[i][2])) {
                    fail("Point cloud coordinates are incorrect at index " + i);
                    return;
                }
            }

            pass("Multiple XYZ measurements converted to Coord3d[]");
        } catch (ClassNotFoundException e) {
            fail("Missing class " + IMPLEMENTATION_CLASS);
        } catch (NoSuchMethodException e) {
            fail("Missing public static method: toPointCloud(double[][])");
        } catch (Exception e) {
            fail("Unexpected error: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
        }
    }

    private static boolean same(double a, double b) {
        return Math.abs(a - b) < 0.000001;
    }

    private static void pass(String message) {
        passed++;
        System.out.println("[PASS] " + message);
    }

    private static void fail(String message) {
        failed++;
        System.out.println("[FAIL] " + message);
    }
}
