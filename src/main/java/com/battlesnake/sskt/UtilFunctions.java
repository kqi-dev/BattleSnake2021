package com.battlesnake.sskt;

import java.util.ArrayList;

public class UtilFunctions {
	static int floodfill(CoordinatePair head, int[][] gameBoard) { //probably a useless function after realizing you can do this mathematically due to movement constraints
    boolean[][] visited = new boolean[Constants.WIDTH][Constants.HEIGHT];

	  return floodfillHelper(new CoordinatePair(head.x, head.y), gameBoard, visited);
	}
  
	private static int floodfillHelper(CoordinatePair head, int[][] gameBoard, boolean[][] visited) {
		if(head.x < 0 || head.x >= Constants.WIDTH || head.y < 0 || head.y >= Constants.HEIGHT) {
			return 0;
		}
		if(visited[head.x][head.y] == true) {
			return 0;
		}
    else {
      if(gameBoard[head.x][head.y] != Constants.EMPTYSQUARE && gameBoard[head.x][head.y] != Constants.FOOD) {
        return 0;
      }
    }
    visited[head.x][head.y] = true;
    return 1 + 
      floodfillHelper(new CoordinatePair(head.x + 1, head.y), gameBoard, visited) +
		  floodfillHelper(new CoordinatePair(head.x - 1, head.y), gameBoard, visited) +
		  floodfillHelper(new CoordinatePair(head.x, head.y + 1), gameBoard, visited) +
		  floodfillHelper(new CoordinatePair(head.x, head.y - 1), gameBoard, visited);
	}
	
