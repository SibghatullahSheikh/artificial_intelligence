/* **********************************************************************
   	FileName: Checkers.java
	Author's name(s): Guojun Zhang 
	Course Title:  CSC 680 Artificial Intelligence
	Semester: Fall 2012
	Assignment Number: #3
	Submission Date: Wednesday October 17, 2012 
	Purpose: This program will using MINMAX algorithm for the game of checkers
			-- boardPlan[][] keeps track of what pieces are to be displayed on the
        	   board
			-- there are four pieces: blue/red single pieces bs/rs
                               blue/red kings         bk/rk
	Help: Jing Zhao

   Change log: 
   
   original version : Forouraghi
   10/12/2012 -- Guojun Zhang add more functions and implement MINMAX 
   				 algorithm.
 *
 ************************************************************************ */
import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

//***********************************************************************
public class Checkers extends JFrame {

	/*
	 * set up an 8 by 8 checkers board with only five pieces legends: 0 - empty
	 * 1/2 = blue single/king 3/4 = red single/king
	 */
	/*
	 * private static int [][] boardPlan = { {0, 0, 0, 0, 0, 0, 0, 0}, //***
	 * blue pieces become king here {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0,
	 * 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 4, 0,
	 * 3, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 2, 0, 0, 0} //*** red
	 * pieces become king here };
	 */
	private boolean DONE = false; 
	private final static short PLY = 3;// indicate the ply of game

	 private static int [][] boardPlan =
	 {
	 {0, 3, 0, 3, 0, 3, 0, 3}, //*** blue pieces become king here
	 {3, 0, 3, 0, 3, 0, 3, 0},
	 {0, 3, 0, 3, 0, 3, 0, 3},
	 {0, 0, 0, 0, 0, 0, 0, 0},
	 {0, 0, 0, 0, 0, 0, 0, 0},
	 {1, 0, 1, 0, 1, 0, 1, 0},
	 {0, 1, 0, 1, 0, 1, 0, 1}, //*** red pieces become king here
	 {1, 0, 1, 0, 1, 0, 1, 0}
	
	 };
	
	// *** the legend strings
	private String[] legend = { "blank", "bs", "bk", "rs", "rk" };

	// *** create the checkers board
	private GameBoard board;

	// *** the xy dimensions of each checkers cell on board
	private int cellDimension;

	// *** pause in seconds in between moves
	private static int pauseDuration = 2000;

	// ***********************************************************************
	Checkers() {
		// *** set up the initial configuration of the board
		board = new GameBoard(boardPlan);

		// *** each board cell is 70 pixels long and wide
		cellDimension = 70;

		// *** set up the frame containign the board
		getContentPane().setLayout(new GridLayout());
		setSize(boardPlan.length * cellDimension, boardPlan.length
				* cellDimension);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(board);

		// *** enable viewer
		setVisible(true);

		// *** place all initial pieces on board and pause a bit
		putInitialPieces();
		pause(2 * pauseDuration);
	}

