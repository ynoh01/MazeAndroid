package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

//import falstad.Constants.StateGUI;
//import generation.CardinalDirection;

import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Constants.StateGUI;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.CardinalDirection;


public class BasicRobot implements Robot {
	private MazeController controller;	
	float battery = 2500;
	private boolean stopped = false;
	int pathLength;
	boolean wall;
	
	public BasicRobot() {
		controller = new MazeController();
	}
	
	/**
	 * constructor with MazeController parameter
	 * @param //maze  the MazeController object for the robot to control.
	 */
	
	public BasicRobot(MazeController controller){
		setMaze(controller);
	}
	/**
	 * rotate the robot. If the robot has the battery level == 0, the robot does not rotate 
	 * Adjust battery level after each rotation.
	 * @param turn turning direction 
	 */
	@Override
	public void rotate(Turn turn) {
		if (!stopped && battery > 0) {
			switch(turn) {
			case LEFT:
				controller.keyDown('h');
				battery = battery - 3;
                controller.updateEnergy(battery);
				break;
			case RIGHT:
				controller.keyDown('l');
				battery = battery - 3;
                controller.updateEnergy(battery);
                break;
			case AROUND:
				controller.keyDown('h');
				controller.keyDown('h');
				battery = battery - 2 * 3;
                controller.updateEnergy(battery);
                break;
			}
		}
	}
	/**
	 * move the robot by distance. Stop if the battery level reaches 0.
	 * @param distance distance to move 
	 * @param manual true if manually moved. False otherwise.
	 */
	
	@Override
	public void move (int distance, boolean manual) {
		if (manual) {
			int move = 0;
			while (move < distance && battery > 0 && !stopped) {
				if (controller.getMazeConfiguration().hasWall(controller.getCurrentPosition()[0], controller.getCurrentPosition()[1], controller.getCurrentDirection())) {
					move++;
					wall = true;
				}else{
					wall = false;
					controller.keyDown('k');
					move++;
                    pathLength++;
                    controller.updatePathLength(pathLength);
					battery = battery - 5;
                    controller.updateEnergy(battery);
                }
			}
		}else{
			int move = 0;
			while (move < distance && battery > 0 && !stopped) {
				if (controller.getMazeConfiguration().hasWall(controller.getCurrentPosition()[0], controller.getCurrentPosition()[1], controller.getCurrentDirection())) {
					move++;
					wall = true;
				}else{
					wall = false;
					controller.keyDown('k');
					move++;
                    pathLength++;
                    controller.updatePathLength(pathLength);
                    battery = battery - 5;
                    controller.updateEnergy(battery);
                }
			}
		}
		
	}
	
	/**
	 * returns current position in int array. 
	 * @return int[2] of {px, py} 
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		return controller.getCurrentPosition();
	}
	
	/**
	 * set MazeController object to setMaze
	 * @param maze MazeController Object 
	 */
	@Override
	public void setMaze(MazeController maze) {
		//maze shouldn't be null object 
		assert maze != null; 
		controller = maze;
	}
	
	

	/**
	 * if the robot is at the exit position, returns true, false otherwise. 
	 * @return boolean condition of robot at the exit position 
	 */

	@Override
	public boolean isAtExit() {
		return controller.getMazeConfiguration().getMazecells().isExitPosition(controller.getCurrentPosition()[0], controller.getCurrentPosition()[1]);
	}

