package com.battlesnake.sskt;

import java.util.ArrayList;

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
	
	boolean canMoveTo(int[][] gameBoard) {
		if(gameBoard[x][y] == Constants.EMPTYSQUARE || gameBoard[x][y] == Constants.FOOD) {
			return true;
		}
		return false;
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
		boolean moveIsValid = false;
		if(newAdjacent(move).isValid() && newAdjacent(move).canMoveTo(gameBoard)) {
			moveIsValid = true;
		}
		
		CoordinatePair temp;
		
		temp = newAdjacent("right");
		if(temp.isValid() && temp.canMoveTo(gameBoard)) {
			if(moveIsValid == false) {
				move = "right";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "right";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}
		
		temp = newAdjacent("down");
		if(temp.isValid() && temp.canMoveTo(gameBoard)) {
			if(moveIsValid == false) {
				move = "down";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "down";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}

		temp = newAdjacent("left");
		if(temp.isValid() && temp.canMoveTo(gameBoard)) {
			if(moveIsValid == false) {
				move = "left";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "left";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}
		
		return move;
	}
	String diffMoveWithMostEmptyAdjacent(int[][] gameBoard, String direction) {
		String move = "up";
		int mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
		boolean moveIsValid = false;
		if(newAdjacent(move).isValid() && newAdjacent(move).canMoveTo(gameBoard)) {
			moveIsValid = true;
		}
		
		CoordinatePair temp;
		
		temp = newAdjacent("right");
		if(temp.isValid() && temp.canMoveTo(gameBoard) && !(direction.equals("right"))) {
			if(moveIsValid == false) {
				move = "right";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "right";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}
		
		temp = newAdjacent("down");
		if(temp.isValid() && temp.canMoveTo(gameBoard) && !(direction.equals("down"))) {
			if(moveIsValid == false) {
				move = "down";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "down";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}

		temp = newAdjacent("left");
		if(temp.isValid() && temp.canMoveTo(gameBoard) && !(direction.equals("left"))) {
			if(moveIsValid == false) {
				move = "left";
				mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				moveIsValid = true;
			}
			else {
				if(mostEmpty < temp.numEmptyAdjacent(gameBoard)) {
					move = "left";
					mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
				}
			}
		}
		
		return move;
	}
	boolean isAdjacentToEnemyHeads(ArrayList<ArrayList<CoordinatePair>> enemies) {
		for(int i = 1; i < enemies.size(); i++) { //start at 1 because 0 is your own snake
			CoordinatePair enemyHead = enemies.get(i).get(0);
			if(Math.abs(enemyHead.x - x) <= 1 || Math.abs(enemyHead.y - y) <= 1) {
				return true;
			}
		}
		return false;
	}
	
}