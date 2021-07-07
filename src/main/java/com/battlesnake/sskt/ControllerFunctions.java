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
        response.put("head", "shades");  
        response.put("tail", "rattle");  
        return response;
    }
	public static Map<String, String> move(JsonNode moveRequest) {
		Constants.setWidth(moveRequest.get("board").get("width").asInt());
		Constants.setHeight(moveRequest.get("board").get("height").asInt());
		Constants.setHealth(moveRequest.get("battlesnake").get("health").asInt());
		
        int[][] gameBoard = new int[Constants.WIDTH][Constants.HEIGHT];
        
        //Set up GameBoard and ArrayLists
        ArrayList<ArrayList<CoordinatePair>> snakeCoordinates = new ArrayList<ArrayList<CoordinatePair>>();
        ArrayList<CoordinatePair> foodCoordinates = new ArrayList<CoordinatePair>();
        
        ArrayList<CoordinatePair> mySnake = new ArrayList<CoordinatePair>();
        JsonNode mySnakeHead = moveRequest.get("you").get("head");
        mySnake.add(new CoordinatePair(mySnakeHead.get("x").asInt(), mySnakeHead.get("y").asInt()));
        gameBoard[mySnakeHead.get("x").asInt()][mySnakeHead.get("y").asInt()] = Constants.SNAKEHEAD;
        for(JsonNode segment : moveRequest.get("you").get("body")) {
        	mySnake.add(new CoordinatePair(segment.get("x").asInt(), segment.get("y").asInt()));
        	gameBoard[segment.get("x").asInt()][segment.get("y").asInt()] = Constants.SNAKEBODY;
        }
        
        snakeCoordinates.add(mySnake);
        
        JsonNode otherSnakes = moveRequest.get("board").get("snakes");
        for(JsonNode snake : otherSnakes) {
        	ArrayList<CoordinatePair> tempSnake = new ArrayList<CoordinatePair>();
        	int tempHeadX = snake.get("head").get("x").asInt();
        	int tempHeadY = snake.get("head").get("y").asInt();
        	tempSnake.add(new CoordinatePair(tempHeadX, tempHeadY)); //head
        	gameBoard[tempHeadX][tempHeadY] = Constants.ENEMYHEAD;
        	if(snake.get("id").equals(moveRequest.get("you").get("id"))) {
        		continue;
        	}
        	for(JsonNode segment : snake.get("body")) {
        		int xCoord = segment.get("x").asInt();
        		int yCoord = segment.get("y").asInt();
        		tempSnake.add(new CoordinatePair(xCoord, yCoord));
        		gameBoard[xCoord][yCoord] = Constants.ENEMYBODY;
        	}
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
        
        /*
         * 
         * MOVE DECISION
         * 
         */
		String move = "";
        
        //if we are closer to a food item than anyone else, meaning we can get there first, then path there
        CoordinatePair target = null;
        for(CoordinatePair food : foodCoordinates) {
        	if(UtilFunctions.isClosestTo(snakeCoordinates, food)) {
        		target = food;
        		break;
        	}
        }
        if(target != null) {
        	move = UtilFunctions.moveToTarget(gameBoard, snakeCoordinates.get(0).get(0), target);
        }
        

        Map<String, String> response = new HashMap<>();
        response.put("move", move);
        return response;
    }
}
