package com.battlesnake.sskt;

import java.util.ArrayList;

public class UtilFunctions {
	static int[][] floodfill(CoordinatePair head) { //probably a useless function after realizing you can do this mathematically due to movement constraints
		int[][] ret = new int[Constants.WIDTH][Constants.HEIGHT];
		floodfillHelper(new CoordinatePair(head.x, head.y), 0, ret);

		return ret;
	}
  
	private static void floodfillHelper(CoordinatePair head, int dist, int[][] ret) {
		if(head.x < 0 || head.x >= Constants.WIDTH || head.y < 0 || head.y >= Constants.HEIGHT) {
			return;
		}
		if(ret[head.x][head.y] != 0) {
			return;
		}

		ret[head.x][head.y] = dist;

		floodfillHelper(new CoordinatePair(head.x + 1, head.y), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x - 1, head.y), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x, head.y + 1), dist + 1, ret);
		floodfillHelper(new CoordinatePair(head.x, head.y - 1), dist + 1, ret);
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
}