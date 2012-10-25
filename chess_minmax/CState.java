/* **********************************************************************
   	FileName: CState.java
	Author's name: Guojun Zhang 
	Course Title:  CSC 680 Artificial Intelligence
	Semester: Fall 2012
	Assignment Number: #3
	Submission Date: Wednesday October 17, 2012 
	Purpose: this aux class represents an ADT for checkers states
   	Change log: 
   	original version : Forouraghi
   	10/12/2012 -- Guojun Zhang add more functions and implement MINMAX 
   				 algorithm.
 *
 ************************************************************************ */

import java.util.*;

//********************************************************************

class CState {
	public static final int MAX_TYPE = 0;
	public static final int MIN_TYPE = 1;
	public static final int BLUE_TURN = 0;
	public static final int RED_TURN = 1;
	public static final int BS = 1;
	public static final int BK = 2;
	public static final int RS = 3;
	public static final int RK = 4;

	// *** the evaluation function e(n)
	private double e;

	// *** node type: MAX or MIN
	private int type;

	// *** some board configuration
	private int[][] state;

	// **************************************************************
	CState(int[][] state, int type) {
		this.state = state;
		this.type = type;
	}

	// **************************************************************
	// *** evaluate a state based on the evaluation function
	void evalState() {

		double num_bs = 0;
		double num_bk = 0;
		double num_rs = 0;
		double num_rk = 0;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				switch (state[i][j]) {
				case 1:
					num_bs++;
					break;
				case 2:
					num_bk++;
					break;
				case 3:
					num_rs++;
					break;
				case 4:
					num_rk++;
					break;
				}

			}

		}
		e = (5 * num_bk + num_bs) - (5 * num_rk + num_rs);
	}

	// **************************************************************
	// *** get a node's E() value
	double getE() {
		return e;
	}

	void setE(double e) {
		this.e = e;
	}

	// **************************************************************
	// *** get a node's type
	int getType() {
		return type;
	}

	// **************************************************************
	// *** get a state
	public String toString() {
		String result = "Type: " + type + "\n";

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++)
				result = result + state[i][j] + " ";
			result += "\n";
		}
		return result;
	}

	int[][] getState() {
		return state;
	}

	// **check if position is in the board
	private boolean legalPosition(int i, int j) {
		// *** can't go outside board boundaries
		return ((i >= 0) && (i < state.length) && j >= 0 && j < state.length);
	}

	/*
	 * Purpose: according to moved piece generating new board configuration
	 * input: the piece original position and destination output: new board
	 * configuration -- 2d array
	 */
	private int[][] getNewState(int oldRow, int oldCol, int newRow, int newCol) {
		int[][] newState = new int[state.length][state.length];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (i == oldRow && j == oldCol) {
					newState[i][j] = 0;
				} else if (i == newRow && j == newCol) {
					newState[i][j] = state[oldRow][oldCol];
				} else {
					newState[i][j] = state[i][j];
				}
			}
		}
		if (oldRow - newRow == 2) {
			if (oldCol - newCol == 2)
				newState[oldRow - 1][oldCol - 1] = 0;
			else if (newCol - oldCol == 2)
				newState[oldRow - 1][oldCol + 1] = 0;

		}
		if (newRow - oldRow == 2) {
			if (oldCol - newCol == 2)
				newState[oldRow + 1][oldCol - 1] = 0;
			else if (newCol - oldCol == 2)
				newState[oldRow + 1][oldCol + 1] = 0;
		}
		if (newRow == 0 || newRow == state.length - 1) {
			if (newState[newRow][newCol] == BS
					|| newState[newRow][newCol] == RS) {
				newState[newRow][newCol] += 1;
			}

		}
		return newState;
	}

	/*
	 * Purpose: according to current state and who's turn for nest step generate
	 * all possible states input: blue's turn or red's turn output: ArrayList
	 * contain all possible states
	 */
	public ArrayList<CState> getPossibleStates(int turn) {
		ArrayList<CState> pStates = new ArrayList<CState>();
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (turn == BLUE_TURN) {
					int legend = state[i][j];
					if (legend == BS || legend == BK) {
						// check up left corner
						if (legalPosition(i - 1, j - 1)
								&& state[i - 1][j - 1] == 0) {
							pStates.add(new CState(getNewState(i, j, i - 1,
									j - 1), (type + 1) % 2));
						}
						// check up right corner
						if (legalPosition(i - 1, j + 1)
								&& state[i - 1][j + 1] == 0) {
							pStates.add(new CState(getNewState(i, j, i - 1,
									j + 1), (type + 1) % 2));
						}
						// check up left if red piece can be capture
						if (legalPosition(i - 2, j - 2)
								&& state[i - 2][j - 2] == 0
								&& (state[i - 1][j - 1] == RS || state[i - 1][j - 1] == RK)) {
							pStates.add(new CState(getNewState(i, j, i - 2,
									j - 2), (type + 1) % 2));
						}
						// check up right if red piece can be capture
						if (legalPosition(i - 2, j + 2)
								&& state[i - 2][j + 2] == 0
								&& (state[i - 1][j + 1] == RS || state[i - 1][j + 1] == RK)) {
							pStates.add(new CState(getNewState(i, j, i - 2,
									j + 2), (type + 1) % 2));
						}
						if (legend == BK) {
							// check down left corner
							if (legalPosition(i + 1, j - 1)
									&& state[i + 1][j - 1] == 0) {
								pStates.add(new CState(getNewState(i, j, i + 1,
										j - 1), (type + 1) % 2));
							}
							// check down right corner
							if (legalPosition(i + 1, j + 1)
									&& state[i + 1][j + 1] == 0) {
								pStates.add(new CState(getNewState(i, j, i + 1,
										j + 1), (type + 1) % 2));
							}
							// check down left if red piece can be capture
							if (legalPosition(i + 2, j - 2)
									&& state[i + 2][j - 2] == 0
									&& (state[i + 1][j - 1] == RS || state[i + 1][j - 1] == RK)) {
								pStates.add(new CState(getNewState(i, j, i + 2,
										j - 2), (type + 1) % 2));
							}
							// check down right if red piece can be capture
							if (legalPosition(i + 2, j + 2)
									&& state[i + 2][j + 2] == 0
									&& (state[i + 1][j + 1] == RS || state[i + 1][j + 1] == RK)) {
								pStates.add(new CState(getNewState(i, j, i + 2,
										j + 2), (type + 1) % 2));
							}
						}
					}
				} else {

					int legend = state[i][j];
					if (legend == RS || legend == RK) {
						// check down left corner
						if (legalPosition(i + 1, j - 1)
								&& state[i + 1][j - 1] == 0) {
							pStates.add(new CState(getNewState(i, j, i + 1,
									j - 1), (type + 1) % 2));
						}
						// check down right corner
						if (legalPosition(i + 1, j + 1)
								&& state[i + 1][j + 1] == 0) {
							pStates.add(new CState(getNewState(i, j, i + 1,
									j + 1), (type + 1) % 2));
						}
						if (legalPosition(i + 2, j - 2)
								&& state[i + 2][j - 2] == 0
								&& (state[i + 1][j - 1] == BS || state[i + 1][j - 1] == BK)) {
							pStates.add(new CState(getNewState(i, j, i + 2,
									j - 2), (type + 1) % 2));
						}
						if (legalPosition(i + 2, j + 2)
								&& state[i + 2][j + 2] == 0
								&& (state[i + 1][j + 1] == BS || state[i + 1][j + 1] == BK)) {
							pStates.add(new CState(getNewState(i, j, i + 2,
									j + 2), (type + 1) % 2));
						}
						if (legend == RK) {
							// check up left corner
							if (legalPosition(i - 1, j - 1)
									&& state[i - 1][j - 1] == 0) {
								pStates.add(new CState(getNewState(i, j, i - 1,
										j - 1), (type + 1) % 2));
							}
							// check up right corner
							if (legalPosition(i - 1, j + 1)
									&& state[i - 1][j + 1] == 0) {
								pStates.add(new CState(getNewState(i, j, i - 1,
										j + 1), (type + 1) % 2));
							}
							if (legalPosition(i - 2, j - 2)
									&& state[i - 2][j - 2] == 0
									&& (state[i - 1][j - 1] == BS || state[i - 1][j - 1] == BK)) {
								pStates.add(new CState(getNewState(i, j, i - 2,
										j - 2), (type + 1) % 2));
							}
							if (legalPosition(i - 2, j + 2)
									&& state[i - 2][j + 2] == 0
									&& (state[i - 1][j + 1] == BS || state[i - 1][j + 1] == BK)) {
								pStates.add(new CState(getNewState(i, j, i - 2,
										j + 2), (type + 1) % 2));
							}
						}
					}
				}
			}
		}
		return pStates;
	}// end of getPossibleStates
}
