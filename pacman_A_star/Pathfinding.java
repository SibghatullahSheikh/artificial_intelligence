/*  ************************************************************************
Author¡¯s name(s): Guojun Zhang 
Course Title:  Artificial Intelligence
Semester: Fall 2012
Assignment Number: #2
Submission Date: Wednesday October 3, 2012 
Purpose: This program will using A* algorithm find a shortest path from the
		 initial node to goal node.
*****************************************************************************/
import java.util.*;

public class Pathfinding {
	
	private Node initNode; //indicate the initial node 
	private Node goalNode; //indicate the goal node
	private Maze myMaze;   //indicate the maze 
	private boolean found; //flag indicate if have available path
	private static final short ONEMOVE= 10;//indicate the cost of one step move
	private static final short ALPHA = 2; 
	private static final short BETA = 10;
	/*
	 * Main constructor
	 */
	public Pathfinding(Maze maze, int gvalue){
		this.myMaze = maze;
		int xicoord = Maze.pacmanX;
		int yicoord = Maze.pacmanY;
		int xgcoord = Maze.ghostX;
		int ygcoord = Maze.ghostY;
		int ma = getManhattanCost(xicoord,yicoord,xgcoord,ygcoord);
		this.initNode = new Node(xicoord,yicoord,gvalue,ma,null);
		this.goalNode = new Node(xgcoord,ygcoord,0,0,null);
	}
	
	/*
	 * Purpose: 
	 * 			Begin at the starting point A and add it to an ¡°open list¡± of
	 * 		    squares to be considered. Look at all the reachable or walkable
	 *			squares adjacent to the starting point. Add them to the open
	 *		    list, too. For each of these squares, save point A as its
	 *		    ¡°parent square¡±. This parent square stuff is important when we 
	 *			want to trace our path. Drop the starting square A from your
	 *			open list,and add it to a ¡°closed list¡± of squares that you 
	 *			don¡¯t need to look at again for now.
	 *			To implement the A* algorithm computes f(n) = 2*g(n)+ 10*h(n)
	 * 			for each node. g(n) is the movement cost to move from the 
	 * 			starting point to a given square on the grid, following 
	 * 			the path generated to get there.
	 * 			h(n) is the estimated movement cost to move from that given
	 * 		    square on the grid to the final destination.
	 *          
	 */
	
	
	public void getPath(){
		
		PriorityQueue<Node> openPQ = new PriorityQueue<Node>();
	    ArrayList<Node> closeList = new ArrayList<Node>();
		openPQ.add(initNode);
		while(!openPQ.isEmpty()){
			//poll node from PQ and add to close list
			Node refNode = openPQ.poll();
			closeList.add(refNode);

			ArrayList<Node> nbs = refNode.getNeighbors();
			
			// remove the nodes which already in the close list
		
			for(int i = 0; i < closeList.size(); i++){
				for(int j = 0; j < nbs.size(); j++){
					if(nbs.get(j).equals(closeList.get(i))){
						nbs.remove(j);
					}
				}
			}
			
			if(openPQ.size() == 0){
				for(int i = 0; i < nbs.size(); i++){
					openPQ.add(nbs.get(i));
				}
				
			}else{
				//discovered neighbors go through open list to see if need
				//gvaule update 
				for(int i = 0; i < nbs.size(); i++){
					Iterator<Node> it = openPQ.iterator();
					boolean foundFlag = false;
					while(it.hasNext()){
						//discover neighbors are equals open list item 
						Node openItem = it.next();
						if(nbs.get(i).equals(openItem)){
							
							foundFlag = true;
							//update gvalue and change the father
							if(refNode.gvalue + ONEMOVE < openItem.gvalue ){
								openItem.gvalue = refNode.gvalue + ONEMOVE;
								openItem.father = refNode;
							}
						}
						
					}
					
					//discover neighbors are not equals open list item 
					if(!foundFlag){
						if(nbs.get(i).equals(goalNode)){
							//find the goal node
							goalNode.father =  nbs.get(i).father;
							found = true;
							return;
						}else{
							openPQ.add(nbs.get(i));
						}
					}	
				}
			}
		}
	}
	
    //*** declare an internal class type
	private class Node implements Comparable{

		private int xcoord;	// the x coordinator of node in 2-d arrray
		private int ycoord; // the y coordinator of node in 2-d array
	    private int gvalue; // cost from the starting point to any node n
	    // an estimate of the minimum cost from any vertex n to the goal
	    private int hvalue; 
	    private int fvalue; //
	    private Node father;// indicate who discover the node    
	    /*
	     * internal Node class constructor
	     */
	    public Node(int x, int y,int gvalue,int hvalue,Node father){
	    	this.xcoord = x;
	    	this.ycoord = y;
	    	this.gvalue = gvalue;
	    	this.hvalue = hvalue;
	     	this.father = father;
	    	fvalue = ALPHA*gvalue + BETA*hvalue;;
	    }
	    /*
	     * Purpose: generate the all neighbor nodes depends on the maze map
	     * 			only generate the nodes which are Power Pellet.
	     * input:  none
	     * output: arraylist store all neighbor nodes 
	     */
		
