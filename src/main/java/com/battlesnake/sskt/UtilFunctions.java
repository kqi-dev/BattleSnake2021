package com.battlesnake.sskt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
    return 1 + floodfillHelper(new CoordinatePair(head.x + 1, head.y), gameBoard, visited) +
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
        if(SnakeCoordinates.get(0).get(0).cardinalDistanceTo(food) < 2) {
          return false;
        }
      }
		}
		return true;
	}
  
  	static String moveToTarget(int[][] gameBoard, CoordinatePair snakeHead, CoordinatePair target) { //now with BFS... but better!
  		boolean[][] visited = new boolean[Constants.WIDTH][Constants.HEIGHT];
  		String[][] pairs = new String[Constants.WIDTH][Constants.HEIGHT];
  		CoordinatePair pairTemp = new CoordinatePair(snakeHead.x, snakeHead.y);
  		pairTemp.newAdjacent("left");
  		if(pairTemp.isValid()) {
  			pairs[pairTemp.x][pairTemp.y] = "left";
  		}
  		pairTemp.newAdjacent("right");
  		if(pairTemp.isValid()) {
  			pairs[pairTemp.x][pairTemp.y] = "right";
  		}
  		pairTemp.newAdjacent("down");
  		if(pairTemp.isValid()) {
  			pairs[pairTemp.x][pairTemp.y] = "down";
  		}
  		pairTemp.newAdjacent("up");
  		if(pairTemp.isValid()) {
  			pairs[pairTemp.x][pairTemp.y] = "up";
  		}
  		Queue<CoordinatePair> queue = new LinkedList<CoordinatePair>();
  		queue.add(snakeHead);
  		
  		while(!queue.isEmpty()) {
        System.out.println("I AM LOOPING");
  			CoordinatePair popped = queue.poll();
  			if(popped.x == target.x && popped.y == target.y) {
  				return pairs[popped.x][popped.y];
  			}
  			else {
  				visited[popped.x][popped.y] = true; 
  				if(!popped.canMoveTo(gameBoard) && !(popped.x == snakeHead.x && popped.y == snakeHead.y)) {
  					continue;
  				}
  				CoordinatePair[] orderedPairs = orderedDirections(popped, target);
          System.out.println("ORDERED PAIRS IS ARRAY LENGTH " + orderedPairs.length);
  				for(int i = 0; i < orderedPairs.length; i++) {
  					if(orderedPairs[i].isValid() && visited[orderedPairs[i].x][orderedPairs[i].y] == false) {
  						queue.add(orderedPairs[i]);
  						if(orderedPairs[i].x == snakeHead.x && orderedPairs[i].y == snakeHead.y) {
  							pairs[orderedPairs[i].x][orderedPairs[i].y] = pairs[popped.x][popped.y];
  						}
  					}
  				}
  			}
  		}
  		
  		return "null";
  	}
  	
  	static CoordinatePair[] orderedDirections(CoordinatePair snakeHead, CoordinatePair target) {
  		CoordinatePair[] ret = new CoordinatePair[4];
  		if(snakeHead.x == target.x) {
  			if(snakeHead.y < target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("up"), snakeHead.newAdjacent("left"), snakeHead.newAdjacent("right"), snakeHead.newAdjacent("down")};
  			}
  			else if(snakeHead.y > target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("down"), snakeHead.newAdjacent("left"), snakeHead.newAdjacent("right"), snakeHead.newAdjacent("up")};
  			}
  			else {
  				return ret;
  			}
  		}
  		if(snakeHead.x > target.x) { //go left
  			if(snakeHead.y < target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("left"), snakeHead.newAdjacent("up"), snakeHead.newAdjacent("right"), snakeHead.newAdjacent("down")};
  			}
  			else if(snakeHead.y > target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("left"), snakeHead.newAdjacent("down"), snakeHead.newAdjacent("right"), snakeHead.newAdjacent("up")};
  			}
  			else {
  				return new CoordinatePair[] {snakeHead.newAdjacent("left"), snakeHead.newAdjacent("down"), snakeHead.newAdjacent("up"), snakeHead.newAdjacent("right")};
  			}
  		}
  		else if(snakeHead.x < target.x) {
  			if(snakeHead.y < target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("right"), snakeHead.newAdjacent("up"), snakeHead.newAdjacent("left"), snakeHead.newAdjacent("down")};
  			}
  			else if(snakeHead.y > target.y) {
  				return new CoordinatePair[] {snakeHead.newAdjacent("right"), snakeHead.newAdjacent("down"), snakeHead.newAdjacent("left"), snakeHead.newAdjacent("up")};
  			}
  			else {
  				return new CoordinatePair[] {snakeHead.newAdjacent("right"), snakeHead.newAdjacent("down"), snakeHead.newAdjacent("up"), snakeHead.newAdjacent("left")};
  			}
  		}
  		
  		return ret;
  	}
	
	static String moveToTargetOld(int[][] gameBoard, CoordinatePair snakeHead, CoordinatePair target) {
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