/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	
//amount of lives
	int lives = NTURNS;

//paddle y 
	private static final int PADDLE_Y = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
// delay
	private static final int DELAY = 5;
// start delay
	private static final int START_DELAY = 10000;
	private static double acc = 0.5;
	
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vy = 3.0;
	private double vx = rgen.nextDouble(1.0, 3.0);
	
	private int x = NBRICK_ROWS * NBRICKS_PER_ROW;
	
	int score = 0;
	
	GLabel labelLives;
	GLabel labelScore;
	AudioClip bounceClip;
	GRect brick;
	GRect paddle;
	GOval ball;
	GObject collider;
	
	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		playBreakout();
	}
	
	// this is body of program there is method which builds wall, paddle, ball, text and music 
	private void playBreakout() {
		bounceClip = MediaTools.loadAudioClip("bounce.au");
		buildWall();
		buildPaddle();
		buildBall();
		buildTxt();
	}
	
	/*
	 * this method builds wall, it gives us rects
	 * pre-condition: there was nothing
	 * post-condition: there are rects
	 */
	private void buildWall() {
		for (int j = 0; j < NBRICK_ROWS; j++) {
			for (int i = 0; i < NBRICKS_PER_ROW; i++) {
				brick = new GRect((BRICK_WIDTH + BRICK_SEP) * i, BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * j,
						BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				if (j < 2) {
					brick.setColor(Color.red);
				} else if (j >= 2 && j < 4) {
					brick.setColor(Color.orange);
				} else if (j >= 4 && j < 6) {
					brick.setColor(Color.yellow);
				} else if (j >= 6 && j < 8) {
					brick.setColor(Color.green);
				} else if (j >= 8 && j < 10) {
					brick.setColor(Color.cyan);
				}
				add(brick);
			}
		}
	}
	
	/*
	 * it builds paddle
	 * pre-condition: there is only wall
	 * post-condition: there is also a paddle which moves follows mouse
	 */
	private void buildPaddle() {
		paddle = new GRect((WIDTH - PADDLE_WIDTH) / 2, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.black);
		addMouseListeners();
		add(paddle);
		if(x == 0) {
			paddle.setLocation(paddle.getX(), paddle.getY());
		}
	}
	
	//this makes paddle follow mouse
	public void mouseMoved(MouseEvent e) {
		if (e.getX() <= WIDTH - PADDLE_WIDTH)
			paddle.setLocation(e.getX(), PADDLE_Y);
	}
	
	/*
	 * it builds ball which moves, movement starts when we click on the mouse if there are no rects program 
	 * finishes and you win 
	 * pre-condition: there are only paddles and wall
	 * post-condition:there is ball which moves
	 */
	private void buildBall() {
		for(int i = 0; i < NTURNS; i++) {
			ball = new GOval(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS, BALL_RADIUS * 2, BALL_RADIUS * 2);
			ball.setFilled(true);
			ball.setColor(Color.black);
			ball.setLocation(WIDTH/2 + BALL_RADIUS, HEIGHT/2 + BALL_RADIUS);
			pause(100);
			add(ball);
			waitForClick();
			playGame();
			if(x == 0) {
				GLabel label = new GLabel("You won.", WIDTH/2, HEIGHT/2);
				add(label);
				break;
			}
		}
		if(x != 0) {
			GLabel label = new GLabel("You lost.", WIDTH/2, HEIGHT/2);
			add(label);
		}
	}
	
	//it builds txt after program which shows us our score
	private void buildTxt() {
		labelScore = new GLabel("your score is " + score, 0, 0);
		labelScore.setLocation(0, labelScore.getHeight());
		add(labelScore);
	}
	
	//this method adds label which shows us live amounts, also it moves and it has acceleration
	private void playGame() {
		labelLives = new GLabel("Lives: " + lives, 0, 0);
		labelLives.setLocation(0, labelLives.getHeight());
		add(labelLives);
		vx += acc;
		vy += acc;
		while (true) {
			ball.move(vx, vy);
			checkWalls();
			pause(DELAY);
			if(ball.getY() + BALL_RADIUS * 2 > HEIGHT) {
				remove(ball);
				lives--;
				break;
			} else if(x == 0) {
				remove(ball);
				break;
			}
		}
		remove(labelLives);
	}
	
	//this returns element and adds music
	private GObject getCollidingObject() {	
		if(getElementAt(ball.getX(), ball.getY()) != null) {
			bounceClip.play();
			return getElementAt(ball.getX(), ball.getY());
		}
		else if(getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY()) != null){
			bounceClip.play();
			return getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY());
		}
		else if(getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2) != null) {
			bounceClip.play();
			return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2);
		}
		else if(getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2) != null) {
			bounceClip.play();
			return getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2);
		}
		else return null;
	}
	
	//this method checks walls and if ball meets wall or rects or paddle it changes direction 
	private void checkWalls() {
		collider = getCollidingObject();		
		if (ball.getX() < 0) {
			vx = -vx;
		} else if (ball.getY() < 0) {
			vy = -vy;
		} else if (ball.getX() + BALL_RADIUS * 2 > WIDTH) {
			vx = -vx;
		} else if (collider == paddle) {
			if (ball.getX() + BALL_RADIUS < paddle.getX() + PADDLE_WIDTH / 2) {
				vx = -vx;
			} else {
				vx = vx;
			}
			vy = -vy;
		} else if(collider == null) {
			
		}
		else {
			vy = -vy;			
			remove(collider);
			score++;
			x--;
			
		}
	}
}