	static boolean isClosestTo(ArrayList<ArrayList<CoordinatePair>> SnakeCoordinates, CoordinatePair food) {
		if(!(SnakeCoordinates.size() > 1)) {
			return true;
		}
		for(int i = 1; i < SnakeCoordinates.size(); i++) {
			if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) >= SnakeCoordinates.get(i).get(0).cardinalDistanceTo(food)) {
				return false;
			}
		}
		return true;
	}

  static boolean isClosestEqualTo(ArrayList<ArrayList<CoordinatePair>> SnakeCoordinates, CoordinatePair food) {
		if(!(SnakeCoordinates.size() > 1)) {
			return true;
		}
		for(int i = 1; i < SnakeCoordinates.size(); i++) {
			if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) > SnakeCoordinates.get(i).get(0).cardinalDistanceTo(food)) {
				return false;
			}
      else if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) == SnakeCoordinates.get(i).get(0).cardinalDistanceTo(food)) {
        if(SnakeCoordinates.get(i).size() < SnakeCoordinates.get(0).size()) {
          continue;
        }
        if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) < 3) {
          return false;
        }
      }
		}
		return true;
	}
	
	static String moveToTarget(int[][] gameBoard, CoordinatePair snakeHead, CoordinatePair target) {
		if(snakeHead.x == target.x && snakeHead.y == target.y) {
			return safeMove(gameBoard, snakeHead, "random");
		}
		if(snakeHead.x == target.x && snakeHead.y < target.y) {
			return safeMove(gameBoard, snakeHead, "up");
		}
		if(snakeHead.x == target.x && snakeHead.y > target.y) {
			return safeMove(gameBoard, snakeHead, "down");
		}
		if(snakeHead.x < target.x && snakeHead.y == target.y) {
			return safeMove(gameBoard, snakeHead, "right");
		}
		if(snakeHead.x > target.x && snakeHead.y == target.y) {
			return safeMove(gameBoard, snakeHead, "left");
		}
		if(snakeHead.x < target.x && snakeHead.y < target.y) {
			if(0.5 < Math.random()) {
				return safeMove(gameBoard, snakeHead, "right");
			}
			else {
				return safeMove(gameBoard, snakeHead, "up");
			}
		}
		if(snakeHead.x < target.x && snakeHead.y > target.y) {
			if(0.5 < Math.random()) {
				return safeMove(gameBoard, snakeHead, "right");
			}
			else {
				return safeMove(gameBoard, snakeHead, "down");
			}
		}
		if(snakeHead.x > target.x && snakeHead.y < target.y) {
			if(0.5 < Math.random()) {
				return safeMove(gameBoard, snakeHead, "left");
			}
			else {
				return safeMove(gameBoard, snakeHead, "up");
			}
		}
		if(snakeHead.x > target.x && snakeHead.y > target.y) {
			if(0.5 < Math.random()) {
				return safeMove(gameBoard, snakeHead, "left");
			}
			else {
				return safeMove(gameBoard, snakeHead, "down");
			}
		}
		return "null";
	}
	
	static String safeMove(int[][] gameBoard, CoordinatePair snakeHead, String direction) {
		switch(direction) {
		case "up":
			if(isValidMove(gameBoard, snakeHead, "up")) {
				return "up";
			}
			else {
				return safeMove(gameBoard, snakeHead, "right");
			}
		case "right":
			if(isValidMove(gameBoard, snakeHead, "right")) {
				return "right";
			}
			else {
				return safeMove(gameBoard, snakeHead, "down");
			}
		case "down":
			if(isValidMove(gameBoard, snakeHead, "down")) {
				return "down";
			}
			else {
				return safeMove(gameBoard, snakeHead, "left");
			}
		case "left":
			if(isValidMove(gameBoard, snakeHead, "left")) {
				return "left";
			}
			else {
				return safeMove(gameBoard, snakeHead, "up");
			}
		case "random":
			return Constants.CARDINALMOVEMENTS[(int)(Math.random() * 4)];
		}
		return "null";
	}
	
	static boolean isValidMove(int[][] gameBoard, CoordinatePair snakeHead, String direction) {
		CoordinatePair newSquare;
		switch(direction) {
		case "up":
			newSquare = new CoordinatePair(snakeHead.x, snakeHead.y + 1);
			if(newSquare.isValid() && (gameBoard[snakeHead.x][snakeHead.y + 1] == 0 || gameBoard[snakeHead.x][snakeHead.y + 1] == 5)) {
				return true;
			}
			return false;
		case "right":
			newSquare = new CoordinatePair(snakeHead.x + 1, snakeHead.y);
			if(newSquare.isValid() && (gameBoard[snakeHead.x + 1][snakeHead.y] == 0 || gameBoard[snakeHead.x + 1][snakeHead.y] == 5)) {
				return true;
			}
			return false;
		case "down":
			newSquare = new CoordinatePair(snakeHead.x, snakeHead.y - 1);
			if(newSquare.isValid() && (gameBoard[snakeHead.x][snakeHead.y - 1] == 0 || gameBoard[snakeHead.x][snakeHead.y - 1] == 5)) {
				return true;
			}
			return false;
		case "left":
			newSquare = new CoordinatePair(snakeHead.x - 1, snakeHead.y);
			if(newSquare.isValid() && (gameBoard[snakeHead.x - 1][snakeHead.y] == 0 || gameBoard[snakeHead.x - 1][snakeHead.y] == 5)) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}
	
	static ArrayList<CoordinatePair> sortByProximity(ArrayList<CoordinatePair> foodCoordinates, CoordinatePair location) {
		ArrayList<CoordinatePair> sorted = new ArrayList<CoordinatePair>();
		while(foodCoordinates.size() > 0) {
			CoordinatePair closest = foodCoordinates.get(0);
			int index = 0;
			for(int i = 1; i < foodCoordinates.size(); i++) {
				if(closest.cardinalDistanceTo(location) > foodCoordinates.get(i).cardinalDistanceTo(location)) {
					closest = foodCoordinates.get(i);
					index = i;
				}
			}
			sorted.add(closest);
			foodCoordinates.remove(index);
		}
		return sorted;
	}
	
	/*static String[] getDirectionsTo(CoordinatePair location, CoordinatePair target) {
		String[] ret;
		if(location.x == target.x || location.y == target.y) {
			ret = new String[1];
		}
		else {
			ret = new String[2];
		}
		if(ret.)
		return ret;
	}*/
}