       private ArrayList<Node> getNeighbors(){
    	
			ArrayList<Node> neighbors = new ArrayList<Node>(4);
			//if upper neighbor is Power Pellet
			if(myMaze.openSpace(xcoord-1,ycoord)){
				neighbors.add(discoverUpChild());
			}
			//if down neighbor is Power Pellet
			if(myMaze.openSpace(xcoord+1,ycoord)){
				neighbors.add(discoverDownChild());
			}
			//if left neighbor is Power Pellet
			if(myMaze.openSpace(xcoord,ycoord-1)){
				neighbors.add(discoverLeftChild());
			}
			//if right neighbor is Power Pellet
			if(myMaze.openSpace(xcoord,ycoord+1)){
				neighbors.add(discoverRightChild());
			}
			return neighbors;
	   	}
       /*
        * Purpose: create left neighbor node
        */
	   	private Node discoverLeftChild(){
	   		int gcost = this.gvalue + ONEMOVE;
	   		int hcost = getManhattanCost(this.xcoord,this.ycoord-1,
	   				goalNode.xcoord,goalNode.ycoord);
	   		return  new Node(this.xcoord,this.ycoord-1,gcost,hcost,this);
	   	}
	   	/*
	     * Purpose: create right neighbor node
	     */
	   	private Node discoverRightChild(){

	   		int gcost = this.gvalue + ONEMOVE;
	   		int hcost = getManhattanCost(this.xcoord,this.ycoord+1,
	   				goalNode.xcoord,goalNode.ycoord);
	   		return new Node(xcoord,ycoord+1,gcost,hcost,this);
	   	}
	   	
	   	/*
	     * Purpose: create upper neighbor node
	     */
	   	
	   	private Node discoverUpChild(){
	   		int gcost = this.gvalue + ONEMOVE;
	   		int hcost = getManhattanCost(this.xcoord-1,this.ycoord,
	   				goalNode.xcoord,goalNode.ycoord);
	   		return new Node(xcoord-1,ycoord,gcost,hcost,this);
	   	}
	   	
	   	/*
	    * Purpose: create down neighbor node
	    */
	   	private Node discoverDownChild(){
	   		int gcost = this.gvalue + ONEMOVE;
	   		int hcost = getManhattanCost(this.xcoord+1,this.ycoord,
	   				goalNode.xcoord,goalNode.ycoord);
	   		return new Node(xcoord+1,ycoord,gcost,hcost,this);
	   	}
	   	
	   	
	   	/*
	   	 * Purpose: equals method for Node object. only compare the 
	   	 * x coordinator and y coordinator
	   	 */
		public boolean equals(Node node){
		return (this.xcoord == node.xcoord) && (this.ycoord == node.ycoord);
		}
		
		/*
		 * toString method
		 */
	    public String toString(){
    	   StringBuffer sb = new StringBuffer();
    	   sb.append(fvalue+"     "+xcoord+","+ycoord+"\n");
    	
    	   if(father != null){
    		   sb.append("   "+father.xcoord+","+father.ycoord+"   \n");
    	   }else
    		   sb.append("   " +xcoord+","+ycoord+"     \n");
    	  
    	   sb.append(gvalue+"      " + hvalue+ "\n");
    	   sb.append("-------------");
    	   return sb.toString();
	     }


		@Override
		/*
		 * Purpose: Override the compareTo method. 
		 * 			only care about the fvaule
		 * 
		 */
		public int compareTo(Object arg0) {
			Node other = (Node)arg0;
			if(this.fvalue == other.fvalue){
				return 0;
			}else if(this.fvalue > other.fvalue){
				return 1;
			}else
				return -1;
		}
    } // NodeType
	
	/*
	 * Purpose: calculate the heuristic function h(n) 
	 * 			from one node to another using Manhattan distance
	 * input:  X, Y coordinator of both nodes
	 * output: Manhattan distance 
	 */
	private int getManhattanCost(int x1, int y1, int x2, int y2){

		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

    //************************************
    public static void main(String[] args) {
    	//*** create a new frame and make it visible
    	Maze mz = new Maze();
        mz.setVisible(true);

        //*** create a priority queue with the initial size 10 
        PriorityQueue<Node> pq = new PriorityQueue<Node>(10);
        Pathfinding pf = new Pathfinding(mz,10); 
        //run A* get the path from initial node to goal
        pf.getPath();
        //backup from goal node until get initial node
        if(pf.found){
        	Stack<Node> st = new Stack<Node>();
        	Node back = pf.goalNode.father;  	
        	st.push(back);
        	while(!back.equals(pf.initNode)){
        		back = back.father;
        		st.push(back);
        	}
        	//pop stack and make move
        	while(!st.isEmpty()){
        		Node node = st.pop();
        		mz.movePacman(node.xcoord,node.ycoord);
        		mz.wait(500);
        		mz.removePacman(node.xcoord,node.ycoord);
        	}
        	
        	System.out.println("Found the goal!");
        }else{
        	System.out.println("No path can get goal!");
        	
        }    
    } // main
} // Pathfinging