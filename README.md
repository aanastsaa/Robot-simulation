# Robot-simulation

This is a team project for my university. The program is written in Java, JavaFX was used for the GUI. At startup, a window opens on the monitor where you can create an environment for robots, robots themselves (there are automatic and other robots that can be controlled), obstacles for robots.

Specification of essential requirements of projects:
 Any number of robots can be placed in a bounded environment of a rectangular floor plan. Robot
it has a body of circular ground plan and is capable of moving forward at a given speed, turning at a given angle
and collision detection.
 There may be static obstacles in the environment. Obstacles are formed by any number of
squares of a given size on the selected coordinates.
 The initial placement of obstacles and robots (including their orientation and other parameters) can be
set interactively in the GUI, it can be saved to a file in a readable and manually editable text
form and load.
 According to the method of Control, 2 types of robots can be distinguished:
1. The autonomous robot moves in a free environment in a straightforward manner and avoids collisions by
when an obstacle is detected at a given distance (robot parameter), it rotates by a given angle (robot parameter
robot) in a given direction (robot parameter) and continues in the same way.
2. The remote-controlled robot moves according to the operator's instructions between the states of no movement, movement
forward, counterclockwise rotation, clockwise rotation.
If it detects an obstacle, it stops its forward movement.
 The GUI allows you to control the simulator and visualize the simulation. Displays a map of the environment
with obstacles and robots and contains controls for remote control of the selected robot.
 The simulation can be started from the default state, paused, resumed or played backwards.
 The simulation works with simplified physics, working with discrete time, continuous 2D
space and uniform rectilinear motion given speed and rotation uniform
angular velocity (does not take into account mass, inertia, acceleration, etc., only
distance, speed, angular velocity and time). 

Tests:
    
    mvn clean compile
    
    mvn test

Application:
    
    mvn clean compile
    
    mvn javafx:run
