package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

//import java.awt.Event;

import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Distance;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Constants.StateGUI;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Direction;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot.Turn;
//import falstad.Constants.StateGUI;
//import falstad.Robot.Direction;
//import falstad.Robot.Turn;
/**
 * 
 * @author Yunwoo Noh 
 *
 */
public class ManualDriver implements RobotDriver, Runnable {
	public BasicRobot robot;
	public int pathLength;
	public float energyConsumption;
	public float initialEnergy;
	
	private int width;
	private int height;
	private Distance dist;

    private int key;
    private Thread thread;

	
	
	public ManualDriver(Robot drivingRobot){
		robot = (BasicRobot) drivingRobot;
		pathLength = 0;
		energyConsumption = 0;
		//initialEnergy = robot.battery;
	}
	
	public ManualDriver(){
		robot = new BasicRobot();
		pathLength = 0;
		energyConsumption = 0;
	}
	
	/*
	public boolean keyDown(int key) {
		assert(robot != null);
		return (robot.keyDown(key));
	}
	 */
	@Override
	public void setRobot(Robot r) {
		robot = (BasicRobot) r;
		//initialEnergy = robot.battery;
	}

	@Override
	public void setDimensions(int width, int height) {
		assert(width >= 0 && height >= 0);
		this.width = width;
		this.height = height;
	}

	@Override
	public void setDistance(Distance distance) {
		assert(null != distance);
		dist = distance;
		
	}

	@Override
	public boolean drive2Exit(int key) throws Exception {

        this.key = key;
        thread = new Thread(this);
        thread.start();


        //robot.keyDown(key);
		return true;
	}

    public void run(){
        try{
            robot.keyDown(key);
            thread.sleep(20);
        }catch(InterruptedException e){}
    }

	@Override
	public float getEnergyConsumption() {
		return 2500 - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return robot.pathLength;
	}
	
	public BasicRobot getRobot(){
		return robot;
	}


    //////////////////////////////Methods for Android //////////////////////////////
    // but do nothing
    @Override
    public void pause(){}

    @Override
    public void resume(){}

    @Override
    public void driverSetting(){}
	
	
}
