## GatherEye

Upon receiving an updated input, GatherEye sends message to Broker in form of:
"GAZE,floatX,floatY"
For example:
"GAZE,0.50,0.50"

Latest sent X and Y values are stored to be retrieved if necessary (public getters)

DisplayGUI() -- How class receives input. DisplayGUI() creates instance of GatherEyeGUI() class

SendBrokerCoords() -- Sends broker coordinates in format listed above 

## GatherEyeGUI

GatherEyeGUI class created as temporary front-end for GatherEye. Upon clicking in clicker-panel, GUI updates listed gaze position and triggers GatherEye SendBrokerCoords() method

Class is created by passing a GatherEye instance through its constructor. This keeps an instance of GatherEye which, upon suitable mouse input, triggers method SendBrokerCoords() and updates "Gaze Position" onscreen UI.

