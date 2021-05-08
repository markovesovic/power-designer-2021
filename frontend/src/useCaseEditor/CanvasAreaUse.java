package useCaseEditor;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import classEditor.Mode;
import modelEditor.figure.Entity;
import modelEditor.figure.Point_2D;
import modelEditor.figure.Relationship;

@SuppressWarnings("serial")
public class CanvasAreaUse extends Canvas {
	protected EnvUse env;
	private ModeUse mode = ModeUse.MOVE;
	protected SelectionUse selection = null;
	
	public CanvasAreaUse(EnvUse env) {
		this.env = env;
		setBackground(Color.white);
	}
	
	public void setMode(ModeUse m) {
		this.mode = m;
	}
	public ModeUse getMode() {
		return this.mode;
	}

	public void setSelection(SelectionUse s) {
		selection = s;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		List<Entity> ents = new ArrayList<Entity>(env.getEntities());
		Collections.reverse(ents);
		for(Entity ent : ents) {
			if(ent.getMode().equals(Mode.DRAW_RECTANGLE))
				ent.draw(g,10);
			else {
				Image img = null;
				 try {
		                img = ImageIO.read(new File("images/man2.png"));
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
				if (img != null) {
//	                int x = (getWidth() - img.getWidth()) / 2;
//	                int y = (getHeight() - img.getHeight()) / 2;
					
	                g.drawImage(img, Math.min(ent.getA().getX(), ent.getB().getX()), Math.min(ent.getA().getY(), ent.getB().getY()), this);
				}
			}
		}
		for(Relationship rel : env.getRelationships())
			rel.draw(g,0);
		if(selection!=null)
			selection.draw(g);
	}
	
	@Override
	public void update(Graphics g) {
		Graphics offgc;
		Image offscreen = null;
		Dimension d = getSize();
		offscreen = createImage(d.width, d.height);
		offgc = offscreen.getGraphics();
		
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, d.width, d.height);
		offgc.setColor(getForeground());
		
		paint(offgc);
		
		g.drawImage(offscreen, 0, 0, this);
	}
}
