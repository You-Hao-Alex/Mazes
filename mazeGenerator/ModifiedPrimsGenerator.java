package mazeGenerator;
/**@author Chih-Yuan Chen & You Hao
 * 
 */
import java.util.ArrayList;
import java.util.Random;

import maze.Cell;
import maze.Maze;
import static maze.Maze.HEX;

public class ModifiedPrimsGenerator implements MazeGenerator {
	private Maze a;
    private ArrayList<Cell> aCells = new ArrayList<>();
    //Check if a cell is in the maze
    private boolean Inmaze(int i, int j) {
        if (a.type == HEX) {
            return i >= 0 && i < a.sizeR && j >= (i + 1) / 2 && j < a.sizeC + (i + 1) / 2;
        } else {
            return i >= 0 && i < a.sizeR && j >= 0 && j < a.sizeC;
        }
    }

    private boolean Inmaze(Cell cell) {
        return cell != null && Inmaze(cell.r, cell.c);
    }

    private boolean isAdjacent(Cell firstCell, Cell secondCell) {
        for (int i = 0; i < Maze.NUM_DIR; i++) {
            Cell currentNeighbor = firstCell.neigh[i];
            if (currentNeighbor == secondCell) {
                return true;
            }
        }
        return false;
    }
    
    //Create the maze using modiPrim generator
    @Override
    public void generateMaze(Maze maze) {
    		// TODO Auto-generated method stub
    		Random R = new Random(System.currentTimeMillis());
    		a = maze;
    		int NCells = 0;
    		ArrayList<Cell> A = new ArrayList<>();
    		ArrayList<Cell> B = new ArrayList<>();
    		Cell currentCell = null;
    		if ((maze.type == Maze.NORMAL) || (maze.type == Maze.TUNNEL)) {
    			//Get the number of cells in normal maze
    			NCells = maze.sizeR * maze.sizeC;
    			//Pick a random starting cell
    			int randomR = R.nextInt(maze.sizeR);
    			int randomC = R.nextInt(maze.sizeC);
    			currentCell = maze.map[randomR][randomC];
    			} 
    		else if (maze.type == Maze.HEX) {
    			//List valid hex cells in the maze
    			ArrayList<Cell> validCells = new ArrayList<>();
    			for (int i = 0; i < maze.sizeR; i++) {
    				for (int j = (i + 1) / 2; j < maze.sizeC + (i + 1) / 2; j++) {
    					if (!Inmaze(i, j))
    						continue;
    					validCells.add(a.map[i][j]);
    					}
    				}
    			//Get the number of cells in hex maze
    			NCells = validCells.size();
    			//Pick a random starting cell
    			currentCell = validCells.get(R.nextInt(validCells.size()));
    			}
    		A.add(currentCell);
    		//Loop until every cell has been visited
    		while (A.size() < NCells) {
    			//Add the neighbors of the current cell to B
    			for (int i = 0; i < Maze.NUM_DIR; i++) {
    				Cell currentNeighbor = currentCell.neigh[i];
    				if ((Inmaze(currentNeighbor)) && (!B.contains(currentNeighbor)) && (!A.contains(currentNeighbor))) {
    					B.add(currentNeighbor);
        			}
        		}
    		Cell c = B.get(R.nextInt(B.size()));
    		B.remove(c);
    		aCells.clear();
    		//List all the cells in A that are adjacent to the cell c
    		for (int i = 0; i < A.size(); i++) {
        		Cell cellToCheck = A.get(i);
        		if (isAdjacent(cellToCheck, c)) {
        			aCells.add(cellToCheck);
        			}
        		}
    		//Randomly select a cell b from adjacent cells
    		Cell b = aCells.get(R.nextInt(aCells.size()));
    		//Carve a path between c and b
    		for (int i = 0; i < Maze.NUM_DIR; i++) {
                Cell currentNeighbor = b.neigh[i];
                if ((Inmaze(currentNeighbor)) && (currentNeighbor == c)) {
                    b.wall[i].present = false;
                }
            }
    		//Add cell c to A
    		A.add(c);
    		//Reset current cell to c
    		currentCell = c;
    		}
    	} // end of generateMaze()

} // end of class ModifiedPrimsGenerator
