package mazeGenerator;

import java.util.*;

import maze.Maze;
import maze.Cell;

public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as "usually pick the most recent cell, but occasionally pick a random cell"
	
	double threshold = 0.1;
	Cell A;
	Cell B;
	Random random = new Random();
	
	@Override
	public void generateMaze(Maze maze) {
		ArrayList<Cell> Visited = new ArrayList<Cell>();
		ArrayList<Cell> Z = new ArrayList<Cell>();
		ArrayList<Cell> unvisit = null;
		int tempR = random.nextInt(maze.sizeR);
		int tempC = random.nextInt(maze.sizeC);
		int strategy = ((int)(Math.random()*2))%2;
		//Pick a random starting cell and add it to set Z
		do {
			tempR = random.nextInt(maze.sizeR);
			tempC = random.nextInt(maze.sizeC);
		}while(maze.map[tempR][tempC] == null);
		Cell Scell = maze.map[tempR][tempC];
		Z.add(Scell);
		Visited.add(Scell);
		//Use a particular strategy to select a cell b from Z
		while(!Z.isEmpty()){
			if(strategy == 1) {
				//Randomly select a cell (1/3)
				A = Z.get(random.nextInt(Z.size()));
			}else if(strategy == 0) {
				//Select the newest cell (2/3)
				A = Z.get(Z.size()-1);
			}
			unvisit = unvisited(A, Visited);
			//Check if cell A has unvisited neighbour
			if(!unvisit.isEmpty()){
				B = unvisit.get(random.nextInt(unvisit.size()));
				//Carve a path
				for (int i = 0; i < B.neigh.length; i++) {
					if(B.neigh[i] != null){
						if (B.neigh[i].equals(A)) {
							B.wall[i].present = false;
						}
					}
				}
				Visited.add(B);
				Z.add(B);
			}
			unvisit = unvisited(A, Visited);
			//Check if cell A has unvisited neighbour
			if(unvisit.isEmpty()){
				Z.remove(A);
			}
		}
	}// end of generateMaze()
	
	ArrayList<Cell> unvisited(Cell a, ArrayList<Cell> Visited){
		ArrayList<Cell> UNVISIT = new ArrayList<Cell>();
		for(int i = 0; i < a.neigh.length; i++ ){
			if(!(Visited.contains(a.neigh[i])) && !(a.neigh[i] == null)){
				UNVISIT.add(a.neigh[i]);
			}
		}
		return UNVISIT;
	}
} 
