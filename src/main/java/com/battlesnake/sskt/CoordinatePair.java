package com.battlesnake.sskt;

public class CoordinatePair {
	int x;
	int y;

	CoordinatePair(int xCo, int yCo) {
		x = xCo;
		y = yCo;
	}

	public int[] getArray(){
		int[] ret = new int[2];
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	int cardinalDistanceTo(CoordinatePair destination) {
		return Math.abs(destination.x - x) + Math.abs(destination.y - y);
	}
	
	boolean isValid() {
		if(x < 0 || x >= Constants.WIDTH) {
			return false;
		}
		if(y < 0 || y >= Constants.HEIGHT) {
			return false;
		}
		return true;
	}
	
	CoordinatePair newAdjacent(String direction) {
		CoordinatePair ret = new CoordinatePair(x, y);
		if(direction.equals("up")) {
			ret.y++;
		}
		else if(direction.equals("right")) {
			ret.x++;
		}
		else if(direction.equals("down")) {
			ret.y--;
		}
		else {
			ret.x--;
		}
		return ret;
	}
	
	int numEmptyAdjacent(int[][] gameBoard) {
		int ret = 4;
		if((new CoordinatePair(x, y + 1)).isValid()) {
			if(gameBoard[x][y + 1] != Constants.EMPTYSQUARE && gameBoard[x][y + 1] != Constants.FOOD) {
				ret--;
			}
		}
		else {
			ret--;
		}
		if((new CoordinatePair(x, y - 1)).isValid()) {
			if(gameBoard[x][y - 1] != Constants.EMPTYSQUARE && gameBoard[x][y - 1] != Constants.FOOD) {
				ret--;
			}
		}
		else {
			ret--;
		}
		if((new CoordinatePair(x + 1, y)).isValid()) {
			if(gameBoard[x + 1][y] != Constants.EMPTYSQUARE && gameBoard[x + 1][y] != Constants.FOOD) {
				ret--;
			}
		}
		else {
			ret--;
		}
		if((new CoordinatePair(x - 1, y + 1)).isValid()) {
			if(gameBoard[x - 1][y] != Constants.EMPTYSQUARE && gameBoard[x - 1][y] != Constants.FOOD) {
				ret--;
			}
		}
		else {
			ret--;
		}
		return ret;
	}
	
	String moveWithMostEmptyAdjacent(int[][] gameBoard) {
		String move = "up";
		int mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
		if(mostEmpty < newAdjacent("right").numEmptyAdjacent(gameBoard)) {
			move = "right";
		}
		if(mostEmpty < newAdjacent("down").numEmptyAdjacent(gameBoard)) {
			move = "down";
		}
		if(mostEmpty < newAdjacent("left").numEmptyAdjacent(gameBoard)) {
			move = "left";
		}
		
		return move;
	}
}