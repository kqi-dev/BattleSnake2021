package com.battlesnake.sskt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;

public class Controller {
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Handler HANDLER = new Handler();
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    /**
     * Main entry point.
     *
     * @param args are ignored.
     */
    public static void main(String[] args) {
        String port = System.getProperty("PORT");
        if (port != null) {
            LOG.info("Found system provided port: {}", port);
        } else {
            LOG.info("Using default port: {}", port);
            port = "8080";
        }
        port(Integer.parseInt(port));
        get("/",  HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/start", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/move", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/end", HANDLER::process, JSON_MAPPER::writeValueAsString);
    }

    /**
     * Handler class for dealing with the routes set up in the main method.
     */
    public static class Handler {

        /**
         * For the start/end request
         */
        private static final Map<String, String> EMPTY = new HashMap<>();

        /**
         * Generic processor that prints out the request and response from the methods.
         *
         * @param req
         * @param res
         * @return
         */
        public Map<String, String> process(Request req, Response res) {
            try {
                JsonNode parsedRequest = JSON_MAPPER.readTree(req.body());
                String uri = req.uri();
                LOG.info("{} called with: {}", uri, req.body());
                Map<String, String> snakeResponse;
                if (uri.equals("/")) {
                    snakeResponse = index();
                } else if (uri.equals("/start")) {
                    snakeResponse = start(parsedRequest);
                } else if (uri.equals("/move")) {
                    snakeResponse = move(parsedRequest);
                } else if (uri.equals("/end")) {
                    snakeResponse = end(parsedRequest);
                } else {
                    throw new IllegalAccessError("Strange call made to the snake: " + uri);
                }
                LOG.info("Responding with: {}", JSON_MAPPER.writeValueAsString(snakeResponse));
                return snakeResponse;
            } catch (Exception e) {
                LOG.warn("Something went wrong!", e);
                return null;
            }
        }

    
        /**
         * This method is called everytime your Battlesnake is entered into a game.
         * 
         * Use this method to decide how your Battlesnake is going to look on the board.
         *
         * @return a response back to the engine containing the Battlesnake setup
         *         values.
         */
        public Map<String, String> index() {         
            return ControllerFunctions.setupValues();
        }

        /**
         * @param startRequest a JSON data map containing the information about the game
         *                     that is about to be played.
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> start(JsonNode startRequest) {
            LOG.info("START");
            return EMPTY;
        }

        /**
         * This method is called on every turn of a game. It's how your snake decides
         * where to move.
         * 
         * Valid moves are "up", "down", "left", or "right".
         *
         * @param moveRequest a map containing the JSON sent to this snake. Use this
         *                    data to decide your next move.
         * @return a response back to the engine containing Battlesnake movement values.
         */
        public Map<String, String> move(JsonNode moveRequest) {
            try {
                LOG.info("Data: {}", JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(moveRequest));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            
            return ControllerFunctions.move(moveRequest);
        }

        /**
         * This method is called when a game your Battlesnake was in ends.
         * 
         * It is purely for informational purposes, you don't have to make any decisions
         * here.
         *
         * @param endRequest a map containing the JSON sent to this snake. Use this data
         *                   to know which game has ended
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> end(JsonNode endRequest) {

            LOG.info("END");
            return EMPTY;
        }

        /*
        Function to determine if a direction is invalid to move in
        Invalid here meaning that it will result in immediate death
        */
        public boolean isValid(int headX, int headY, int[][] gameBoard, String move){
          int targetX = headX;
          int targetY = headY;

          if(move.equals("up")){
            targetY++;
          }
          else if(move.equals("down")){
            targetY--;
          }
          else if(move.equals("left")){
            targetX--;
          }
          else{
            targetX++;
          }

          if(targetX < 0 || targetX >= gameBoard.length){
            return false;
          }
          if(targetY < 0 || targetY >= gameBoard[0].length){
            return false;
          }

          //if target block is occupied 
          if(gameBoard[targetX][targetY] == 1){
            return false;
          }
          else if(gameBoard[targetX][targetY] == 2){
            return false;
          }
          return true;
        }

        public String decideDirection(int headX, int headY, ArrayList<Integer> otherHeadsX, ArrayList<Integer> otherHeadsY, int[][] gameBoard){
          String[] possibleMoves = { "up", "down", "left", "right"};
          int[] movePriority = {1, 1, 1, 1};

          for(int i = 0; i < possibleMoves.length; i++){
            if(!isValid(headX, headY, gameBoard, possibleMoves[i])){
              movePriority[i] = -99;
            }
          }
          
          //up
          movePriority[0] -= (4 - validMoves(headX, headY + 1, gameBoard));
          //down
          movePriority[1] -= (4 - validMoves(headX, headY - 1, gameBoard));
          //left
          movePriority[2] -= (4 - validMoves(headX - 1, headY, gameBoard));
          //right
          movePriority[3] -= (4 - validMoves(headX + 1, headY, gameBoard));

          for(int i = 0; i < otherHeadsX.size(); i++){
            int[] priorityChange = makeProximityArray(headX, headY, otherHeadsX.get(i), otherHeadsY.get(i));
            for(int j = 0; j < priorityChange.length; j++){
              movePriority[j] -= priorityChange[j];
            }
          }

          int highestPriorityIndex = 0;
          int highestPriority = movePriority[0];

          for(int i = 1; i < possibleMoves.length; i++){
            if(movePriority[i] > highestPriority){
              highestPriority = movePriority[i];
              highestPriorityIndex = i;
            }
            if(movePriority[i] == highestPriority){
              Random r = new Random();
              if(r.nextFloat() > 0.5){
                highestPriority = movePriority[i];
                highestPriorityIndex = i;
              }
            }
          }

          return possibleMoves[highestPriorityIndex];
        }

        public int validMoves(int headX, int headY, int[][] gameBoard){
          int validMoveCount = 4;
          //up
          if(!isValid(headX, headY + 1, gameBoard, "up")){
            validMoveCount--;
          }
          //down
          if(!isValid(headX, headY - 1, gameBoard, "down")){
            validMoveCount--;
          }
          //left
          if(!isValid(headX - 1, headY, gameBoard, "left")){
            validMoveCount--;
          }
          //right
          if(!isValid(headX + 1, headY, gameBoard, "right")){
            validMoveCount--;
          }
          return validMoveCount;
        }

        public int[] makeProximityArray(int headX, int headY, int otherX, int otherY){
            int[] ret = {0, 0, 0, 0};
            if(headY - otherY >= -3){
              if(headY - otherY >= -2){
                ret[0] -= 3;
              }
              else{
                ret[0] -= 1;
              }
            }
            else if(headY - otherY <= 3){
              if(headY - otherY <= 2){
                ret[1] -= 3;
              }
              else{
                ret[1] -= 1;
              }
            }
            if(headX - otherX <= 3){
              if(headX - otherX <= 2){
                ret[2] -= 3;
              }
              else{
                ret[2] -= 1;
              }
            }
            else if(headX - otherX >= -3){
              if(headX - otherX >= -2){
                ret[3] -= 3;
              }
              else{
                ret[3] -= 1;
              }
            }
            return ret;
        }
    }

}
