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

/**This class implements the game of Breakeout*/
public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT-50;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 0;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;
	
/** Number of bricks */
	private static final int NBRICKS = NBRICK_ROWS*NBRICKS_PER_ROW;
	
/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_DIAM = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/**state of the game*/
	private static final int NEW_GAME=0;
	private static final int PLAY=1;
	private static final int PAUSE=2;
	private static final int GAME_OVER_WIN=4;
	private static final int GAME_OVER_LOOSE=5;
	private static final int LOOSE_TURN=3;
	
/** Life indication diameter */
	private static final int LIFE_DIAM = 15;
/** Life indication separator */
	private static final int LIFE_SEP = 4;
	public static void main(String[] args)
	{
		new Breakout().start(args);
	}
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		
		/*sets up the environment for a game */
		setup();
		
		/*starts playing a game */
		play();
		
	}
	
	/**sets up the environment for a game */
	private void setup()
	{
		setBricks();
		setPaddle();
		setBall();
		state = NEW_GAME;
		updateStateDisplay();
	}
	/**starts a game */
	private void play()
	{
		//rgen.setSeed(1);
		/**sets up initial sped of ball*/
		vy = 3;
		vx = rgen.nextDouble(1.0, 3.0);
		if(rgen.nextBoolean(0.5)) vx = -vx;
		addMouseListeners();
		
		/**plays the game until win or loose*/
		while (true)
		{	
			if(checkGameOver())
				break;
			while(state!=PLAY)
			{
				pause(20);
				
			}
			/**play*/
			if (state == PLAY)
			{
				speedIncrease();
				
				/**move the ball*/
				ball.move (vx, vy);
				
				double x = ball.getX();
				double y = ball.getY();
			
				/**checks for collision with walls*/
				checkForCollisionsWithWalls(x,y);
				
				/**checks for collision with bricks and paddle*/
				checkForCollisionWithObjects(x,y);
				
				/**pause for 20 milliseconds*/
				pause(20);
			}
			
			
		}
		
			
	}
	public void mouseReleased (MouseEvent e)
	{
		if ((state == LOOSE_TURN) || (state == NEW_GAME) || (state==PAUSE))
			{
				state = PLAY;
				return;
			}
		if (state == PLAY)
			state = PAUSE;
		
	}
	public void mouseMoved (MouseEvent e)
	{
		if (state != PAUSE)
		{
			double newX = e.getX();
			if (((newX + PADDLE_WIDTH / 2) < WIDTH) && ((newX - PADDLE_WIDTH / 2) > 0))
				paddle.setLocation (newX-PADDLE_WIDTH / 2, paddle.getY());
		}
	}
	
	/**Places bricks on the screen*/
	private void setBricks()
	{
		for (int i = 0; i < NBRICK_ROWS; i++)
		{
			setBrickRow(i);
		}
	}
	
	/**Places the paddle in the center on the bottom of the screen*/
	private void setPaddle()
	{ 
		paddle = new GRect ( (WIDTH/2 - PADDLE_WIDTH / 2),
				HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		
	}
	
	/**Places the ball on the screen*/
	private void setBall()
	{
		ball = new GOval (WIDTH / 2 - BALL_DIAM/2, HEIGHT/2 - BALL_DIAM/2,  BALL_DIAM, BALL_DIAM);
		ball.setFilled(true);
		add (ball);
	}
	
	/**sets a row of bricks */
	private void setBrickRow(int n)
	{
		for (int i = 0; i < NBRICKS_PER_ROW; i++)
		{
			setBrick(n, i);
		}
	}
	
	/**Sets a brick*/
	private void setBrick(int row, int column)
	{
		/**creates a new brick*/
		GRect rect = new GRect (column * (BRICK_WIDTH + BRICK_SEP),
				BRICK_Y_OFFSET + row * (BRICK_HEIGHT + BRICK_SEP),
				BRICK_WIDTH,
				BRICK_HEIGHT);
	
		/**sets the color for the brick depending on the row*/
		switch (row)
		{
			case 0:rect.setColor(Color.RED); 
			break;
			case 1:rect.setColor(Color.RED); 
			break;
			
			case 2:rect.setColor(Color.ORANGE); 
			break;
			case 3:rect.setColor(Color.ORANGE); 
			break;
			
			case 4:rect.setColor(Color.YELLOW); 
			break;
			case 5:rect.setColor(Color.YELLOW); 
			break;
			
			case 6:rect.setColor(Color.GREEN); 
			break;
			case 7:rect.setColor(Color.GREEN); 
			break;
			
			case 8:rect.setColor(Color.CYAN); 
			break;
			case 9:rect.setColor(Color.CYAN); 
			break;
		}

		rect.setFilled(true);
		add (rect);
	}
	
	/**checks for collision with walls*/
	private void checkForCollisionsWithWalls(double x, double y)
	{
		if (x <= 0) 
		{
			ball.move(-vx,0);
			vx = -vx;
		}
		if  ((x + BALL_DIAM) >= WIDTH)
		{
			ball.move(-vx,0);
			vx = -vx;
		}	
		if (y <= 0) 
		{
			ball.move(0,-vy);
			vy = -vy;
		}
		
		/**user looses one turn if ball hits bottom, after NTURNS user looses the game */
		if ((y + BALL_DIAM) >= HEIGHT)
		{
			ball.move(0,-vy);
			vy = -vy;
			looseCounter--;
			remove(ball);
			setBall();
			state = LOOSE_TURN;
			updateStateDisplay();
			pause(200);
			
		}
		
		
	}
	
	/**checks for collision, removes colliding object and changes vertical speed direction*/
	private void checkForCollisionWithObjects(double x,double y)
	{
		double  collisionX;
		
		GObject  obj = getElementAt(x, y);
		if (obj == null)
		{
			obj = getElementAt(x + BALL_DIAM, y);		
			if (obj == null)
			{
				obj = getElementAt(x, y +  BALL_DIAM);
				if (obj == null)
				{
					obj = getElementAt(x + BALL_DIAM, y + BALL_DIAM);
					if (obj == null)
						{
							return;
						}
					else
					{
						collisionX = x + BALL_DIAM;
					}
				}
				else
				{
					collisionX = x;
				}
			}
			else
			{
				collisionX = x + BALL_DIAM;
			}
		}
		else
		{
			collisionX = x;
		}
		/**removes colliding object and changes vertical speed direction*/
		if (obj != paddle) 
		{	
			if (obj.getColor() == Color.RED)
				iscore+=100;
			if (obj.getColor() == Color.ORANGE)
				iscore+=50;
			if (obj.getColor() ==  Color.YELLOW)
					iscore+=10;
			if (obj.getColor() == Color.GREEN)
					iscore+=5;
			if (obj.getColor() == Color.CYAN)
					iscore+=1;	
		
			remove(obj);
			brickCounter--;
			updateStateDisplay();
			vy = -vy;
		}
		else
		{
			if ((paddle.getX() + 10 > collisionX) && (vx > 0))
					vx = -vx;
			if ((paddle.getX() + PADDLE_WIDTH - 10 < collisionX) && (vx < 0))
					vx = -vx;
			vy = -Math.abs(vy);
			bounceClip.play();
		}
	}
	
	/**messages the win*/
	private void messageWin()
	{
		GLabel winLabel = new GLabel("You have won!",0,0);
		winLabel.setFont("sans-50");
		winLabel.setLocation(WIDTH/2-winLabel.getWidth()/2, HEIGHT/2-winLabel.getAscent()/2);
		add(winLabel);
		remove(ball);
	}
	
	/**messages the loose*/
	private void messageLoose()
	{
		GLabel looseLabel =new GLabel("Game over!", 0,0);
		looseLabel.setFont("sans-50");
		looseLabel.setLocation(WIDTH/2-looseLabel.getWidth()/2, HEIGHT/2-looseLabel.getAscent()/2);
		add(looseLabel);
		remove(ball);
	}
	
	/**update information about turns and score*/
	private void updateStateDisplay()
	{
		switch (state)
		{
			/**sets 3 red circles as indicators of lives in the bottom left corner of the screen
			 * and the score indicator in the bottom right corner
			 */
			case NEW_GAME:
				life1 = new GOval(LIFE_SEP , HEIGHT + LIFE_SEP, LIFE_DIAM, LIFE_DIAM);
				life2 = new GOval(2 * LIFE_SEP + LIFE_DIAM, HEIGHT + LIFE_SEP, LIFE_DIAM, LIFE_DIAM);
				life3 = new GOval(3 * LIFE_SEP + 2 * LIFE_DIAM, HEIGHT + LIFE_SEP, LIFE_DIAM, LIFE_DIAM);
				score = new GLabel ("0", APPLICATION_WIDTH - 50, HEIGHT + 18);
				score.setFont("sans-18");
				life1.setColor(Color.RED);
				life2.setColor(Color.RED);
				life3.setColor(Color.RED);
				life1.setFilled(true);
				life2.setFilled(true);
				life3.setFilled(true);
				add(life1);
				add(life2);
				add(life3);
				add(score);
				break;
				
			/**removes one life indicator*/	
			case LOOSE_TURN:
				switch (looseCounter)
				{
					case 2:
						remove(life3);
						break;
					case 1:
						remove(life2);
						break;
					case 0:
						remove(life1);
						break;
				}
				break;
			
			/**message about win*/
			case GAME_OVER_WIN:
				messageWin();
				break;
			
			/**message about loose*/
			case GAME_OVER_LOOSE:
				messageLoose();
				break;
			
			/**updates score*/
			case PLAY:
				bounceClip.play();
				remove(score);
				score = new GLabel (""+iscore, APPLICATION_WIDTH - 50, HEIGHT + 18);
				score.setFont("sans-18");
				add(score);
				break;
		}
	}
	
	
	/**increased speed of the ball */
	private void speedIncrease()
	{
		if (brickCounter == NBRICKS - 7)
		{
			vy=(4*vy/Math.abs(vy));
		}
	if (brickCounter == NBRICKS - 20)
		{
			vy=(6*vy/Math.abs(vy));
		}
	if (brickCounter == NBRICKS - 50)
		{
			vy=(8*vy/Math.abs(vy));
		}
	if (brickCounter == NBRICKS - 90)
		{
			vy=(12*vy/Math.abs(vy));
		}
	}
	
	/**check if the game is over*/
	private boolean checkGameOver()
	{
		/**if there is no bricks left display win message*/
		if (brickCounter == 0)
		{
			state = GAME_OVER_WIN;
			updateStateDisplay();
			return true;
		}
		
		/**after NTURNS user looses the game */
		else if (looseCounter == 0)
		{	
			state = GAME_OVER_LOOSE;
			updateStateDisplay();
			return true;
		}
		return false;
	}
	
	/**mouse listeners for paddle moving*/
	
	
	

	/**private instance variables*/
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int brickCounter = NBRICKS;
	private int looseCounter = NTURNS;
	private int state;
	private GOval life1, life2, life3;
	private GLabel score;
	private int iscore;
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
}
