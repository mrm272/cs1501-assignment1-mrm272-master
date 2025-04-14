import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class A1 implements A1Interface {


    Set<String> answerSet = new HashSet<String>();
    StringBuilder currentSolution;

    @Override
    public Set<String> getWordsOfLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
        answerSet.clear();

        resetBoard(boggleBoard); // clears board
		// looping over board
        for(int i = 0; i < boggleBoard.length; i++) {
            for(int k = 0; k < boggleBoard[i].length; k++) {

                resetBoard(boggleBoard);
                solve(i, k, boggleBoard, dictionary, wordLength, true);
            }
        }


		// removing wildcard and block
		Iterator<String> iterator = answerSet.iterator();
		while(iterator.hasNext()) {
			String element = iterator.next();

			if(element.contains("*")) {
				iterator.remove();
			}
			else if(element.contains("#")) {
				iterator.remove();
			}
		}
        return answerSet;
    
    }

	
	//We have 8 directions:
	// 0 => up left
	// 1 => up
	// 2 => up right
	// 3 => right
	// 4 => down right
	// 5 => down
	// 6 => down left
	// 7 => left
	private void solve(int row, int col, char[][] boggleBoard, DictInterface dictionary, int wordLength, boolean b) {

		// setting coordinate values
		Coordinates Coords = nextCoordinates(row, col, 8);
		int rowRemember = Coords.row;
		int colRemember = Coords.col;

		// return if blocked
		if (nextChar(row, col, 8, boggleBoard) == '#'){
			return;
		}

		// wildcard
		else if(nextChar(row, col, 8, boggleBoard) == '*') {
			for(char x = 'a'; x < 'z'; x++) {
				boggleBoard[Coords.row][Coords.col] = x;
				solve(row, col, boggleBoard, dictionary, wordLength, true);
			}
			boggleBoard[rowRemember][colRemember] = '*';

		// string builder for solutions	
		}
		else {
			currentSolution = new StringBuilder();
			currentSolution.append(boggleBoard[row][col]);
			boggleBoard[row][col] = Character.toUpperCase(boggleBoard[row][col]);
			solve(row, col, boggleBoard, dictionary, wordLength);
		}

		
	}


	private void solve(int row, int col, char[][] boggleBoard, DictInterface dictionary, int wordLength){
		for(int direction=0; direction<8; direction++){

			// settigng next coords
			Coordinates nextCoords = nextCoordinates(row, col, direction);
			int rowRemember = nextCoords.row;
			int colRemember = nextCoords.col;

			// replacing wildcard with letter
			if(isValid(row, col, direction, boggleBoard)){

				if(nextChar(row, col, direction, boggleBoard) == '*') {
					
					for(char x = 'a'; x < 'z'; x++) {
						boggleBoard[nextCoords.row][nextCoords.col] = x;
						solve(row, col, boggleBoard, dictionary, wordLength);
					}
					boggleBoard[rowRemember][colRemember] = '*';
				}






				currentSolution.append(nextChar(row, col, direction, boggleBoard));
			    boggleBoard[nextCoords.row][nextCoords.col] =
					Character.toUpperCase(boggleBoard[nextCoords.row][nextCoords.col]);
                StringBuilder checker = new StringBuilder(currentSolution.toString().toUpperCase());
				int res = dictionary.searchPrefix(checker);







				if(res == 1){ //prefix but not word
					solve(nextCoords.row, nextCoords.col, boggleBoard, dictionary, wordLength);
				}

				if(res == 2 && currentSolution.length() == wordLength){ //word but not prefix
						answerSet.add(currentSolution.toString().toUpperCase());
				}

				if(res == 3){ //word and prefix
					//TODO: Write the code for the word and prefix case
					if (currentSolution.length() == wordLength) {
                        answerSet.add(currentSolution.toString().toUpperCase());
					}
                    else {
                        solve(nextCoords.row, nextCoords.col, boggleBoard, dictionary, wordLength);
                    }
					// building on word
				}
                




                if(currentSolution.length() >= 1) {
					currentSolution.deleteCharAt(currentSolution.length()-1);
					boggleBoard[nextCoords.row][nextCoords.col] =
						Character.toLowerCase(boggleBoard[nextCoords.row][nextCoords.col]);
                }
			}

		}
	}

	//is the letter already used (upper case) or are we at an edge of the board?
	private boolean isValid(int row, int col, int direction, char[][] theBoard){
		Coordinates coords = nextCoordinates(row, col, direction);

		//TODO: Write the code to check whether already used / at an edge of the board
		if (coords.row < 0 || coords.row >= theBoard.length || coords.col < 0 || coords.col >= theBoard.length) {
			return false;
		}
	
		// Check if the letter at the target location has already been used (is uppercase)
		if (Character.isUpperCase(nextChar(row, col, direction, theBoard))) {
			return false;
		}
	
		return true;
	}

	private char nextChar(int row, int col, int direction, char[][] theBoard){
		Coordinates coords = nextCoordinates(row, col, direction);
		return theBoard[coords.row][coords.col];
	}

	private Coordinates nextCoordinates(int row, int col, int direction){
		Coordinates result = null;
		switch(direction){
			case 0: //up left
				result = new Coordinates(row-1, col-1);
				break;
			case 1: //up
				result = new Coordinates(row-1, col);
				break;
			case 2: //up right
				result = new Coordinates(row-1, col+1);
				break;
			case 3: //right
				result = new Coordinates(row, col+1);
				break;
			case 4: //bottom right
				result = new Coordinates(row+1, col+1);
				break;
			case 5: //bottom
				result = new Coordinates(row+1, col);
				break;
			case 6: //bottom left
				result = new Coordinates(row+1, col-1);
				break;
			case 7: //left
				result = new Coordinates(row, col-1);
				break;
			case 8: 
				result = new Coordinates(row, col);
				break;
		}
		return result;
	}

	//reset all characters to lower case
	private void resetBoard(char[][] theBoard){

		for (int i = 0; i < theBoard.length; i++)
		{
			for (int j = 0; j < theBoard[i].length; j++)
			{
				theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
			}
		}
	}

	//inner class
	private class Coordinates {
		int row;
		int col;

		private Coordinates(int row, int col){
			this.row = row;
			this.col = col;
		}
    }

}