package com.example.huy.pong2nguyenhu21;

import android.graphics.*;
import android.view.MotionEvent;
import java.util.*;



/**
 * This class animates the behavior of the ball
 * Also includes the AI paddle
 * AI paddle is on the left and user paddle is on the right
 * @author Huy Nguyen
 * @version March 2018
 */

public class BallAnimator implements Animator {
    private final int BALL_RADIUS = 30; //Set radius of ball
    private final float AI_PADDLE_SPEED = 15;//Set how fast the computer's paddle move
    private int playerScore, AIScore;//Variables of scores for player and computer
    private Random ranNum = new Random(); //Random number generator
    //Instance variables of x position, y position, the change in x, change in y and the speed
    private float ballXPos, ballYPos, changeInY, changeInX, ballSpeedX, ballSpeedY,
            paddleHeight, paddleWidth, touchX, touchY, newY, AIPaddlePos;
    private Paint paint = new Paint(); //paint for the ball, walls, and paddle
    private boolean isContinue; //boolean to see if the player wants to continue
    /**
     * ctor
     * Initialize the instance variables
     */
    public BallAnimator(){
        //Initialize the scores
        playerScore =  0;
        AIScore = 0;
        //Initialize the height and width of the paddle(the default will be the smaller paddle);
        paddleHeight = 240;
        paddleWidth = 60;
        //Initialize starting position for A.I paddle
        AIPaddlePos = 600;
        //Initialize paddle starting position
        newY = 500;
        //Pick a random starting position for the ball
        ballXPos = ranNum.nextInt(1000) + 500;
        ballYPos = ranNum.nextInt(500) + 500;
        //Pick a random direction and speed of the ball
        randomize();
        //set paint color to white
        paint.setColor(Color.WHITE);
        //Set continue to true
        isContinue = true;
    }
    /**
     * Set a random speed from 1 to 2
     * @return float of speed
     */
    public float randomBallSpeed(){
        return 1 + (float)Math.random();
    }
    /**
     * Randomize direction and speed of the ball
     */
    public void randomize(){
        //Pick a random initial speed
        ballSpeedX = 1 + ranNum.nextInt(2) + (float)Math.random();
        ballSpeedY = 1 + ranNum.nextInt(2) + (float)Math.random();
        //Pick random Y direction
        if(ranNum.nextInt(2) == 1){
            changeInY = 10 * ballSpeedY;
        }
        else {
            changeInY = -10 * ballSpeedY;
        }
        //Pick random X direction
        if(ranNum.nextInt(2) == 1){
            changeInX = 10 * ballSpeedX;
        }
        else {
            changeInX = -10 * ballSpeedX;
        }
    }
    /**
     * Draw the the top and bottom walls of the game
     * Also draw the center art thingy
     * @param canvas  canvas to be drawn on
     */
    public void drawWalls(Canvas canvas) {
        //Draw top wall
        canvas.drawRect(0.0f, 0.0f, (float)canvas.getWidth(), 60.0f, paint);
        //Draw bottom wall
        canvas.drawRect(0.0f, (float)(canvas.getHeight() - 60), canvas.getWidth(), canvas.getHeight(), paint);
        //Draw the center art thingy
        canvas.drawRect((float)(canvas.getWidth()/2 - 10), 0.0f, (float)(canvas.getWidth()/2 + 10), canvas.getHeight(), paint);
        //Set the stroke so that it only draw the outline of the circle
        paint.setStyle(Paint.Style.STROKE);
        //Set the thickness of the outline of the circle
        paint.setStrokeWidth(20.0f);
        //Draw the unfilled circle
        canvas.drawCircle((float)canvas.getWidth()/2, (float)canvas.getHeight()/2, 300.0f, paint);
        //Set the stroke back to normal
        paint.setStyle(Paint.Style.FILL);
    }
    /**
     * Draw the correct paddle for the game
     * @param canvas canvas to be drawn on
     */
    public void drawPlayerPaddle(Canvas canvas) {
        //Check to see if it's the big paddle that has to be drawn, draw the small one otherwise
        if(touchX >= canvas.getWidth() - (paddleWidth + 400)){
            newY = touchY;
        }
        if(newY - paddleHeight/2 < 60) {
            newY = paddleHeight/2 + 60;
        }
        if(newY + paddleHeight/2 >= canvas.getHeight() - 60){
            newY = canvas.getHeight() - paddleHeight/2 - 60;
        }
        //Draw the player's paddle
        canvas.drawRect((float)(canvas.getWidth() - paddleWidth), newY - paddleHeight/2,
                (float)(canvas.getWidth()), newY + paddleHeight/2, paint);
    }
    /**
     * Draw the AI paddle with the correct function
     * @param canvas canvas to be drawn on
     */
    public void drawAIPaddle(Canvas canvas){
        /**
         * AI paddle works by checking where the ball is and increment it either up
         * or down until it reaches where the ball is
         */
        if(AIPaddlePos < ballYPos){
            AIPaddlePos += AI_PADDLE_SPEED;
        }
        if(AIPaddlePos > ballYPos){
            AIPaddlePos -= AI_PADDLE_SPEED;
        }
        //Draw the paddle
        canvas.drawRect(0.0f, AIPaddlePos - 120, 60.0f, AIPaddlePos + 120, paint);
    }
    /**
     * Animates the ball
     * Check collisions of the ball and change direction appropriately
     * Note: Every time the ball hit the wall, it slows down hence the 0.99
     * @param canvas canvas to be drawn on
     */
    public void ballAnimation(Canvas canvas) {
        //If the ball hit the top wall, move it down
        if((ballYPos - BALL_RADIUS) < 60) {
            changeInY = 10 * randomBallSpeed();
        }
        //If the ball hit the bottom wall, move it up
        if((ballYPos + BALL_RADIUS) > canvas.getHeight()- 60) {
            changeInY = -10 * randomBallSpeed();
        }
        //If the ball hit the AI/Computer's paddle
        if((ballXPos - BALL_RADIUS) < 60) {
            if ((ballYPos >= AIPaddlePos - 120) && (ballYPos <= AIPaddlePos + 120)){
                changeInX = 10 * randomBallSpeed();
            }
        }
        /**
         * Check if it hits the paddle, move it in the other direction
         */
        if((ballXPos + BALL_RADIUS) > canvas.getWidth() - paddleWidth){
                if((ballYPos >= newY - paddleHeight/2) && (ballYPos <= newY + paddleHeight/2)) {
                    changeInX = -10 * randomBallSpeed();
                }
        }
        /**
         * If the ball goes out on the player's side, the AI gets a point
         * Put the ball back in the frame with random position, direction, and speed
         */
        if(ballXPos > canvas.getWidth()){
            AIScore++;
            isContinue = false;
            ballXPos = ranNum.nextInt(1000) + 500;
            ballYPos = ranNum.nextInt(500) + 500;
            randomize();
        }
        /**
         * If ball goes out on the AI side, the player gets a point
         * Put the ball back in the frame with random position, direction, and speed
         */
        if(ballXPos < 0){
            playerScore++;
            isContinue = false;
            ballXPos = ranNum.nextInt(1000) + 500;
            ballYPos = ranNum.nextInt(500) + 500;
            randomize();
        }
        //Move the ball by changing the positions
        ballYPos += changeInY;
        ballXPos += changeInX;
        //Draw ball
        canvas.drawCircle(ballXPos, ballYPos, BALL_RADIUS, paint);
    }
    /**
     * Print the score of the player and the computer on the screen
     * @param canvas canvas to be drawn on
     */
    public void printScores(Canvas canvas){
        //Set the text size
        paint.setTextSize(200.0f);
        //Print the player's score
        canvas.drawText(Integer.toString(playerScore), canvas.getWidth() - canvas.getWidth()/4, canvas.getHeight()/6, paint);
        //Print the AI/Computer score
        canvas.drawText(Integer.toString(AIScore), canvas.getWidth()/2 - canvas.getWidth()/4, canvas.getHeight()/6, paint);
    }
    /**
     * Set the paddle to big
     */
    public void setPaddleBig() {
        paddleHeight = 600;
        paddleWidth = 150;
    }
    /**
     * Set paddle to small
     */
    public void setPaddleSmall() {
        paddleHeight = 240;
        paddleWidth = 60;
    }
    public void setContinue(){
        isContinue = true;
    }
    /**
     * Getter of frame rate
     * @return frame rate
     */
    @Override
    public int interval() {
        return 10;
    }
    /**
     * Get the background color
     * @return background color
     */
    @Override
    public int backgroundColor() {
        return Color.BLACK;
    }
    /**
     * See if the game is pause(it never does)
     * @return true if pause, false otherwise
     */
    @Override
    public boolean doPause() {
        return false;
    }
    /**
     * Method to quit the game
     * @return true to quit, false to not
     */
    @Override
    public boolean doQuit() {
        return false;
    }
    /**
     * Draw the walls
     * Draw the paddles
     * Print the scores
     * Draw the ball along with its animation
     * @param canvas canvas to be drawn on
     */
    @Override
    public void tick(Canvas canvas) {
        //Draw the walls
        drawWalls(canvas);
        //draw the player's paddle
        drawPlayerPaddle(canvas);
        //draw AI/Computer paddle
        drawAIPaddle(canvas);
        //Print the scores
        printScores(canvas);
        //Animation of the ball
        if(isContinue) {
            ballAnimation(canvas);
        }
        else if(!isContinue) {
            //Set size of text
            paint.setTextSize(100.0f);
            //Draw the text "Press Continue"
            canvas.drawText("Press Continue", canvas.getWidth() / 2 - 200, canvas.getHeight() / 4, paint);
        }
    }
    /**
     * Touch events to get where it's being touch to change the position of the paddle
     * @param event a MotionEvent describing the touch
     */
    @Override
    public void onTouch(MotionEvent event) {
        //Get the the position wher it's being touch
        touchX = event.getX();
        touchY = event.getY();
    }
}
