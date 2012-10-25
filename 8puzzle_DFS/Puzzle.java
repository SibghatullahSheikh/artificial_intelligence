/*  ***************************************************************************
Author¡¯s name(s): Guojun Zhang
Course Title:	 CSC 680: Artificial Intelligence 
Semester:	fall 2012
Assignment Number: HW1
Submission Date: Wednesday 9/19/2012 
Purpose: To gain programming experience using an uninformed search method.
		 The x-puzzle is a sliding puzzle that consists of a grid of numbered
		 squares with one square missing, and the labels on the squares jumbled
		 up.The goal of the puzzle is to unjumble the squares by only making 
		 moves which slide squares into the empty space, in turn revealing 
		 another empty space in the position of the moved piece.
Input:   initial state and goal state which represented by 2-d array
Output:  the path from initial state to goal state 
******************************************************************************/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Puzzle {

	private Node initNode;  // initial node
	private Node goalNode;  // goal node 
	private Stack<Node> st;  //track the path to goal
	private boolean found = false;
	private static final short LEFT = 1;
	private static final short RIGHT = 2;
	private static final short UP = 3;
	private static final short DOWN = 4;
	
	/*
	 * Constructor 
	 */
	public Puzzle(int[][] init,int[][] goal,int size){
		
		initNode = new Node(init, -1,size);
		goalNode = new Node(goal, -1, size);
		st = new Stack<Node>();
	}
	/*
	 * Node inner class represent the puzzle state for each move
	 * 
	 */
	private class Node{
		//2-d int array indicate the each squares in the puzzle
		// int 0 indicate the title you can move
		private int[][] state;
		//record last move 
		private int lastMove;
		private int boundary ;
		/*
		 * constructor
		 */
		public Node(int[][] state, int move, int size){
			this.state = state;
			this.lastMove = move;
			boundary = size;
		}

		public String toString(){
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < boundary; i++ ){
				for(int j = 0; j < boundary; j++){
					sb.append(state[i][j] + " ");
				}
				sb.append("\n");
			}
			for(int i = 0 ; i <= boundary; i++){
				sb.append("- ");
			}				
			return sb.toString();
		}	
	}
	
	/*
	 * Purpose: get the horizontal and vertical coordinates of title
	 * Input: Node which indicate the current state of squares in puzzle 
	 * Output: horizontal and vertical coordinates in int array
	 */
	private int[] getMover(Node node){
		
		int[] pos = new int[2];
		for(int i = 0; i < node.boundary; i++){
			for(int j = 0; j < node.boundary; j++){
				if(node.state[i][j] == 0){
					pos[0] = i;
					pos[1] = j;
				}
			}
		}
		return pos;
	}

	/*
	 * Purpose: move the title left and generate another node 
	 * Input: Node which indicate the current state of squares in puzzle 
	 * Output: another node which represent the node after moving title
	 */
	
	private Node moveLeft(Node node){
		//System.out.println("Move left <---");
		if(node == null){
			return null;
		}else{
			if(node.lastMove == RIGHT){
				return null;
			}
			int[] pos = getMover(node);
			int i = pos[0];
			int j = pos[1];
			if(j  <= 0){
				return null;
			}else{
				int[][] tmp = new int[node.boundary][node.boundary];
				for(int n = 0; n < node.boundary; n++){
					for(int m = 0; m < node.boundary; m++){
						tmp[n][m] = node.state[n][m];
					}
				}
				tmp[i][j] = tmp[i][j-1];
				tmp[i][j-1] = 0;
				return new Node(tmp,LEFT,node.boundary);
			}	
		}	
	}//end of left
	
	/*
	 * Purpose: move the title right and generate another node 
	 * Input: Node which indicate the current state of squares in puzzle 
	 * Output: another node which represent the node after moving title
	 */
	private Node moveRight(Node node){
		//System.out.println("Move right --->");
		if(node == null){
			return null;
		}else{
			if(node.lastMove == LEFT){
				return null;
			}
			int[] pos = getMover(node);
			int i = pos[0];
			int j = pos[1];
			if(j >= node.boundary -1){
				return null;
			}else{
				int[][] tmp = new int[node.boundary][node.boundary];
				for(int n = 0; n < node.boundary; n++){
					for(int m = 0; m < node.boundary; m++){
						tmp[n][m] = node.state[n][m];
					}
				}
				tmp[i][j] =	tmp[i][j+1];
				tmp[i][j+1] = 0;
				return new Node(tmp,RIGHT,node.boundary);
			}
		}
		
	}//end of moveRight
	/*
	 * Purpose: move the title up and generate another node 
	 * Input: Node which indicate the current state of squares in puzzle 
	 * Output: another node which represent the node after moving title
	 */
	private Node moveUp(Node node){
		//System.out.println("Move up ^^^^^");
		if(node == null){
			return null;
		}else{
			if(node.lastMove == DOWN){
				return null;
			}
			int[] pos = getMover(node);
			int i = pos[0];
			int j = pos[1];
			if(i  <= 0){
				return null;
			}else{
				int[][] tmp = new int[node.boundary][node.boundary];
				for(int n = 0; n < node.boundary; n++){
					for(int m = 0; m < node.boundary; m++){
						tmp[n][m] = node.state[n][m];
					}
				}
				tmp[i][j] = tmp[i-1][j];
				tmp[i-1][j] = 0;
				return new Node(tmp,UP,node.boundary);
			}
		}
	}//end of moveUp
	
	/*
	 * Purpose: move the title down and generate another node 
	 * Input: Node which indicate the current state of squares in puzzle 
	 * Output: another node which represent the node after moving title
	 */
	private Node moveDown(Node node){
		//System.out.println("Move down _____");
		if(node == null){
			return null;
		}else{
			if(node.lastMove == UP){
				return null;
			}
			int[] pos = getMover(node);
			int i = pos[0];
			int j = pos[1];
			if(i  >= node.boundary-1){
				return null;
			}else{
				int[][] tmp = new int[node.boundary][node.boundary];
				for(int n = 0; n < node.boundary; n++){
					for(int m = 0; m < node.boundary; m++){
						tmp[n][m] = node.state[n][m];
					}
				}
				tmp[i][j] = tmp[i+1][j];
				tmp[i+1][j] = 0;
				return new Node(tmp,DOWN,node.boundary);
			}
		}
	}//end of moveDown
	
	/*
	 * Purpose: compare if two nodes if they have same state 
	 * Input: two Nodes which indicate the different state of squares in puzzle 
	 * Output: true if two nodes have same state vice versa
	 */
	private boolean isMatch(Node node1, Node node2){
		int[][] currstate = node1.state;
		int[][] goalstate = node2.state;	
		for(int i = 0; i < node1.boundary; i++){
			for(int j = 0; j< node1.boundary; j++){
				if(currstate[i][j] != goalstate[i][j])
					return false;
			}
		}
		return true;
	}
	/* Purpose: Using iterative deepening search (IDS) to solve the puzzle
	 * 			increasing the depth limit of depth first search(dfs) with each 
	 * 			iteration until it reaches the goal.
	 * 			From initial state, moving title with order left, right
	 * 			up and down to generate 4 children. Recursively call dfs with
	 * 			limited depth for each of node. if can't find the goal, with 
	 * 			increased depth repeat dfs until find the goal node. 
	 * 			stack will keep tracking the path to get the goal.
	 * Input:   the initial and goal node 
	 * Output:  each step of title move to the goal
	 */
	public String solvePuzzle(){
		
		int depth = 0;
		StringBuffer result = new StringBuffer();
		//iterative deepening search algorithm implement
		while(!found ){
			//System.out.println("----DFS depth---- " + depth);
			dfs(initNode, goalNode, depth);
			depth++;
		}
		//iterate stack append content to string buffer
		for(Node node: st){
			switch(node.lastMove){
			case -1:
				result.append("Initial");
				break;
			case 1:
				result.append("Left");
				break;
			case 2:
				result.append("Right");
				break;
			case 3:
				result.append("Up");
				break;
			case 4:
				result.append("Down");
				break;
			default:
				break;
			}
			if(node != st.peek()){
				result.append("->");
			}
		}
		return result.toString();			
	}
	/*
	 * Purpose: the actual depth first search method to traversal each node 
	 * Input: initial node and goal node 
	 * Output: void
	 */
	private void dfs(Node initNode, Node goalNode, int depth){	
		
		//System.out.println("In DFS method print \n"+ initNode);
		//System.out.println("st stack push \n" + initNode);
		//push all visited node into stack
		st.push(initNode);
		
		//return if find the goal node
		//the base case of recursion. 
		if(isMatch(initNode, goalNode)){
			//System.out.println("!!!! Find the goal !!!!");
			found = true; 
			return;
		}else{
			//reach the bottom and the node is not the goal node
			//then pop it as useless
			if(depth == 0){
				Node tmp = st.pop();
				//System.out.println("st stack pop \n" + tmp);
				return;				
			}else{
			//generate nodes for each movement order with left, right,
			//up and down. then recursively call dfs for each node
				Node midNode;
				depth--; 
				if(!found){
					midNode = moveLeft(initNode);
					if(midNode != null && !found){	
						dfs(midNode,goalNode,depth);
					}
				}
				if(!found){
					midNode = moveRight(initNode);
					if(midNode != null && !found){
						dfs(midNode,goalNode,depth);
					}
				}				
				if(!found){
					midNode = moveUp(initNode);
					if(midNode != null && !found){
						dfs(midNode,goalNode,depth);
					}
				}
				if(!found){
					midNode = moveDown(initNode);
					if(midNode != null && !found){
						dfs(midNode,goalNode,depth);
					}
				}
				// pop the nodes when find the path to the goal node
				// pop it in solvePuzzle method and print the result 
				if(!found){
					Node popnode = st.pop();
					//System.out.println("st stack pop \n" + popnode);
				}
			}
		}
	}
	
	/*
	 * purpose: parser the input file as int array 
	 * 			pass the array into ids function get the path to goal.
	 * input:  a text file have two line 
	 * 		   first line is the initial state of puzzle
	 * 		   second line is the goal state of puzzle 
	 * output: string which tell you how to move the title 
	 * 			to the goal state
	 */
	public static void main(String[] args) {
		File input = new File(args[0]);
		try {
			Scanner scan = new Scanner(input);		
			String[] initState = scan.nextLine().split(" +");
			String[] goalState = scan.nextLine().split(" +");	
		
			int size =  (int) Math.sqrt(initState.length);
			if(Math.sqrt(initState.length) == size){
				int[][] init = new int[size][size] ;
				int[][] goal = new int[size][size] ;
				int count1 = 0;
				int count2 = 0;
				for(int i = 0; i < size ; i++){
					for(int j = 0; j < size; j++){
						init[i][j] = Integer.parseInt(initState[count1++]);
						goal[i][j] = Integer.parseInt(goalState[count2++]);
					}
				}
				Puzzle pu = new Puzzle(init,goal,size);		
				String result = pu.solvePuzzle();
				System.out.println("Move the title as following actions");
				System.out.println(result);
			}else{
				System.out.println("Input file is not valid");
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
}