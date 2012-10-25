import java.util.ArrayList;
import java.util.PriorityQueue;


public class Nodes implements Comparable{

		private int xcoord;	// the x coordinator of node in 2-d arrray
		private int ycoord; // the y coordinator of node in 2-d array
	    private int gvalue; // the cost of the path from the starting point to any node n
	    private int hvalue; // an estimate of the minimum cost from any vertex n to the goal
	    private int fvalue; //
	    private Nodes father;// indicate who discover the node    
	    private static final short ONEMOVE= 10;
		private static final short ALPHA = 2;
		private static final short BETA = 10;
		private int hcost = 10;
		private Maze myMaze;
		
	    public Nodes(int x, int y,int gvalue,int hvalue){
	    	this.xcoord = x;
	    	this.ycoord = y;
	    	this.gvalue = gvalue;
	    	this.hvalue = hvalue;
	     	this.father = father;
	    	fvalue = ALPHA*gvalue + BETA*hvalue;;
	    }
		

       public int getPriority() {
    	   return fvalue;
       }
       public int getXcoord(){
    	   return xcoord;
	   }
	   public int getYcoord(){
		   return ycoord;
	   }
       private ArrayList<Nodes> getNeighbors(){
    	 //left xpos,ypos-1|right xpos,ypos+1|up xpos-1,ypos|down xpos+1,ypos
			ArrayList<Nodes> neighbors = new ArrayList<Nodes>(4);

			if(myMaze.openSpace(xcoord-1,ycoord)){
				neighbors.add(discoverUpChild());
			}
			if(myMaze.openSpace(xcoord+1,ycoord)){
				neighbors.add(discoverDownChild());
			}
			if(myMaze.openSpace(xcoord,ycoord-1)){
				neighbors.add(discoverLeftChild());
			}
			if(myMaze.openSpace(xcoord,ycoord+1)){
				neighbors.add(discoverRightChild());
			}
			return neighbors;
	   	}
	   	private Nodes discoverLeftChild(){

	   		int gcost = this.gvalue + ONEMOVE;
	   		
	   		return  new Nodes(xcoord,ycoord-1,gcost,hcost);
	   	}
	   	private Nodes discoverRightChild(){

	   		int gcost = this.gvalue + ONEMOVE;
	   		
	   		return new Nodes(xcoord,ycoord+1,gcost,hcost);
	   	}
	   	private Nodes discoverUpChild(){
	   		int gcost = this.gvalue + ONEMOVE;
	   		
	   		return new Nodes(xcoord-1,ycoord,gcost,hcost);
	   	}
	   	private Nodes discoverDownChild(){
	   		int gcost = this.gvalue + ONEMOVE;
	   		
	   		return new Nodes(xcoord+1,ycoord,gcost,hcost);
	   	}
	   	
		public boolean equals(Nodes node){
			return (this.xcoord == node.xcoord) && (this.ycoord == node.ycoord);
		}
		
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
	public int compareTo(Object arg0) {
		Nodes other = (Nodes)arg0;
		if(this.fvalue == other.fvalue){
			return 0;
		}else if(this.fvalue > other.fvalue){
			return 1;
		}else
			return -1;
		//return a.getPriority() - b.getPriority();
	}
	public static void main(String[] args) {
		 PriorityQueue<Nodes> pq = new PriorityQueue<Nodes>(10);
		 pq.add(new Nodes(2,2,10,20));
		 pq.add(new Nodes(2,2,15,20));
		 pq.add(new Nodes(2,2,20,20));
		 pq.add(new Nodes(2,2,10,40));
		 pq.add(new Nodes(2,2,30,60));
		 
		 System.out.println(pq.poll());
		 System.out.println(pq.poll());
		 Nodes node1 = new Nodes(2,2,10,20);
		 Nodes node2 = new Nodes(2,1,10,20);
		 
		 if(node1.equals(node2)){
			 System.out.println("yes");
		 }
	}
	
}