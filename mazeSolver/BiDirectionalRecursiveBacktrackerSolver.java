package mazeSolver;
import java.util.HashSet;
import java.util.LinkedList;
import maze.Cell;
import maze.Maze;
import static maze.Maze.HEX;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 * @author Chih-Yuan Chen & You Hao
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {
    private Maze newMaze;
	private boolean meet = false;
	private HashSet<Cell> newCells;
	private HashSet<Cell> leaveCells;
	private int vistedCells =0;
	@Override
	public void solveMaze(Maze maze) {
		// TODO Auto-generated method stub
    newMaze = maze;
    LinkedList<Cell>newQueue= new LinkedList<Cell>();// Enter into BFS
    LinkedList<Cell>endQueue= new LinkedList<Cell>();// Leave BFS
    newCells= new HashSet<Cell>(); //list of cells about entry side cells
    leaveCells=new HashSet<Cell>();//list of cells about exit side cells
    newQueue.add(maze.entrance);//insert the entrance cell via the new Queue
    endQueue.add(maze.exit);//insert the exit cell via the exit queue
    meet=false;
    Cell newCell; //new entry cell in the new queue
    Cell leaveCell;//new exit cell in the end queue
    if(maze.entrance.equals(maze.exit)) {// check the entrance and exit has the same cell and the maze has solved. 
    	meet=true;
    	maze.drawFtPrt(maze.entrance);
    	newCells.add(maze.entrance);
    }
    while(!meet) {
    	newCell =newQueue.removeFirst(); //acquire new cell from new queue
    	maze.drawFtPrt(newCell); //illustrate it
    	newCells.add(newCell);// insert it to the new cells that is visited
    	if(newCell.tunnelTo!=null) {//confirm the cell has a tunnel
    		Cell neighbor = newCell.tunnelTo;
    		
    		if((Check(neighbor))&&(!newQueue.contains(neighbor))&&(!newCells.contains(neighbor))) {
    			newQueue.add(neighbor);
    		}
    		else if((leaveCells.contains(neighbor))||(endQueue.contains(neighbor))) {
    			if(!leaveCells.contains(neighbor)) {
    				maze.drawFtPrt(neighbor);;
    				newCells.add(neighbor);
    			}
    			meet = true;
    			break;
    		}
    		
    	}
    	for(int i=0;i<Maze.NUM_DIR;i++) { //insert neighbor cells to the queue
    		Cell currentNeighbor=newCell.neigh[i];
    		//check the neighbor cell is in the leave cells that is from exit side
    		if(leaveCells.contains(currentNeighbor)&&(!newCell.wall[i].present)) {
    			meet=true;
    			break;
    		}
    		//insert the neighbor cell into the new queue
    		else if((Check(currentNeighbor)&&(!newQueue.contains(currentNeighbor))&&(!newCell.wall[i].present)&&(!newCells.contains(currentNeighbor)))){
    			newQueue.add(currentNeighbor);
    		}
    		//check the end queue has the neighbor cell
    		else if(endQueue.contains(currentNeighbor)&&(!newCell.wall[i].present)) {
    			if(!leaveCells.contains(currentNeighbor)) {
    				newCells.add(currentNeighbor);
    				maze.drawFtPrt(currentNeighbor);
    			}
    			meet=true;
    			break;
    		}
    	}
    	if(meet==true) {
    		break;
    	}
    	leaveCell =endQueue.removeFirst();//acquire the first cell from end queue
    	maze.drawFtPrt(leaveCell);
    	leaveCells.add(leaveCell);//insert it into the leave cells that is visited
    	if(leaveCell.tunnelTo!=null) {//confirm the cell has a tunnel
    		Cell tunnel =leaveCell.tunnelTo;
    		 if ((!endQueue.contains(tunnel))&& (!leaveCells.contains(tunnel)))  
            		
            {
                endQueue.add(tunnel);
            }
    		 else if((newQueue.contains(tunnel)) || leaveCells.contains(tunnel))
             {
					if(!newCells.contains(tunnel))
					{
						maze.drawFtPrt(tunnel);
						leaveCells.add(tunnel);
					}		
             	meet = true;
             	break;
             }
    	}
    	for (int i = 0; i < Maze.NUM_DIR; i++) // insert all neighbor cells into the queue
		{
            Cell currentNeighbor = leaveCell.neigh[i];
             if((newCells.contains(currentNeighbor)) && (!leaveCell.wall[i].present))
            {
            	meet = true;
            	break;
            }
            else if ((Check(currentNeighbor)) && (!endQueue.contains(currentNeighbor))
            		&& (!leaveCell.wall[i].present) && (!leaveCells.contains(currentNeighbor))) 
            {
                endQueue.add(currentNeighbor);
            }
            else if((newQueue.contains(currentNeighbor)) && (!leaveCell.wall[i].present))
            {
            	if(!newCells.contains(currentNeighbor))
            	{
            		leaveCells.add(currentNeighbor);
                	maze.drawFtPrt(currentNeighbor);
            	}	
            	meet = true;
            	break;
            }
           
           
        }
    	if(meet==true) {
    		break;
    	}
    	
    }
	} // end of solveMaze()


	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return meet;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return leaveCells.size()+newCells.size();
	} // end of cellsExplored()
    
	private boolean Check(int line, int Col) {//confirm the cell is in the row and column
    	if(newMaze.type == HEX) {
    		return line >= 0 && line < newMaze.sizeR && Col >=(line+1)/2&& Col<newMaze.sizeC+(line+1)/2;
    	}
    	else {
    		return line >=0&&line<newMaze.sizeR&&Col>=0&&Col<newMaze.sizeC;
    	}
    }
    private boolean Check(Cell cell) {//confirm the cell is in the maze
    	return cell !=null && Check(cell.r,cell.c);
    }
} // end of class BiDirectionalRecursiveBackTrackerSolver
