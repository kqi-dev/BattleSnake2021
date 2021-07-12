package com.battlesnake.sskt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class ControllerFunctions {
	public static Map<String, String> setupValues() {         
        Map<String, String> response = new HashMap<>();
        response.put("apiversion", "1");
        response.put("author", "keviqi");
        response.put("color", "#33ffff");     
        response.put("head", "evil");  
        response.put("tail", "rattle");  
        return response;
    }
	public static Map<String, String> move(JsonNode moveRequest) {
		Constants.setWidth(moveRequest.get("board").get("width").asInt());
		Constants.setHeight(moveRequest.get("board").get("height").asInt());
		Constants.setHealth(moveRequest.get("you").get("health").asInt());
		CoordinatePair myLocation = null;
		
        int[][] gameBoard = new int[Constants.WIDTH][Constants.HEIGHT];
        
        //Set up GameBoard and ArrayLists
        ArrayList<ArrayList<CoordinatePair>> snakeCoordinates = new ArrayList<ArrayList<CoordinatePair>>();
        ArrayList<CoordinatePair> foodCoordinates = new ArrayList<CoordinatePair>();
        
        ArrayList<CoordinatePair> mySnake = new ArrayList<CoordinatePair>();
        JsonNode mySnakeHead = moveRequest.get("you").get("head");
        myLocation = new CoordinatePair(mySnakeHead.get("x").asInt(), mySnakeHead.get("y").asInt());
        for(JsonNode segment : moveRequest.get("you").get("body")) {
        	mySnake.add(new CoordinatePair(segment.get("x").asInt(), segment.get("y").asInt()));
        	gameBoard[segment.get("x").asInt()][segment.get("y").asInt()] = Constants.SNAKEBODY;
        }
        
        snakeCoordinates.add(mySnake);
        
        JsonNode otherSnakes = moveRequest.get("board").get("snakes");
        for(JsonNode snake : otherSnakes) {
        	ArrayList<CoordinatePair> tempSnake = new ArrayList<CoordinatePair>();
        	if(snake.get("id").equals(moveRequest.get("you").get("id"))) {
        		continue;
        	}
        	for(JsonNode segment : snake.get("body")) {
        		int xCoord = segment.get("x").asInt();
        		int yCoord = segment.get("y").asInt();
        		tempSnake.add(new CoordinatePair(xCoord, yCoord));
        		gameBoard[xCoord][yCoord] = Constants.ENEMYBODY;
        	}
        	snakeCoordinates.add(tempSnake);
        }
        /*
         * Loading food data
         */
        JsonNode foodData = moveRequest.get("board").get("food");
        for(JsonNode foodLocation : foodData) {
        	int foodX = foodLocation.get("x").asInt();
        	int foodY = foodLocation.get("y").asInt();
        	foodCoordinates.add(new CoordinatePair(foodX, foodY));
        	gameBoard[foodX][foodY] = Constants.FOOD;
        }
        
        foodCoordinates = UtilFunctions.sortByProximity(foodCoordinates, myLocation); //sort food by proximity to player location
        
        /*
         * 
         * MOVE DECISION
         * 
         */
        
		String move = myLocation.moveWithMostEmptyAdjacent(gameBoard);
		System.out.println("At the start, I want to move " + move);
        
        //if we are closer to a food item than anyone else, meaning we can get there first, then path there
        CoordinatePair target = null;
        for(CoordinatePair food : foodCoordinates) {
        	if(UtilFunctions.isClosestTo(snakeCoordinates, food)) {
        		target = food;
        		break;
        	}
        }
        if(target == null) {
          for(CoordinatePair food : foodCoordinates) {
        	if(UtilFunctions.isClosestEqualTo(snakeCoordinates, food)) {
        		target = food;
        		break;
        	}
        }
        }
        if(target != null) {
        	move = UtilFunctions.moveToTarget(gameBoard, snakeCoordinates.get(0).get(0), target);
        	System.out.println("I have decided to go towards block " + target.x +", " + target.y);
    		System.out.println("To go there I will move in direction: " + move);
        }
        
        //check if we are going down a dangerous tunnel or walking into the head of an enemy we don't want to collide with
        if(!move.equals("")) {
        	if(UtilFunctions.floodfill(myLocation.newAdjacent(move), gameBoard) < snakeCoordinates.get(0).size() + 1) {
        		System.out.println("Right now I want to move " + move + ", but after checking with floodfill...");
        		move = myLocation.moveWithMostEmptyAdjacent(gameBoard);
        		System.out.println("I have decided to move " + move);
        	}
        	
        	if(myLocation.newAdjacent(move).isAdjacentToThreatEnemyHeads(snakeCoordinates)) {
        		System.out.println("ENEMY THREAT ADJACENT -----------------------------");
        		System.out.println("Right now I want to move " + move + ", but after checking for different most adjacent...");
        		move = myLocation.diffMoveWithMostEmptyAdjacent(gameBoard, move);
        		System.out.println("I have decided to move " + move);
        	}
        }
        
        System.out.println("This is where I think the heads are");
        System.out.println("I am at " + myLocation.x + ", " + myLocation.y);
        for(int i = 0; i < snakeCoordinates.size(); i++){
        	System.out.println("Snake of index " + i + " has a head at: " + snakeCoordinates.get(i).get(0).x + ", " + snakeCoordinates.get(i).get(0).y);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("move", move);
        return response;
    }
}
