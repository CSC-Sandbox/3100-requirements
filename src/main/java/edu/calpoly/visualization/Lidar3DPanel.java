// CLASS THAT WOULD DISPLAY DATA, or the PANEL IN THIS CASE

package edu.calpoly.visualization;
 
import javax.swing.JPanel;  // LIBRARY -> I WANT TO BE A WINDOW... from analogy from class

import org.jzy3d.maths.Coord3d; // to map the coordinates

import org.jzy3d.plot3d.primitives.Scatter; // to create the 3D

import org.jzy3d.chart.AWTChart; // constructor

import org.jzy3d.chart.factories.EmulGLChartFactory; // returning type = AWTChart\

import java.awt.Component; // AWT 


/** lidar3DPanel take the point data cloud and then it reders it as a jzy3D scatter chart, and then it store/embed that chart into a JPanel
  * 1. Represents the point data
  * 2. construct our chart
  * 3. reders the chart
  * @author Diego Martinez Parra (sp4msvwnz)
  * @version version 2.0 (2026-09-28)
  * lidar3DPanel
  */


public class Lidar3DPanel extends JPanel { //is a typed JPanel: inheritance so lidar3DPanel (child) -> JPanel (parent)
    
    private static final long serialVersionUID = 1l; // swing component

    public Lidar3DPanel(Coord3d[] points) { // constructor from the second library
        //create the 3D Scatter object
        Scatter scatter = new Scatter(points); //drawable representations of points

        EmulGLChartFactory factory = new EmulGLChartFactory(); // constructs what kind of chart object

        AWTChart chart = factory.newChart(); // therefore since call a methods and the return must be AWTChart

        chart.add(scatter); //adds the scatter object to  the graph

        // #192: enables the mouse interaction with the 3D Camera
        chart.addMouseCameraController();


        Component canvas = (Component)chart.getCanvas(); // Does this return ICanvas? Yes, casting plays a big role to return from and Obkect to the actual obkect

        this.add(canvas); // therefore Panel becomes teh container, and canvas the child

    }
}


