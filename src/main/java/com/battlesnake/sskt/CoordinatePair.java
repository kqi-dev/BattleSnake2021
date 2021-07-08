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
		CoordinatePair ret;
		if(direction.equals("up")) {
			ret = new CoordinatePair(x, y + 1);
		}
		else if(direction.equals("right")) {
			ret = new CoordinatePair(x + 1, y);
		}
		else if(direction.equals("down")) {
			ret = new CoordinatePair(x, y - 1);
		}
		else {
			ret = new CoordinatePair(x - 1, y);
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
		if((new CoordinatePair(x - 1, y)).isValid()) {
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
		ArrayList<String> validMoves = new ArrayList<String>();
		String move = "right";
		for(int i = 0; i < Constants.CARDINALMOVEMENTS.length; i++) {
			CoordinatePair tempCheck = newAdjacent(Constants.CARDINALMOVEMENTS[i]);
			if(tempCheck.isValid() && tempCheck.canMoveTo(gameBoard)) {
				validMoves.add(Constants.CARDINALMOVEMENTS[i]);
			}
		}
		if(validMoves.size() == 0) {
			System.out.println("THIS MEANS THERE ARE NO VALID MOVES TO MAKE AND DEATH SHOULD BE IMMINENT");
			return move;
		}
		else {
			move = validMoves.get(0);
			int mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
			for(int i = 1; i < validMoves.size(); i++) {
				CoordinatePair targetTemp = newAdjacent(validMoves.get(i));
				if(targetTemp.numEmptyAdjacent(gameBoard) > mostEmpty) {
					move = validMoves.get(i);
					mostEmpty = targetTemp.numEmptyAdjacent(gameBoard);
				}
			}
		}
		return move;
	}
	String diffMoveWithMostEmptyAdjacent(int[][] gameBoard, String direction) {
		ArrayList<String> validMoves = new ArrayList<String>();
		String move = "right";
		for(int i = 0; i < Constants.CARDINALMOVEMENTS.length; i++) {
			CoordinatePair tempCheck = newAdjacent(Constants.CARDINALMOVEMENTS[i]);
			if(tempCheck.isValid() && tempCheck.canMoveTo(gameBoard) && !direction.equals(Constants.CARDINALMOVEMENTS[i])) {
				validMoves.add(Constants.CARDINALMOVEMENTS[i]);
			}
		}
		if(validMoves.size() == 0) {
			System.out.println("THIS MEANS THERE ARE NO VALID MOVES TO MAKE AND DEATH SHOULD BE IMMINENT");
			return move;
		}
		else {
			move = validMoves.get(0);
			int mostEmpty = newAdjacent(move).numEmptyAdjacent(gameBoard);
			for(int i = 1; i < validMoves.size(); i++) {
				CoordinatePair targetTemp = newAdjacent(validMoves.get(i));
				if(targetTemp.numEmptyAdjacent(gameBoard) > mostEmpty) {
					move = validMoves.get(i);
					mostEmpty = targetTemp.numEmptyAdjacent(gameBoard);
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
	boolean isAdjacentToThreatEnemyHeads(ArrayList<ArrayList<CoordinatePair>> enemies) {
		for(int i = 1; i < enemies.size(); i++) { //start at 1 because 0 is your own snake
			CoordinatePair enemyHead = enemies.get(i).get(0);
			System.out.println("Checking if destination: " + x + ", " + y + " is adjacent to enemy head: " +enemyHead.x + ", " + enemyHead.y);
			if(Math.abs(enemyHead.x - x) <= 1 || Math.abs(enemyHead.y - y) <= 1) {
				if(enemies.get(i).size() >= enemies.get(0).size()) {
					return true;
				}
			}
		}
		return false;
	}
	
}