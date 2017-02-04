package edu.wm.cs.cs301.amazebyyunwoonoh.falstad;

import java.util.ArrayList;

/**
 * Uses Eller's algorithm to generate maze pathways.
 * Here, no loop is allowed, as no isolated cells.
 * Also, there is one starting point and one exit point.
 * @author Yoongbok Lee, Yunwoo Noh
 *
 */

public class MazeBuilderEller extends MazeBuilder{
	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}
	
	public MazeBuilderEller(boolean det) {
		super(det);
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}

	/**
	 * This method generates pathways into the maze by using Eller's algorithm to delete walls to connect cells.
	 * The cells are the nodes of the graph and the cell set. a set represents a connected group of cells.
	 * add row to sets.
	 * connect horizontal cells randomly.
	 * connect vertical cells randomly.
	 * Important to keep all cells kept in cellSet, for the validity of algorithm.
	 * multiple sets should eventually gather into one big set for every cell to be reachable from the starting position, at the last row.
	 */
	@Override
	protected void generatePathways() {
		// 3d array list cellSet to manage sets of cells.
		ArrayList<ArrayList<ArrayList<Integer>>> cellSet = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for (int i = 0; i < height; i++){
			//Eller's algorithm
			//1. add row to set.
			addRowToSet(cellSet,i);
			//2. randomly connect horizontal cells.
			randomHorizontalConnect(cellSet, i);
			if(i == height-1) break;
			//3. randomly connect to cells on the next(down) row.
			randomVerticalConnect(cellSet, i);
		}
		//at last row, we need to connect all remaining sets into one, so that all cells are reachable.
		connectLastRow(cellSet);
	}
	
	/**
	 * add new row to the set. if the cell coordinate is already included in one of the sets(by randomVerticalConnect or during randomHorizontalConnect)
	 * do not include the cell again.
	 * @param cellSet set of cells as their coordinates
	 * @param i current row index
	 */
	private void addRowToSet(ArrayList<ArrayList<ArrayList<Integer>>> cellSet, int i){
		for (int j = 0; j < width; j++){
			ArrayList<Integer> currentCoordinate = new ArrayList<Integer>();
			currentCoordinate.add(i);
			currentCoordinate.add(j);
			int found = findCellFromSet(cellSet, i, j);
			if (found == -1){
				ArrayList<ArrayList<Integer>> newSet = new ArrayList<ArrayList<Integer>>();
				newSet.add(currentCoordinate);
				cellSet.add(newSet);
			}
		}
	}
	
	/**
	 * First check of the cell at the position has wall on the bottom and to the right. If there already doesn't exist a wall(possibly a room etc.)
	 * include right or down cell coordinate to its set. Otherwise, randomly remove wall horizontally and join sets of connected cells.
	 * @param cellSet set of cells as their coordinates
	 * @param i current row index
	 */
	private void randomHorizontalConnect(ArrayList<ArrayList<ArrayList<Integer>>> cellSet, int i){
		
		for (int k = 0; k < width-1; k++){
			if(cells.hasNoWall(k, i, CardinalDirection.East)){
				if(findCellFromSet(cellSet,i,k) != findCellFromSet(cellSet,i,k+1)){
					cellSet.get(findCellFromSet(cellSet,i,k)).addAll(cellSet.remove(findCellFromSet(cellSet,i,k+1)));
				}
			}
			if(cells.hasNoWall(k, i, CardinalDirection.South)){
				if(findCellFromSet(cellSet,i,k) != findCellFromSet(cellSet,i+1,k)){
					ArrayList<Integer> currentCoordinate = new ArrayList<Integer>();
					currentCoordinate.add(i+1);
					currentCoordinate.add(k);
					cellSet.get(findCellFromSet(cellSet,i,k)).add(currentCoordinate);
				}
			}
			int concattoRight = random.nextInt();
			if (concattoRight % 2 == 0){
				Wall wall = new Wall(k, i, CardinalDirection.East);
				if (cells.canGo(wall)){
					if(findCellFromSet(cellSet,i,k) != findCellFromSet(cellSet,i,k+1)){
						cells.deleteWall(wall);
						cellSet.get(findCellFromSet(cellSet,i,k)).addAll(cellSet.remove(findCellFromSet(cellSet,i,k+1)));
					}
				}
			}
		}
	}
	
	/**
	 * randomly connect current cells downwards. In order to keep all cells conected
	 * if a set doesn't make it through to next row, it is guaranteed for the rightmost cell of the set connects downwards
	 * to keep the set connected with other cells.
	 * @param cellSet set of cells as their coordinates
	 * @param i current row index
	 */
	private void randomVerticalConnect(ArrayList<ArrayList<ArrayList<Integer>>> cellSet, int i){
		
		boolean[] movedDown = new boolean[cellSet.size()];
		for (boolean entry: movedDown){
			entry = false;
		}
		for(int l = 0; l < width; l++){
			
			int connectDown = random.nextIntWithinInterval(0,3);
			if (connectDown % 2 == 0){
				Wall wall = new Wall(l, i, CardinalDirection.South);
				cells.deleteWall(wall);
				ArrayList<Integer> currentCoordinate = new ArrayList<Integer>();
				currentCoordinate.add(i+1);
				currentCoordinate.add(l);
				cellSet.get(findCellFromSet(cellSet,i,l)).add(currentCoordinate);
				movedDown[findCellFromSet(cellSet,i,l)] = true;
			} else if (cells.hasWall(l, i, CardinalDirection.East) && movedDown[findCellFromSet(cellSet,i,l)] == false){
				Wall wall = new Wall(l, i, CardinalDirection.South);
				cells.deleteWall(wall);
				ArrayList<Integer> currentCoordinate = new ArrayList<Integer>();
				currentCoordinate.add(i+1);
				currentCoordinate.add(l);
				cellSet.get(findCellFromSet(cellSet,i,l)).add(currentCoordinate);
				movedDown[findCellFromSet(cellSet,i,l)] = true;
			}
		}
	}
	
	/**
	 * Last opportunity to connect separated sets.
	 * if this method fails, sometimes it might be impossible to solve the maze., as exit cell and start cell might be separated.
	 * @param cellSet set of cells as their coordinates
	 */
	private void connectLastRow(ArrayList<ArrayList<ArrayList<Integer>>> cellSet){
		int lastRow = height-1;
		for (int l = 0; l < width-1; l++){
			Wall wall = new Wall(l, lastRow, CardinalDirection.East);
			if (cells.canGo(wall)){
				if(findCellFromSet(cellSet,lastRow,l) != findCellFromSet(cellSet,lastRow,l+1)){
					cells.deleteWall(wall);
					cellSet.get(findCellFromSet(cellSet,lastRow,l)).addAll(cellSet.remove(findCellFromSet(cellSet,lastRow,l+1)));
				}
			}
		}
	}
	/**
	 * find coordinate m,n from cell set, and return the set index if found. return -1 otherwise.
	 * Note that m,n stands for the coordinate of the cells finding, not actual index on the cell set.
	 * Also, as Cells.cells operate in column major notation, m stands for y coordinate and n stands for x coordinate.
	 * @param cellSet set of cells as their coordinates.
	 * @param m finding cell y coordinate
	 * @param n finding cell x coordinate
	 * @return index of set where [m,n] is found
	 */
	private int findCellFromSet(ArrayList<ArrayList<ArrayList<Integer>>> cellSet, int m, int n){
		for (int i = 0; i < cellSet.size(); i++) {
			for (int j = 0; j < cellSet.get(i).size(); j++){
				if( cellSet.get(i).get(j).get(0).equals(m) && cellSet.get(i).get(j).get(1).equals(n)){
					return i;
				}
			}
		}
		return -1;
	}
}
