# STORY #135- Visualize LiDAR DATA IN 3D


## OVERVIEW

Story #135 extends the existing LiDAR visualization in order to use the LiDAR measurement to inspect
3D dimensional mapping while preserving the original 2D map (from previous stories).

Moreover, 

The existing `DisplayLidar` Implementation continues to received the `LiDAR,X,Y,Z` messages through the `Broker` as expected, while validating the metrics used as well.

    - The existing 2D mapping and,
    - a Jzy3d 3D point-cloud visualization (as a result of the implementation).

Therefore,

The 3D visualization is embedded inside the existing `Swing` application rather than just opening a separate Jzy3d window.

------

## USER STORY

> As a user, I want to visualize LiDAR Measurement in three dimensions so that I can inspect the spatial structure represented by the sensor data.

-------


## ACCEPTANCE CRITERIA

- LiDAR measurements are displayed using X, Y, Z, coordinates.
- Multiple measurements can be displayed (at the same tiem) as a 3D point-cloud.
- The user can inspect the point cloud from different perspectives.
- Existing LiDAR behavior continues to work.
- The project builds successfully with MAVEN.

-------


## ARCHITECTURE/DESIGN

The implementations is pretty much straight foward because it separates the LiDAR data, 3D visualization, and the existing application responsabilities across the following classes:
    ### `DisplayLidar`
        - Remains responsible for the existing LiDAR application.
        - Responsabilities:
            - Receiving `LiDAR, X, Y, Z,` messages through `Broker.`
            - Parsing and validating incoming LiDAR measurments;
            - Maintaing the existing 2D mapping;
            - Forwarding valid XYZ coordinates to the 3D LiDAR model;
            - Embedding the 2D and 3D visualization in the same Swing Window, and
            - Safely shceduling 3D visualization updates on the Swing Event Thread.
    
    ### `Lidar3D`
        - Represents the 3D LiDAR point-cloud data.
        - Resposabilities:
            - Converting X, Y, Z, data poiunts into Jzy3d `Coord3d` objects.
            - Converting multiple LiDAR measurements into a point-cloud
            - Accumulating incoming `Coord3d,` and
            - Providing a copy of the accumulated points for visualization
    
    ### `Lidar3DPanel`
        -Is the Swing component responsible for displaying the 3D point-cloud.
        - Responsabilities:
            - Creating the Jzy3d `Scatter`
            - Creating and embedding the Jzy3d chart,
            - Updating the existing scatter when new arrive
            - Recalculating the chart bounds when needed
            - Repainting/Rerendering the chart without repoening the application, and
            - Enabling mouse rotating and zoom
    
    ### `Broker`
        - also represents a big role because it provides the existing communication used by `DisplayLidar` to receive LiDAR messages

-----


## Data

So how is the data flowing along the system? 

Text -> (hits Broker) -> LiDAR X, Y, Z -> DisplayLidar (parser) -> 2D and Lidar3D -> coord3d[] -> Lidar3DPanel -> Scatter -> AWTChart

-----

## 2D and 3D Layout

For this part of the code, the existing Swing Window is preserved and refresh automatically.
In other words,

A `BorderLayout` is used to display both visualization in the same interface
    -> JFrame -> JPanel -> West and Center -> Lidar3DPanel

-----

The Jzy3d visualization is embedded into the Swing Interface: as a result the implementation does NOT use chart.open().

-------

## Live 3D Updates

LiDar Messages are received on a background thread because `Broker.received()` blocks.

After, the validation occurs: 
    1. `DisplayLidar` updates the existing 2D mapping.
    2. The XYZ is added to `Lidar3D`
    3. A snapshot of the `Coord3d` point is created.
    4. SwingUtiities.invokeLater()... schedules the visualization update.
    5. Lidar3DPanel updates the existing `Scatter`
    6. The Jzy3d view bounds are recalculated
    7. The existing chart is rendered again

This allows new data (measurements) to become visible without having to refresh or re-run the code.

----


## 3D interaction

The embedded Jzy3d chart uses its mouse camera controller.

The user can:
    - Drag the mouse to rotate the 3D scene, and
    - Use the mouse wheel or trackpad scrolling to zoom in and out

The interaction remains inside the existing Swing application and (of course) does not interfere with the 2D mapping.


----


## Jzy3d Maven Dependency

Jzy3d -> Mavem

It releases repository is configured in `pom.xml`

```xml
<repositories>
    <repository>
        <id>jzy3d-releases</id>
        <name>Jzy3d Releases</name>
        <url>https://maven.jzy3d.org/releases/</url>
    </repository>
</repositories>
```

Therefore,

No Jzy3d JAR files are manually stored in the repository

-----


## Build

From the repository Root, Run:

```bash
mvn compile
```

The integrated Story #135 implementation has been verified to compile successfully with Maven.

The Jzy3d dependency can be verified with:

```bash
mvn dependency:tree -Dincludes=org.jzy3d
```

Maven resolves:

```text
org.jzy3d:jzy3d-emul-gl-awt:2.2.1
```

along with its required Jzy3d dependencies.

---

## Running the LiDAR Visualization

The existing course-provided LiDAR simulator can be used to exercise the visualization.

First run:

```text
edu.calpoly.test.sprint1.TestDisplayLidar
```

The simulator sends repeated messages in the form:

```text
LIDAR,X,Y,Z
```

Then run:

```text
edu.calpoly.provided.DisplayLidar
```

The application displays the existing 2D occupancy map and the embedded Jzy3d 3D visualization in the same window.

As measurements arrive:

- the 2D occupancy map continues to update; and
- the 3D scatter is refreshed with the accumulated XYZ measurements.

---

## Verification

The completed implementation was manually verified for the following behavior:

- The existing 2D occupancy map continues to display incoming LiDAR measurements.
- Multiple XYZ measurements accumulate into the 3D point cloud.
- Newly received points become visible without reopening the application.
- The 3D scene can be rotated using the mouse.
- The 3D scene can be zoomed using the mouse wheel or trackpad.
- The 2D and 3D visualizations remain available in the same Swing window.
- Maven successfully resolves the Jzy3d dependency.
- `mvn compile` completes successfully.
- No Jzy3d JAR files are manually included in the repository.

---

## UML

The UML class diagram for Story #135 focuses on the classes and relationships required to understand the feature as an engineering blueprint.

The primary classes are:

- `DisplayLidar`
- `Lidar3D`
- `Lidar3DPanel`
- `Broker`

Relevant external types include:

- `JPanel`
- `Coord3d`
- `Scatter`
- `AWTChart`

The diagram documents the separation between communication, LiDAR data management, the existing 2D visualization, and the new Jzy3d 3D visualization.

![Story #135 UML](3100-requirements/docs/135-visualize-lidar-in-3D/Spring02.png)

