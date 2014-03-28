import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * 
 * @author yueheng
 * Tank
 */
class Tank {

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.dir = dir;
	}
	
	/**
	 * draw the tank
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		if(live == false) {  
			if(!good) {
				tc.tanks.remove(this);  //if bad tanks die, remove them
			}
			return;
		}
		Color c = g.getColor();
		if(good == true) g.setColor(new Color(200, 50, 50));
		else g.setColor(new Color(50, 50, 200));
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		if(good) bb.draw(g);  //if tank is good, draw blood bar
		
		switch(ptDir) {  //draw the line in tanks, which represents their directions
		case U: g.drawLine(x+WIDTH/2, y, x+WIDTH/2, y+HEIGHT/2); break;
		case RU:g.drawLine(x+WIDTH, y, x+WIDTH/2, y+HEIGHT/2); break;
		case R:g.drawLine(x+WIDTH, y+HEIGHT/2, x+WIDTH/2, y+HEIGHT/2); break;
		case RD:g.drawLine(x+WIDTH, y+HEIGHT, x+WIDTH/2, y+HEIGHT/2); break;
		case D: g.drawLine(x+WIDTH/2, y+HEIGHT, x+WIDTH/2, y+HEIGHT/2); break;
		case LD: g.drawLine(x, y+HEIGHT, x+WIDTH/2, y+HEIGHT/2); break;
		case L: g.drawLine(x, y+HEIGHT/2, x+WIDTH/2, y+HEIGHT/2); break;
		case LU: g.drawLine(x, y, x+WIDTH/2, y+HEIGHT/2); break;
		}
		
		move();
	}
	
	/**
	 * move
	 */
	private void move() {
		oldX = x;  //record last location
		oldY = y;
		
		switch(dir) {
		case U: y -= YSPEED; break;
		case RU: x += XSPEED; y -= YSPEED; break;
		case R: x += XSPEED; break;
		case RD: x += XSPEED; y += YSPEED; break;
		case D:  y += YSPEED; break;
		case LD: x -= XSPEED; y += YSPEED; break;
		case L: x -= XSPEED;  break;
		case LU: x -= XSPEED; y -= YSPEED; break;
		case STOP: break;
		}
		
		if(this.dir != Direction.STOP) { //record the ptDir
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;  //tanks can not go out of the screen 
		if(y < 30) y = 30;
		if(x > TankClient.GAME_WIDTH - this.WIDTH) x = TankClient.GAME_WIDTH - this.WIDTH;
		if(y > TankClient.GAME_HEIGHT - this.HEIGHT) y = TankClient.GAME_HEIGHT - this.HEIGHT;
		
		if(!good) {		//change the direciton of bad tanks	
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(5) + 3;
				int randomNumber = r.nextInt(dirs.length);
				dir = dirs[randomNumber];
			}
			
			if(r.nextInt(40) > 38) this.fire();
			step--;
		}
	}
	
	/**
	 * change directions
	 */
	private void setDirection() {
		if(left==true && !up==true && !right==true && !down==true) dir = Direction.L;
		else if(left==true && up==true && !right==true && !down==true) dir = Direction.LU;
		else if(!left==true && up==true && !right==true && !down==true) dir = Direction.U;
		else if(!left==true && up==true && right==true && !down==true) dir = Direction.RU;
		else if(!left==true && !up==true && right==true && !down==true) dir = Direction.R;
		else if(!left==true && !up==true && right==true && down==true) dir = Direction.RD;
		else if(!left==true && !up==true && !right==true && down==true) dir = Direction.D;
		else if(left==true && !up==true && !right==true && down==true) dir = Direction.LD;
		else if(!left==true && !up==true && !right==true && !down==true) dir = Direction.STOP;
		
		if(dir != Direction.STOP) ptDir = dir; 
	}
	
	/**
	 * Fire
	 * @return new missiles
	 */
	private Missile fire() {
		if(!live) return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2; //location of missiles
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, ptDir, tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * Fire using its direction
	 * @param dir
	 * @return
	 */
	private Missile fire(Direction dir) {
		if(!live) return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir, tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * Fire to all eight directions
	 */
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i = 0; i < dirs.length -1; i++) {
			fire(dirs[i]);
		}
	}
	
	/**
	 * 
	 * @return The left life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * Set life
	 * @param life
	 */
	public void setLife(int life) {
		this.life = life;
	}

	/**
	 * Set whether the tank is live
	 * @param live
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	
	/**
	 * Check whether the tank is live
	 * @return
	 */
	public boolean isLive() {
		return live;
	}
	
	/**
	 * Check whether it's a good tank
	 * @return
	 */
	public boolean isGood() {
		return good;
	}
	
	/**
	 * Go to its last location
	 */
	public void stay() {
		x = oldX;
		y = oldY;
	}
	
	/**
	 * 
	 * @return The Rectangle around the tank
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * Hit Wall
	 * @param w Wall
	 * @return
	 */
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) { 
			this.stay(); //if tanks hit the wall, go to their last location
			return true;
		}
		return false;
	}
	
	/**
	 * Collides with tanks
	 * @param tanks
	 * @return
	 */
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay(); //if tank hit tanks, go to their last location
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Add more life
	 * @param b
	 * @return
	 */
	public boolean eatBlood(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	/**
	 * KeyEvent: Key pressed
	 * @param k
	 */
	public void keyPressed(KeyEvent k) {
		int key = k.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2: //if tank is not live, make it live with 100 life
			if(!live) {
				live = true;
				life = 100;
			}
			break;
		case KeyEvent.VK_LEFT: left = true; break;
		case KeyEvent.VK_UP: up = true; break;
		case KeyEvent.VK_RIGHT: right = true; break;
		case KeyEvent.VK_DOWN: down = true; break;
		}
		setDirection();
	}
	
	/**
	 * KeyEvent: Key relieased
	 * @param k
	 */
	public void keyReleased(KeyEvent k) {
		int key = k.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL: this.fire(); break; //fire
		case KeyEvent.VK_LEFT: left = false; break;
		case KeyEvent.VK_UP: up = false; break;
		case KeyEvent.VK_RIGHT: right = false; break;
		case KeyEvent.VK_DOWN: down = false; break;
		case KeyEvent.VK_A: superFire(); break; //fire in all eight directions
		}
		setDirection();
	}
	
	private int x;
	private int y;
	private int oldX;
	private int oldY;
	private boolean good;
	private boolean live = true;
	private int life = 100;
	private final int XSPEED = 5;
	private final int YSPEED = 5;
	private final int WIDTH = 30;
	private final int HEIGHT = 30;
	private boolean up = false, right = false, down = false, left = false;
	enum Direction {U, RU, R, RD, D, LD, L, LU, STOP};
	private static Random r = new Random();
	private int step = r.nextInt(5) + 3;
	private Direction dir;
	private Direction ptDir = Direction.R;
	TankClient tc ;
	BloodBar bb = new BloodBar();
	
	/**
	 * Blood Bar
	 * @author yueheng
	 *
	 */
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y - 10, WIDTH, 10); //the outer rect
			int w = WIDTH * getLife() /100;
			g.fillRect(x, y - 10, w, 10); //the rect represents the tank's life
			g.setColor(c);
			
		}
	}
}
