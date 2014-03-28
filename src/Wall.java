import java.awt.*;

/**
 * The walls in the games
 * @author yueheng
 *
 */
class Wall {
	
	public Wall(int x, int y, int witdth, int height, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.width = witdth;
		this.height = height;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		g.fillRect(x, y, width, height);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	private int x, y, width, height;
	private TankClient tc;

}
