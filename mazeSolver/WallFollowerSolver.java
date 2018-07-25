package mazeSolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import maze.Cell;
import maze.Maze;
import static maze.Maze.NUM_DIR;
import static maze.Maze.oppoDir;

public class WallFollowerSolver implements MazeSolver {
        HashSet visitedCells = new HashSet<>();
        HashSet explored=null;
	    boolean achieved = false;
        Cell gate ;
        Cell exit;
        Cell current;
        int  startPosition;
        boolean exTunnel;
       
        public void checkPath(Cell cell,int Position){ //when path,neighbor and wall do not exist 
    	for(int i=0;i<NUM_DIR;i++){
    	if(cell.wall[i]!=null){
    		if(cell.neigh[i]==null&&!current.wall[i].present){
    			Position=i;
    			break;
    		}
    	}	
    	}	
    	}
    
       public void markCell(Cell cell,boolean tunnel){//mark cell which is at next position as the cell that is at the other end of tunnel
    	   cell=cell.tunnelTo;
    	   tunnel=true;
       } 
       
       public void travel(Cell cell,Maze newmaze,boolean reached){//check the cell and distinguish the status of solving maze
    					   if(cell.equals(exit)){
    						   newmaze.drawFtPrt(cell);
    						   visitedCells.add(cell);
    						   reached=true;
    					   }
       }
    @Override
	public void solveMaze(Maze maze) {
            gate = maze.entrance;
            exit = maze.exit;
            current=maze.entrance;
            startPosition = -1;
            exTunnel = false;
          while(current != maze.exit) { //travel all cells and mark cells that are visited
                maze.drawFtPrt(current);
                visitedCells.add(current);
                if(startPosition == -1) {    
                    checkPath(current,startPosition);
                }
                if(!exTunnel && current.tunnelTo != null) {
                markCell(current,exTunnel);
                    continue;
                }
            
                else {
                    exTunnel = false;
                }
                for(int i=startPosition;i<startPosition+NUM_DIR+1;i++){//travel and set all path,then check all elements that is used to solve the maze
                	if(i!=startPosition){
                		int path=i%NUM_DIR; 
                		if(current.neigh[path]!=null&&current.wall[path]!=null){
                			if(!current.wall[path].present){
                			current=current.neigh[path];
                			travel(current,maze,achieved);
                			startPosition = oppoDir[path];
                			break;	
                			}
                		}
                		
                		
                	}
                }
               
            }          
	} // end of solveMaze()
	@Override
	public boolean isSolved() {
            return achieved;
	} // end if isSolved()
    
	@Override
	public int cellsExplored() {
		return visitedCells.size();
	} // end of cellsExplored()

} // end of class WallFollowerSolver

