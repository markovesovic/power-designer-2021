package classEditor;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelEditor.figure.Entity;
import modelEditor.figure.Relationship;

@SuppressWarnings("serial")
public class CanvasArea extends Canvas {
	protected Env env;
	private Mode mode = Mode.MOVE;
	protected Selection selection = null;
	
	public CanvasArea(Env env) {
		this.env = env;
		setBackground(Color.white);
	}
	
	public void setMode(Mode m) {
		this.mode = m;
	}
	public Mode getMode() {
		return this.mode;
	}

	public void setSelection(Selection s) {
		selection = s;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		List<Entity> ents = new ArrayList<Entity>(env.getEntities());
		Collections.reverse(ents);
		for(Entity ent : ents)
			ent.draw(g,10);
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
