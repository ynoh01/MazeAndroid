# maze

Android App 

User can select difficulty, maze building algorithm, and play mode from the beginning screen. 

There are two play mode: Manual and Robot 

If user select to play manual, the game will simply operates with manual inputs of 4 directions (up, down, left, right) 

If robot mode is selected, there are three different robot mode to select - Wizard, Wall Follower, Pledge 

Wizard is probably the fastest maze solver as it can hop over a wall and obstacle is detected. 

Wall Follower is a classic maze solving algorithm that you put one hand on one side of the wall and 
keep following the wall until you find the exit. 

Pledge is similar to Wall Follower but the robot starts moving toward randomly chosen direction. 
When it faces a room, a circumvent obstacles, one hand is kept along the wall while the angles turned are counted. 
When the robot is facing the original coming direction again, the angle sum becomes 0 and 
the robot leaves the obstacle, continue to moving in the original direction. 
