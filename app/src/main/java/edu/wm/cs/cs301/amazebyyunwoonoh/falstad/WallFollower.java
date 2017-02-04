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
 * WallFollower is a classic solution to Maze: keep following the left wall. 
 * Using distanceToObstacle method, the robot has a sensor at the front and on the left. 
 * There are 4 possible cases that the robot can face on its journey to find the exit. 
 * Wall on the left only, Wall on the front and the left, Wall on the front only, No walls 
 * In the first two cases, we are following the wall, though right rotation involves in the second case, 
 * The third case, the robot needs to rotate right. 
 * When there's no wall, WallFollower Algorithm calls findWall Method. 
 * @author Yunwoo Noh 
 *
 */

public class WallFollower implements RobotDriver, Runnable{
	private BasicRobot robot;
	private int width = 0;
	private int height = 0;
	private Distance dist;
	private int pathLength;
    private boolean pause;

    private Thread thread;
	
	
	//Constructor 
	public WallFollower(){
		//initialize the fields
		robot = new BasicRobot();
		//width = 0;
		//height = 0; 
		pathLength = 0;
	}
	
	public WallFollower(BasicRobot r){
		setRobot(r);
		pathLength = 0;
	}

	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
		
	}
	
	

	@Override
	public void setDimensions(int width, int height) {
		this.width = width;
		
	}
	/**
	 * get dimension
	 * return integer array of dimension 
	 * @return dimension
	 */
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
	/**
	 * get distance
	 * @return distance object 
	 */
	public Object getDistance() {
		return dist;
	}

	@Override
	public boolean drive2Exit(int key) throws Exception {
		thread = new Thread(this);
        thread.start();

		return true;
	}

    public void run(){
        try{
            //distance 0 means, there's a wall
            findWall();
            if(robot.distanceToObstacle(Direction.LEFT) != 0){
                robot.rotate(Turn.LEFT);
                robot.move(1, false);
                pathLength++;
            }
            while(!robot.isAtExit() && !pause){
                if(robot.distanceToObstacle(Direction.LEFT) != 0){
                    robot.rotate(Turn.LEFT);
                    robot.move(1, false);
                    pathLength++;
                }
                else{
                    if(robot.distanceToObstacle(Direction.FORWARD) != 0){
                        robot.move(1, false);
                        pathLength++;
                    }
                    else{
                        robot.rotate(Turn.RIGHT);
                    }
                }
                Thread.sleep(20);
                if(pause){
                    Thread.interrupted();
                }
            }


            getOut();
        }catch(Exception e){}
    }


	/**
	 * get the robot out of the maze. 
	 * It figures out the exit is located, then make the robot face the exit if it is not already, 
	 * then move it out. 
	 * @throws Exception 
	 */
	
	//getout don't need  to worry about the left-right
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
	 * findWall method when there is no wall around the robot. 
	 * If there is a wall on the left, it means the robot is following the wall, so returns nothing. 
	 * If there is a wall on the front, it rotates right so now the robot is following the wall on the left. 
	 */
    public void findWall(){
    	
    	if (robot.distanceToObstacle(Direction.LEFT) != 0){
    		while(robot.distanceToObstacle(Direction.FORWARD) != 0){
    			robot.move(1, false);
    		}
    		robot.rotate(Turn.RIGHT);
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
	/**
	 * get robot 
	 * @return robot 
	 */

	public BasicRobot getRobot() {
		return robot;
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
