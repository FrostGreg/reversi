/* RevList Class / Author: @Greg.Frost
 * 
 * - List style class 
 * - get/set element methods
 * - method to clear the list
 * - method to find the next empty element
 * */

package reversi;

public class RevList {
	public int row, col;
	public int list[][];
	
	//constructor creates an int array based on input size, also initialises elements to 9
	public RevList(int inRow, int inCol) {
		row = inRow;
		col = inCol;
		list = new int[row][col];
		clear();
	}
	
	//method to set all elements to 9 (an invalid value on an 8x8 grid)
	public void clear() {
		//initialises the list
		for (int i=0; i < row; i++) {
			for (int j=0; j < col; j++) {
				list[i][j] = 9;
			}
		}
	}
	
	//find the next available element in the list
	public int nextEmpty() {
		int i;
		for (i=0; i < row; i++) {
			if (list[i][0] == 9)
				break;
		}
		return i;
	}
	
	//sets a specific element in the list
	public void setElement(int row, int column, int val) {
		list[row][column] = val;
	}
	
	//gets a specific element in the list
	public int getElement(int row, int col) {
		return list[row][col];
	}
	
}
