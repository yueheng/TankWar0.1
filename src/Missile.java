import java.awt.*;
import java.util.List;


/**
 * Missile, which can hit tanks, and die when hit the walls and the frame
 * when a tank is hit by its enemy missiles, it will die or lose life
 * @author yueheng
 *
 */
class Missile {
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	/**
	 * draw the missiles
	 * @param g
	 */
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}
	
	/**
	 * move
	 */
	public void move() {
		switch(dir) {
		case U: y -= YSPEED; break;
		case RU: x += XSPEED; y -= YSPEED; break;
		case R: x += XSPEED; break;
		case RD: x += XSPEED; y += YSPEED; break;
		case D:  y += YSPEED; break;
		case LD: x -= XSPEED; y += YSPEED; break;
		case L: x -= XSPEED;  break;
		case LU: x -= XSPEED; y -= YSPEED; break;
		}
		if(x<0 || y<0 || x>TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT) { //die when go out of the screen
			live = false;
		}
	}
	
	/**
	 * Get the rectangle around the missile
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * Hit tanks
	 * @param k
	 * @return
	 */
	public boolean hitTank(Tank k) {
		
		if(this.live && k.isLive() && this.getRect().intersects(k.getRect()) && this.good != k.isGood()) {
			
			if(k.isGood()) {
				k.setLife(k.getLife() - 20);
				if(k.getLife() <= 0) k.setLive(false);
			}
			else k.setLive(false);
			
			live = false;
			Explode e = new Explode(x, y, tc); //if hitted, explode
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	/**
	 * Hit a list of tanks
	 * @param tanks
	 * @return
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			if(this.hitTank(tanks.get(i)) == true) return true;
		}
		return false;
	}
	
	/**
	 * Hit the walls
	 * @param w
	 * @return
	 */
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
	private int x;
	private int y;
	private boolean live = true;
	private boolean good;
	private TankClient tc;
	private final int XSPEED = 10;
	private final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	Tank.Direction dir;

}
