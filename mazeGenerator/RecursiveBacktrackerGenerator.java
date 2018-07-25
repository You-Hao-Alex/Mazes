package mazeGenerator;
/**@author Chih-Yuan Chen & You Hao
 * 
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import maze.Cell;
import maze.Maze;
import static maze.Maze.HEX;
import static maze.Maze.NUM_DIR;

public class RecursiveBacktrackerGenerator implements MazeGenerator {
	private Maze AA;
	private Cell Temp;
    private boolean VoN[][];
    private HashSet<Cell> HexV;
    private ArrayList<Cell> mValidCells;
    private Random random = new Random(System.currentTimeMillis());
    
    //Randomly pick a starting cell
    private void Start() {
    	if (AA.type == HEX) {
    		Temp = mValidCells.get(random.nextInt(mValidCells.size()));
    		} 
    	else {
    		int randRow = random.nextInt(AA.sizeR);
    		int randCol = random.nextInt(AA.sizeC);
    		Temp = AA.map[randRow][randCol];
    		}
    	}
    
    //check if a cell is raw
    private boolean raw (Cell a) {
    	if (AA.type == HEX) {
    		return !HexV.contains(a);
    		} 
    	else {
    		return !VoN[a.r][a.c];
    		}
    }
    
    //Check if the cell in the maze
    private boolean Check (int row, int column) {
    	if (AA.type == HEX) {
    		return row >= 0 && row < AA.sizeR && column >= (row + 1) / 2 && column < AA.sizeC + (row + 1) / 2;
    		} 
    	else {
    		return row >= 0 && row < AA.sizeR && column >= 0 && column < AA.sizeC;
    		}
    	}
    private boolean Check(Cell cell) {
    		return cell != null && Check(cell.r, cell.c);
    }
    
    @Override
	public void generateMaze(Maze maze) {
    		// TODO Auto-generated method stub
		AA = maze;
		VoN = new boolean[maze.sizeR][maze.sizeC];
		HexV = new HashSet<>();
		int rest;
		boolean keep = true;
		int Nonei;
		Stack<Cell> past = new Stack<>();
		ArrayList<Cell> finish = new ArrayList<>();
		if (maze.type == Maze.NORMAL) {
			rest = maze.sizeR * maze.sizeC;
			//Randomly pick a starting cell
			Start();
			//Starting cell has been visited
			VoN[Temp.r][Temp.c] = true;
			rest--;
			//When there are no more unvisited neighbours for all cells, then every cell would have been visited and we have generated a perfect maze
			while (rest > 0) {
				//Continue this process until we reach a cell that has no unvisited neighbours
				while (keep) {
					//Pick a random unvisited neighbouring cell and move to that neighbour
					ArrayList<Integer> unvisit = new ArrayList<>();
					for (int i = 0; i < NUM_DIR; i++) {
						Cell tempnei = Temp.neigh[i];
						if ((Check(tempnei)) && (raw(tempnei))) {
							unvisit.add(i);
						}
					}
					if (unvisit.size() > 0) {
						Nonei = unvisit.get(random.nextInt(unvisit.size()));
						Temp.wall[Nonei].present = false;
						past.add(Temp);
						Temp = Temp.neigh[Nonei];
						VoN[Temp.r][Temp.c] = true;
						rest--;
					}
					else {
						keep = false;
					}
				}
				if (past.size() > 0) {
					Temp = past.pop();
				} 
				keep = true;
			}
		} 
		else if (maze.type == Maze.HEX) {
			mValidCells = new ArrayList<>();
			for (int i = 0; i < maze.sizeR; i++) {
				for (int j = (i + 1) / 2; j < maze.sizeC + (i + 1) / 2; j++) {
					if (!Check(i, j))
						continue;
					mValidCells.add(AA.map[i][j]);
				}
			}
			//Number of rest cells to visit
			rest = mValidCells.size();
			Start();
			HexV.add(Temp);
			rest--;
			//When there are no more unvisited neighbours for all cells, then every cell would have been visited and we have generated a perfect maze
			while (rest > 0) {
				//Continue this process until we reach a cell that has no unvisited neighbours
				while (keep) {
					//Pick a random unvisited neighbouring cell and move to that neighbour
					ArrayList<Integer> unvisit = new ArrayList<>();
					for (int i = 0; i < NUM_DIR; i++) {
						Cell tempnei = Temp.neigh[i];
						if ((Check(tempnei)) && (raw(tempnei))) {
							unvisit.add(i);
						}
					}
					if (unvisit.size() > 0) {
						Nonei = unvisit.get(random.nextInt(unvisit.size()));
						Temp.wall[Nonei].present = false;
						past.add(Temp);
						Temp = Temp.neigh[Nonei];
						HexV.add(Temp);
						rest--;
					}
					else {
						keep = false;
						}
				}
				if (past.size() > 0) {
					Temp = past.pop();
				}
				keep = true;
			}
		}
		else if (maze.type == Maze.TUNNEL) {
			rest = maze.sizeR * maze.sizeC;
			//Randomly pick a starting cell
			Start();
			VoN[Temp.r][Temp.c] = true;
			rest--;
			//When there are no more unvisited neighbours for all cells, then every cell would have been visited and we have generated a perfect maze
			while (rest > 0) {
				//Continue this process until we reach a cell that has no unvisited neighbours
				while (keep) {
					//Pick a random unvisited neighbouring cell and move to that neighbour
					ArrayList<Integer> unvisit = new ArrayList<>();
					for (int i = 0; i < NUM_DIR; i++) {
						Cell tempnei = Temp.neigh[i];
						if ((Check(tempnei)) && (raw(tempnei)) && (!finish.contains(tempnei))) {
							unvisit.add(i);
						}
					}
					if ((Temp.tunnelTo != null) && (!VoN[Temp.tunnelTo.r][Temp.tunnelTo.c])) {
						unvisit.add(6);
					}
					if (unvisit.size() > 0) {
						int NoneiIndex = random.nextInt(unvisit.size());
						Nonei = unvisit.get(NoneiIndex);
						if (Nonei != 6) {
							if (Temp.tunnelTo != null) {
								finish.add(Temp.tunnelTo);
							}
							Temp.wall[Nonei].present = false;
							past.add(Temp);
							Temp = Temp.neigh[Nonei];
						}
						else {
							past.add(Temp);
							Temp = Temp.tunnelTo;
						}
						//Temporary cell has been visited
						VoN[Temp.r][Temp.c] = true;
						rest--;
					}
					else {
	                        keep = false;
					}
				}
				if (past.size() > 0) {
					Temp = past.pop();
				}
				keep = true;
			}
		}
    } // end of generateMaze()
    
} // end of class RecursiveBacktrackerGenerator
