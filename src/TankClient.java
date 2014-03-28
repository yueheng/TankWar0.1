/*
 * use double buffer to eliminate flicker
 * 
 */


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Main class which contains that frame
 * @author yueheng
 */
public class TankClient extends Frame{
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Image offScreenImage = null;  
	
	Blood b = new Blood();
	Wall w1 = new Wall(100, 200, 20, 100, this);
	Wall w2 = new Wall(300, 100, 150, 20, this);
	Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP, this);
	List<Tank> tanks = new ArrayList<Tank>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}

	/**
	 * Launch the frame
	 */
	public void launchFrame() {
		setTitle("Tank War");
		setLocation(200, 50);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setBackground(new Color(100, 200, 100));
		setVisible(true);
		setResizable(false);
		
		for(int i = 0; i < 10; i++) { //add some enemy tanks
			tanks.add(new Tank(50+40*(i+1), 50, false, Tank.Direction.D, this));
		}
		
		addWindowListener(new TCWindowListener());  //add window listener
		addKeyListener(new KeyMonitor());	//add key listener	
		new Thread(new TKThread()).start();  //add new thread
	}
	
	/**
	 * To paint
	 * @param g Graphics
	 */
	public void paint (Graphics g) {
		
		if(tanks.size() <= 0) {  //if all enemies die, add more 5 more enemies
			for(int i = 0; i < 5; i++) {
				tanks.add(new Tank(50+40*(i+1), 50, false, Tank.Direction.D, this));
			}
		}
		g.drawString("Missiles:  " + missiles.size(), 10, 50); //draw some description strings 
		g.drawString("Explodes: " + explodes.size(), 10, 70);
		g.drawString("Tanks   : " + tanks.size(), 10, 90);
		g.drawString("Mytank life: " + myTank.getLife(), 10, 110);
		
		for(int i = 0; i < missiles.size(); i++) {  //draw missles
			Missile m = missiles.get(i);
			m.hitWall(w1); //hit wall
			m.hitWall(w2);
			m.hitTanks(tanks); //hit tanks
			m.hitTank(myTank);
			m.draw(g);
		}
		
		for(int i = 0; i< explodes.size(); i++) {  //draw explode
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i < tanks.size(); i++) {  //draw tanks
			Tank t = tanks.get(i);
			t.hitWall(w1);  //tanks hit wall
			t.hitWall(w2);
			t.collidesWithTanks(tanks); //tank collides with tanks
			t.draw(g);
		}
		
		myTank.collidesWithTanks(tanks);  //my tank collides with tanks
		myTank.draw(g);
		myTank.eatBlood(b); //my tanks eat blood
		
		w1.draw(g); //draw the wall
		w2.draw(g);	
		b.draw(g); //draw blood
		
	}
	

	/**
	 * Draw the off screen image
	 */
	public void update(Graphics g) { //draw the off screen image
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreenImage = offScreenImage.getGraphics();
		Color c = gOffScreenImage.getColor();
		gOffScreenImage.setColor(new Color(100, 200, 100));
		gOffScreenImage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreenImage.setColor(c);
		paint(gOffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	/**
	 *Tank War Window listener
	 */
	private class TCWindowListener extends WindowAdapter {  
		public void windowClosing (WindowEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * Key Monitor
	 */
	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent k) {
			myTank.keyReleased(k);
		}
		public void keyPressed(KeyEvent k) {
			myTank.keyPressed(k);
		}
		
	}
	
	/**
	 *Tank War Thread
	 */
	private class TKThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);  //调用外部包装类的repaint
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
