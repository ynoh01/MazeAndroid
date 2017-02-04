/**
 * 
 */
package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

/**
 * Class encapsulates access to all information that constitutes a maze.
 * 
 * @author pk
 *
 */
public class MazeContainer implements MazeConfiguration {
	// properties of the current maze
	private int width; // width of maze
	private int height; // height of maze
	private Cells mazecells ; // maze as a matrix of cells which keep track of the location of walls
	private Distance mazedists ; // a matrix with distance values for each cell towards the exit
	private BSPNode rootnode ; // a binary tree type search data structure to quickly locate a subset of segments
	// a segment is a continuous sequence of walls in vertical or horizontal direction
	// a subset of segments need to be quickly identified for drawing
	// the BSP tree partitions the set of all segments and provides a binary search tree for the partitions
	private int[] start ;
	/**
	 * 
	 */
	public MazeContainer() {
		// TODO Auto-generated constructor stub
	}

	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}

	/**
	 * Gives the cells.
	 * Warning, returns direct access to internal field.
	 * @return the mazecells
	 */
	public Cells getMazecells() {
		return mazecells;
	}

	/**
	 * @param mazecells the mazecells to set
	 */
	public void setMazecells(Cells mazecells) {
		this.mazecells = mazecells;
	}

	/**
	 * Gives the distance. 
	 * Warning, returns direct access to internal field.
	 * @return the mazedists
	 */
	public Distance getMazedists() {
		return mazedists;
	}

	/**
	 * Sets the distance.
	 * @param mazedists the mazedists to set
	 */
	public void setMazedists(Distance mazedists) {
		this.mazedists = mazedists;
	}

	/**
	 * Gives the rootnode for the tree of BSPnodes.
	 * Warning, returns direct access to internal field.
	 * @return the rootnode
	 */
	public BSPNode getRootnode() {
		return rootnode;
	}

	/**
	 * Sets the root for the tree of BSPnodes
	 * @param rootnode the rootnode to set
	 */
	public void setRootnode(BSPNode rootnode) {
		this.rootnode = rootnode;
	}
	/**
	 * Tells if given (x,y) position is valid, i.e. within legal range of values
	 * @param x is on the horizontal axis 
	 * @param y is on the vertical axis 
	 * @return true if 0 <= x < width and 0 <= y < height, false otherwise
	 */
	public boolean isValidPosition(int x, int y) {
		return ((0 <= x && x < width) && (0 <= y && y < height));
	}
	/**
	 * Gives the number of steps or moves needed to get to the exit.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return number of steps to exit
	 */
	public int getDistanceToExit(int x, int y) {
		return mazedists.getDistance(x, y) ;
	}
	/**
	 * Tells if at position (x,y) and looking into given direction faces a wall.
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return true if there is a wall, false otherwise
	 */
	public boolean hasWall(int x, int y, CardinalDirection dir) {
		return this.mazecells.hasWall(x, y, dir) ;
	}

	/**
	 * Gives a (x',y') neighbor for given (x,y) that is closer to exit
	 * if it exists. 
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 * @return array with neighbor coordinates if neighbor exists, null otherwise
	 */
	public int[] getNeighborCloserToExit(int x, int y) {
		//System.out.println("new getNeighborCloserToExit at: " + x + ", "+ y ) ;
		// find best candidate
		int dnext = getDistanceToExit(x, y) ;
		int[] result = new int[2] ;
		int[] dir;
		for (CardinalDirection cd: CardinalDirection.values()) {
			if (hasWall(x, y, cd)) 
				continue; // there is a wall
			// no wall, let's check the distance
			dir = cd.getDirection();
			int dn = getDistanceToExit(x+dir[0], y+dir[1]);
			if (dn < dnext) {
				// update neighbor position with min distance
				result[0] = x+dir[0] ;
				result[1] = y+dir[1] ;
				dnext = dn ;
			}	
		}
		
		if (getDistanceToExit(x, y) <= dnext)
		{
			System.out.println("ERROR: MazeContainer.getNeighborCloserToExit cannot identify direction towards solution: stuck at: " + x + ", "+ y ) ;
			// TODO: perform proper error handling here
			return null ;
		}
		return result;
	}

	/**
	 * Provides the (x,y) starting position.
	 * The starting position is typically chosen to by furthest away from the exit.
	 * @return the start
	 */
	public int[] getStartingPosition() {
		return start;
	}

	/**
	 * Sets the starting position
	 * @param start the start to set
	 */
	public void setStartingPosition(int[] start) {
		assert (null != start && start.length == 2) : "MazeContainer.start illegal parameter value";
		assert this.isValidPosition(start[0], start[1]) : "Invalid starting position";
		this.start = start;
	}
	/** 
	 * Sets the starting position
	 * @param x is on the horizontal axis, 0 <= x < width
	 * @param y is on the vertical axis, 0 <= y < height
	 */
	public void setStartingPosition(int x, int y) {
		assert this.isValidPosition(x,y) : "Invalid starting position";
		if (null == start)
			start = new int[2] ;
		start[0] = x ;
		start[1] = y ;
	}
}
