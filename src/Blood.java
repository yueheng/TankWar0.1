import java.awt.*;

/**
 * Blood in the game, with which the tanks can get 100 life
 * @author yueheng
 *
 */
public class Blood {

	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	
	public void draw(Graphics g) {
		step++;
		if(step == pos.length) step = 0;
		if(!live) return;
		Color c = g.getColor();
		g.setColor(new Color(255, 100, 100));
		g.fillRect(pos[step][0], pos[step][1], w, h);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	private int x, y, w, h;
	private int step;
	private boolean live = true;
	private int[][] pos = { // the positions of blood
			{400, 400}, {410, 410}, {420, 410}, {420, 420},
			{420, 430}, {430, 430}, {430, 440}, {440, 440},
			{440, 450}, {450, 450},	{450, 460}, {460, 460}
	};
}