	/**
	 * searches exit toward the given direction. 
	 * Using distanceToObstacle method, return boolean condition. 
	 * True if robot can reach the exit, false otherwise. 
	 * decrease battery level accordingly 
	 * @param direction direction to search for exit. 
	 * @return boolean condition of robot being able to see the exit at the given direction 
	 */
	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {
		battery = battery - 1;
		return distanceToObstacle(direction) == Integer.MAX_VALUE;
	}
	/**
	 * determines if the robot is inside a room 
	 * @return boolean condition for robot being in the room 
	 */

	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		return controller.getMazeConfiguration().getMazecells().isInRoom(controller.getCurrentPosition()[0],controller.getCurrentPosition()[1]);
	}
	
	/**
	 * check robot's sensor capability. 
	 * @return true if the robot has room sensor 
	 */

	@Override
	public boolean hasRoomSensor() {
		return true;
	}

	/**
	 * check current direction of the robot 
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		return controller.getCurrentDirection();
	}

	/**
	 * get battery level and return it 
	 */
	@Override
	public float getBatteryLevel() {
		return battery;
	}
	
	/**
	 * set battery level 
	 */

	@Override
	public void setBatteryLevel(float level) {
		battery = level;
	}
	
	/**
	 * get energy for for a full rotation
	 */

	@Override
	public float getEnergyForFullRotation() {
		return 4 * 3;
	}

	/**
	 * get energy for stepping one step forward 
	 */
	@Override
	public float getEnergyForStepForward() {
		return 5;
	}

	/**
	 * check if the robot stopped 
	 * @return boolean true if the robot is blocked 
	 */
	@Override
	public boolean hasStopped() {
		return stopped;
	}
	
	/**
	 * calculate the distance to an obstacle 
	 * depends on robot's point of view 
	 * @param direction direction to search for the exit. 
	 * @return int distance to an obstacle 
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		CardinalDirection viewDir = robotViewDirection(direction);
		int px = controller.getCurrentPosition()[0];
		int py = controller.getCurrentPosition()[1];
		int step = 0;
		boolean open = !controller.getMazeConfiguration().hasWall(px, py, viewDir);
		
		
		switch (viewDir) {
		case East:
			//move with distance 1 until it faces an obstacle. 
			//If it move to invalid position, then get out of the maze 
			//apply this for each direction 
			while(open){
				if (px+step == controller.getMazeConfiguration().getWidth()-1) {
					open = false;
				}
				else if(!controller.getMazeConfiguration().isValidPosition(px+step, py)){
					step = Integer.MAX_VALUE;
					open = false;
				}else{
					step++;
					open = !controller.getMazeConfiguration().hasWall(px+step, py, viewDir); 
				}
			}
			break;
		case West:
			while(open){
				if (px-step == 0) {
					open = false;
				}
				else if(!controller.getMazeConfiguration().isValidPosition(px-step, py)){
					step = Integer.MAX_VALUE;
					open = false;
				}else{
					step++;
					open = !controller.getMazeConfiguration().hasWall(px-step, py, viewDir);
				}
			}
			break;
		case North:
			while(open){
				if (py-step == 0) {
					open = false;
				}
				else if(!controller.getMazeConfiguration().isValidPosition(px, py-step)){
					step = Integer.MAX_VALUE;
					open = false;
				}else{
					step++;
					open = !controller.getMazeConfiguration().hasWall(px, py-step, viewDir);
				}
			}
			break;
		case South:
			while(open){
				if (py+step == controller.getMazeConfiguration().getHeight()-1) {
					open = false;
				}
				else if(!controller.getMazeConfiguration().isValidPosition(px, py+step)){
					step = Integer.MAX_VALUE;
					open = false;
				}else{
					step++;
					open = !controller.getMazeConfiguration().hasWall(px, py+step, viewDir);
				}
			}
			break;
		}
		//consume 1 energy each time 
		battery = battery - 1;
		return step;
	}
	
	/**
	 * check if the robot has a distance sensor 
	 * @return true if the robot has a distance sensor, false otherwise. 
	 */
	@Override
	public boolean hasDistanceSensor(Direction direction) {
		return true;
	}
	
	
	/**
	 * This method calculates a searching direction by superimpose CardinalDirection 
	 * East, West, North, South and Direction of Left, Right, Forward, and Backward.
	 * 
	 * @param direction
	 * @return viewDir
	 */
	private CardinalDirection robotViewDirection(Direction direction) {
		CardinalDirection viewDir = controller.getCurrentDirection();
		switch(direction) {
			case LEFT:
				viewDir = controller.getCurrentDirection().rotateClockwise();
				break;
			case RIGHT:
				viewDir = controller.getCurrentDirection().oppositeDirection().rotateClockwise();
				break;
			case FORWARD:
				viewDir = controller.getCurrentDirection();
				break;
			case BACKWARD:
				viewDir = controller.getCurrentDirection().oppositeDirection();
				break;
		}
		return viewDir;
	}

	/**
	 * returns the controller.
	 * @return controller
	 */
	public MazeController getController() {
		return controller;
	}
	
	/**
	 * keyDown for simple key listener -> manualdriver -> basicRobot.
	 * @param key
	 */
	public void keyDown(int key){
		controller.keyDown(key);
		pathLength++;
	}
	
}