	// ***********************************************************************
	public void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
		}
	}

	// ***********************************************************************
	void putPiece(int i, int j, String piece) {
		// *** can do error checking here to make sure pieces are bs, bk, rs, rk
		board.drawPiece(i, j, "images/" + piece + ".jpg");
	}

	// ***********************************************************************
	void putInitialPieces() {
		// *** use legend variables to draw one piece at a time
		for (int i = 0; i < boardPlan.length; i++)
			for (int j = 0; j < boardPlan.length; j++)
				if (boardPlan[i][j] != 0)
					board.drawPiece(i, j, "images/" + legend[boardPlan[i][j]]
							+ ".jpg");
	}

	// ***********************************************************************
	boolean legalPosition(int i) {
		// *** can't go outside board boundaries
		return ((i >= 0) && (i < boardPlan.length));
	}

	String getBoard(int[][] a) {
		String result = "";

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++)
				result = result + a[i][j] + " ";

			result += "\n";
		}

		return result;
	}

	// ***********************************************************************

	private void movePiece(int i1, int j1, int i2, int j2, String piece) {
		// *** raise exception if outside the board or moving into a non-empty
		// *** cell
		if ((boardPlan[i2][j2] != 0) || !legalPosition(i1)
				|| !legalPosition(i2) || !legalPosition(j1)
				|| !legalPosition(j1))
			throw new IllegalMoveException("An illegal move was attempted.");

		// *** informative console messages
		System.out.println("Moved " + piece + " from position [" + i1 + ", "
				+ j1 + "] to [" + i2 + ", " + j2 + "]");

		// *** erase the old cell
		board.drawPiece(i1, j1, "images/blank.jpg");

		// *** draw the new cell
		board.drawPiece(i2, j2, "images/" + piece + ".jpg");

		// *** erase any captured piece from the board
		if ((Math.abs(i1 - i2) == 2) && (Math.abs(j1 - j2) == 2)) {
			// *** this handles hops of length 2
			// *** the captured piece is halfway in between the two moves
			int captured_i = i1 + (i2 - i1) / 2;
			int captured_j = j1 + (j2 - j1) / 2;

			// *** now wait a bit
			pause(pauseDuration);

			// *** erase the captured cell
			board.drawPiece(captured_i, captured_j, "images/blank.jpg");

			// *** print which piece was captured
			System.out
					.println("Captured "
							+ legend[boardPlan[captured_i][captured_j]]
							+ " from position [" + captured_i + ", "
							+ captured_j + "]");

			// *** the captured piece is removed from the board with a bang
			boardPlan[captured_i][captured_j] = 0;
			Applet.newAudioClip(getClass().getResource("images/hit.wav")).play();
		}

		// *** update the internal representation of the board by moving the old
		// *** piece into its new position and leaving a blank in its old
		// position
		boardPlan[i2][j2] = boardPlan[i1][j1];
		boardPlan[i1][j1] = 0;

		// *** red single is kinged
		if ((i2 == boardPlan.length - 1) && (boardPlan[i2][j2] == 3)) {
			boardPlan[i2][j2] = 4;
			putPiece(i2, j2, "rk");
		}

		// *** blue single is kinged
		if ((i2 == 0) && (boardPlan[i2][j2] == 1)) {
			boardPlan[i2][j2] = 2;
			putPiece(i2, j2, "bk");
		}

		// *** now wait a bit
		pause(pauseDuration);
	}

	/*
	 * Purpose: move the piece according to different state
	 * 			1, when regular diagonal move
	 * 			2, when jump move
	 * input: original state, state after moving and who's turn
	 */
	void move(CState c1, CState c2, int side){
		int[][] before = c1.getState();
		int[][] after = c2.getState();
		int i1 = 0;
		int j1 = 0;
		int i2 = 0;
		int j2 = 0;
		int piece = 0;
		for (int i = 0; i < before.length; i++) {
			for (int j = 0; j < before.length; j++) {
				if (before[i][j] != after[i][j]) {
					if (before[i][j] == 0) {
						i2 = i;
						j2 = j;
					} else if(before[i][j] == side*2 + 1 || before[i][j] == side*2 + 2){
						i1 = i;
						j1 = j;
					}
				}
			}
		}
		piece = before[i1][j1];
		movePiece(i1, j1, i2, j2, numberToPicec(piece));
	}
	//*** mapping number to GUI board piece
	private String numberToPicec(int i) {
		if (i == 1) {
			return "bs";
		} else if (i == 2) {
			return "bk";
		} else if (i == 3) {
			return "rs";
		} else if (i == 4) {
			return "rk";
		} else
			return null;
	}
	/*
	 * Purpose: implement MinMax algorithm
	 * input: current state, who's turn and ply
	 * output: the best state have to take
	 */
	public CState maxmin(CState current, int turn, int depth) {
		
		int workingTurn = turn;

		if (depth == PLY) {
			current.evalState();
			
			return current;
		} else {
			depth++;
			CState best = null;
			ArrayList<CState> sucessors = current.getPossibleStates(turn);
			
			if(sucessors.isEmpty()){
				if(depth == 1){
					return null;
				}
					
				current.evalState();
				return current;
			}
			
			PriorityQueue<CState> queue = new PriorityQueue<CState>(10,
					new Comparator<CState>() {
						// *** define your own comparator object for ordering
						public int compare(CState stateA, CState stateB) {
							// *** sort into ascending order based on evaluation
							// function e()
							if (stateA.getType() == CState.MAX_TYPE) {
								if (stateA.getE() >= stateB.getE())
									return 1;
								else
									return -1;

							} else {

								if (stateA.getE() >= stateB.getE())
									return -1;
								else
									return 1;
							}
						}
					});

			for (int i = 0; i < sucessors.size(); i++) {
				
				CState cs = sucessors.get(i);
				maxmin(cs, (workingTurn+1)%2, depth);
				queue.offer(cs);
			}
			if (!queue.isEmpty()) {
				//if each state in PriorityQueue has same evaluation
				//random pick up one
				if( isSameValue(queue)){
					Random random = new Random();
					int randomNumber = random.nextInt(queue.size() - 0) + 0;
					while(randomNumber != 0){
						queue.poll();
						randomNumber--;
					}
				}
					best = queue.poll();
					current.setE(best.getE());
					
			}
			return best;
		}
	}
	
	//*** check if each element(state) in PriorityQueue have same evaluation 
	private boolean isSameValue(PriorityQueue<CState> queue){
		boolean same = true;
		Iterator<CState> it = queue.iterator();
		double first = it.next().getE();
		while(it.hasNext()){
			if(it.next().getE() != first){
				same = false;
			}
		}
		return same;
	}
	
	//***** play the game always blue first
	
	public void play() {
		CState current = new CState(boardPlan, CState.MAX_TYPE);
		int side = CState.BLUE_TURN;
		String winner = " ";
		CState bestMove;

		while (!DONE) {
			// always blue first
			bestMove = maxmin(current, side, 0);
			
			if (side == CState.BLUE_TURN) {
				if (bestMove == null) {
					// red win
					DONE = true;
					winner = "red";
					break;
				}
				move(current, bestMove,side);
				side = CState.RED_TURN;
			} else {
				if (bestMove == null) {
					// blue win
					DONE = true;
					winner = "blue";
					break;
				}
				
				move(current, bestMove,side);
				side = CState.BLUE_TURN;
			}
			
			current = bestMove;		
		}
		JOptionPane.showMessageDialog(board, "Winner is " +winner);
		System.out.println("winner: "+ winner);
		
	}//end of play

	// ***********************************************************************
	// *** incorporate your MINIMAX algorithm in here
	// ***********************************************************************
	public static void main(String[] args) {
		// *** create a new game and make it visible
		Checkers game = new Checkers();
		game.play();
	}
}
