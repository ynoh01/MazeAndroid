package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

import java.util.Random;

//import falstad.Robot.Direction;
//import falstad.Robot.Turn;
//import generation.CardinalDirection;
//import generation.Distance;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Direction;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Turn;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.CardinalDirection;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Distance;

/**
 * Pledge is a refined WallFollower algorithm. 
 * Fist it picks a random direction and set it as main direction. The robot generally wants to go toward main direction unless there's a wall 
 * Once the robot faces a wall, it uses the same algorithm as WallFollower to find the exit. 
 * Main direction is rotateCount = 0, Left rotation is -1, right rotation is +1, and turn around is -2. 
 * 
 * @author Yunwoo Noh 
 * 
 *
 */
public class Pledge implements RobotDriver, Runnable{
	private BasicRobot robot;
	private int width;
	private int height;
	private Distance dist;
	private int pathLength;
	private int rotateCount;
    private boolean pause;

    private CardinalDirection mainDir = CardinalDirection.East;

    private Thread thread;




    //Constructor
	public Pledge() {
		//initialize the fields 
		robot = new BasicRobot();
		//width = 0;
		//height = 0;
		pathLength = 0;
	}
	
	public Pledge(BasicRobot r){
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

    private void setMainDir(){
        //generate random direction
        Random random = new Random();

        int randomDir = random.nextInt(4);
        switch(randomDir){
            case 0:
                mainDir = CardinalDirection.East;
                break;
            case 1:
                mainDir = CardinalDirection.West;
                break;
            case 2:
                mainDir = CardinalDirection.North;
                break;
            case 3:
                mainDir = CardinalDirection.South;
                break;
        }
    }


	@Override
	public boolean drive2Exit(int key) throws Exception {
        thread = new Thread(this);
        thread.start();

		return true;
	}
    public void run(){
        try{
            switch(mainDir){
                case East:
                    rotateCount = 0;
                    break;
                case West:
                    rotateCount = -2;
                    break;
                case North:
                    rotateCount = -1;
                    break;
                case South:
                    rotateCount = 1;
                    break;
            }


            while(!robot.isAtExit() && !pause){
                if(rotateCount == 0){
                    int dist = robot.distanceToObstacle(Direction.FORWARD);
                    robot.move(dist, false);
                    pathLength++;
                    robot.rotate(Turn.RIGHT);
                    rotateCount++;
                }
                else{
                    findWall();
                    if(robot.distanceToObstacle(Direction.LEFT) != 0){
                        robot.rotate(Turn.LEFT);
                        rotateCount--;
                        robot.move(1, false);
                        pathLength++;
                    }
                    while(!robot.isAtExit() && !pause){
                        if(robot.distanceToObstacle(Direction.LEFT) != 0){
                            robot.rotate(Turn.LEFT);
                            rotateCount--;
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
                                rotateCount++;
                            }
                        }
                        Thread.sleep(20);
                        if(pause){
                            Thread.interrupted();
                        }

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
	 * Called when there is no wall around the robot. 
	 * If there is a wall on the left, it means the robot is following the wall, so returns nothing. 
	 * If there is a wall on the front, it rotates right so now the robot is following the wall on the left. 
	 */
	 private void findWall(){
		    if (robot.distanceToObstacle(Direction.LEFT) != 0){
	    		while(robot.distanceToObstacle(Direction.FORWARD) != 0){
	    			robot.move(1, false);
	    			pathLength++;
	    		}
	    		robot.rotate(Turn.RIGHT);
	    		rotateCount++;
	    	}
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
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case North:
					robot.rotate(Turn.LEFT);
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case South: 
					robot.rotate(Turn.RIGHT);
					rotateCount++;
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
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case West:
					robot.move(1, false);
					pathLength++;
					break;
				case North:
					robot.rotate(Turn.RIGHT);
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case South: 
					robot.rotate(Turn.LEFT);
					rotateCount++;
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
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case West:
					robot.rotate(Turn.LEFT);
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case North:
					robot.move(1, false);
					pathLength++;
					break;
				case South: 
					robot.rotate(Turn.AROUND);
					rotateCount++;
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
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case West:
					robot.rotate(Turn.RIGHT);
					rotateCount++;
					robot.move(1, false);
					pathLength++;
					break;
				case North:
					robot.rotate(Turn.AROUND);
					rotateCount++;
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
    public void driverSetting(){
        setMainDir();
    }


}
