import java.awt.*;

/**
 * Get explodes
 * @author yueheng
 *
 */
public class Explode {

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	/**
	 * draw the explodes
	 * @param g
	 */
	public void draw(Graphics g) {
		if(!live) {
			tc.explodes.remove(this); //if not alive, remove this
			return;
		}
		
		if(step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(new Color(150, 150, 20));
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}
	
	private int x;
	private int y;
	private int step = 0;
	private boolean live = true;
	int[] diameter = {5, 10, 15, 20, 25, 30, 35, 40, 45, 28, 15, 5}; //diameters of explodes
	private TankClient tc;

}
