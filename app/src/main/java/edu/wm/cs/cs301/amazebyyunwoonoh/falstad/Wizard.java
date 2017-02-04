package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

//import falstad.Robot.Direction;
//import falstad.Robot.Turn;
//import generation.CardinalDirection;
//import generation.Distance;

import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Direction;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Turn;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.CardinalDirection;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Distance;

/**
 * Wizard is a cheater who uses the MazeController.getNeighborCloserToExit method to find the exit.  
 * While the robot is not at the exit, driveToNeighbor method the robot to the neighbor slot.
 * Once the robot reaches to the exit, drive2Exit method figure out which direction the exit exists 
 * then make the robot faces the exit and move out. 
 * 
 * @author Yunwoo Noh
 *
 */
public class Wizard implements RobotDriver, Runnable{
	private BasicRobot robot;
	private int width = 0;
	private int height = 0;
	private Distance dist;
	private int pathLength;
	private ManualDriver drive;

    private boolean pause;
    private Thread thread;
	
	
	//Constructor
	public Wizard() {
		robot = new BasicRobot();
		pathLength = 0;
	}
	
	
	public Wizard(Robot robot){
		pathLength = 0;
		setRobot(robot);
	}
	
	
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
	}

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;	
	}
	
	public int[] getDimensions(){
		int[] dimension = new int[2];
		dimension[0] = width;
		dimension[1] = height;
		return dimension;
		
	}

	@Override
	public void setDistance(Distance distance) {
		dist = distance;
	}

	@Override
	public boolean drive2Exit(int key) throws Exception {
		thread = new Thread(this);
        thread.start();

		return true;
	}

    public void run(){
        try{
            //System.out.println("robot initial direction is: " + robot.getCurrentDirection());
            int[] neighbor = new int[2];
            int counter = 0;
            while (!robot.isAtExit() && !pause){
                neighbor[0] = robot.getController().getMazeConfiguration().getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])[0];
                neighbor[1] = robot.getController().getMazeConfiguration().getNeighborCloserToExit(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])[1];

                driveToNeighbor(neighbor[0],neighbor[1]);
                counter++;
                Thread.sleep(20);
            }

            if(pause){
                Thread.interrupted();
            }


            getOut();
        }catch(Exception e){}
    }
	
	/**
	 * Method to get the robot out of the maze. 
	 * This method figure out the exit is located, then make the robot face the exit if it is not already, 
	 * then move it out. 
	 * @throws Exception 
	 */
	public void getOut() throws Exception{		
		if (!robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0]+1, robot.getCurrentPosition()[1])
				&& robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])){
			switch(robot.getCurrentDirection()){
			case East:
				robot.move(1, false);
				pathLength++;
				break;
			case West:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				pathLength++;
				break;
			case North:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				pathLength++;
				break;
			case South: 
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				pathLength++;
				break;		
			}
		}
		if (!robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0]-1, robot.getCurrentPosition()[1])
				&& robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])){
			switch(robot.getCurrentDirection()){
			case East:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				pathLength++;
				break;
			case West:
				robot.move(1, false);
				pathLength++;
				break;
			case North:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				pathLength++;
				break;
			case South: 
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				pathLength++;
				break;		
			}
		}
		
		if (!robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]-1)
				&& robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])){
			switch(robot.getCurrentDirection()){
			case East:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				pathLength++;
				break;
			case West:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				pathLength++;
				break;
			case North:
				robot.move(1, false);
				pathLength++;
				break;
			case South: 
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				pathLength++;
				break;		
			}
		}
		
		if (!robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]+1)
				&& robot.getController().getMazeConfiguration().isValidPosition(robot.getCurrentPosition()[0], robot.getCurrentPosition()[1])){
			switch(robot.getCurrentDirection()){
			case East:
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				pathLength++;
				break;
			case West:
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
				pathLength++;
				break;
			case North:
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				pathLength++;
				break;
			case South: 
				robot.move(1, false);
				pathLength++;
				break;		
			}
		}
		
	}
		
	
		
	
	/**
	 * driveToNeighbor takes x and y coordinates as parameters. 
	 * First it find the current direction the robot is facing 
	 * then for each East, West, North, South direction case, finds the neighbor slot 
	 * and make the robot face the neighbor slot then move to there. 
	 * @param nx x coordinate of neighbor slot
	 * @param ny y coordinate of neighbor slot
	 * @throws Exception 
	 */
	
	// left is right!!! (the maze is upside-down) 
	private void driveToNeighbor(int nx, int ny) throws Exception{
		
		CardinalDirection direction = robot.getCurrentDirection();
		switch(direction){
		
		case East:
			if (nx > robot.getCurrentPosition()[0]){
				
				robot.move(1, false);
				pathLength++;
			}
			else {
				if(nx == robot.getCurrentPosition()[0]){
					if (ny > robot.getCurrentPosition()[1]){
						robot.rotate(Turn.LEFT);
						robot.move(1, false);
						pathLength++;

					}
					else{
						robot.rotate(Turn.RIGHT);
						robot.move(1, false);
						pathLength++;

					}
				}
				else{
					robot.rotate(Turn.AROUND);
					robot.move(1, false);
					pathLength++;

				}
			}
			break;
		case West:
			if (nx < robot.getCurrentPosition()[0]){
				robot.move(1, false);
				pathLength++;

			}
			else {
				if(nx == robot.getCurrentPosition()[0]){
					if (ny > robot.getCurrentPosition()[1]){
						robot.rotate(Turn.RIGHT);
						robot.move(1, false);
						pathLength++;

					}
					else{
						robot.rotate(Turn.LEFT);
						robot.move(1, false);
						pathLength++;

					}
				}
				else{
					robot.rotate(Turn.AROUND);
					robot.move(1, false);
					pathLength++;

				}
				
			}
			break;
		case North:
			if (ny < robot.getCurrentPosition()[1]){
				robot.move(1, false);
				pathLength++;

			}
			else {
				if(ny == robot.getCurrentPosition()[1]){
					if (nx > robot.getCurrentPosition()[0]){
						robot.rotate(Turn.LEFT);
						robot.move(1, false);
						pathLength++;

					}
					else{
						robot.rotate(Turn.RIGHT);
						robot.move(1, false);
						pathLength++;

					}
				}
				else{
					
					robot.rotate(Turn.AROUND);
					robot.move(1, false);
					pathLength++;

				}
				
			}
			break;
		case South:
			if (ny > robot.getCurrentPosition()[1]){
				robot.move(1, false);
				pathLength++;

			}
			else {
				if(ny == robot.getCurrentPosition()[1]){
					if (nx > robot.getCurrentPosition()[0]){
						robot.rotate(Turn.RIGHT);
						robot.move(1, false);
						pathLength++;

					}
					else{
						robot.rotate(Turn.LEFT);
						robot.move(1, false);
						pathLength++;

					}
				}
				else{
				
					robot.rotate(Turn.AROUND);
					robot.move(1, false);
					pathLength++;

				}
				
			}
			break;
		}
	}
	
	
	
	
	@Override
	public float getEnergyConsumption() {
		float energyLeft = robot.getBatteryLevel();
		return 2500 - energyLeft;
	}

	@Override
	public int getPathLength() {
		return pathLength;
	}
	
	public BasicRobot getRobot(){
		return robot;
	}


	public Object getDistance() {
		return dist;
	}

    //////////////////////////////Methods for Android //////////////////////////////
    @Override
    public void pause(){
        pause = true;
    }

    @Override
    public void resume(){
        pause = false;
        try{
            drive2Exit(0);
        }catch(Exception e){}
    }

    @Override
    public void driverSetting(){}
	
}