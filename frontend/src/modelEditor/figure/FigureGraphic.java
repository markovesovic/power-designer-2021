package modelEditor.figure;

import java.awt.*;
import java.util.ArrayList;

import classEditor.Env;
import useCaseEditor.EnvUse;

public abstract class FigureGraphic implements Figure
{
    protected Color colorStroke, colorBackground;
	protected String name;
	protected boolean isAbstract;
	protected String type;
	
	
	protected static final int THRESHOLD_BUILDING_PX = 8;
	
	protected boolean selected = false;
	
	protected boolean transparent = false;
	
	protected boolean building = false; 
	
	public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public FigureGraphic (String name, Color colorStroke, Color colorBackground) {
		this.colorStroke = colorStroke;	
		this.colorBackground = colorBackground;	
		this.name = name;
	}

    public FigureGraphic (String name) {
    	this(name, Color.black, Color.white);
	}
    
    public void setColors(Env env) {
    	setColors(env.getStrokeColor(), env.getBackgroundColor());
    }
    
    public void setColors(EnvUse env) {
    	setColors(env.getStrokeColor(), env.getBackgroundColor());
    }
    
	public void setColors(Color stroke, Color bg) {
		colorStroke = stroke;
    	colorBackground = bg;
	}
	
	

	public Color getStrokeColor()
	{
		return colorStroke;	
	}

	public Color getBackgroundColor()
	{
		return colorBackground;	
	}

	public void setStrokeColor(Color c) {
		colorStroke = c;
	}

	public void setBackgroundColor(Color c) {
		colorBackground = c;
	}
	
	public static double distance(Figure f1, Figure f2)
	{
		return Point_2D.distance(f1.getCenter(), f2.getCenter());
	}

	protected void drawCenter(Graphics g) {
		Point_2D c = getCenter();
		g.drawLine(c.getX()-1, c.getY(), c.getX()+1, c.getY());
		g.drawLine(c.getX(), c.getY()-1, c.getX(), c.getY()+1);
	}
	
	protected void drawName(Graphics g) {
		Point_2D c = getCenter();
		g.drawString(name, c.getX()+2, c.getY()+12);
	}
	
	public abstract void draw(Graphics g, int number);


	public void afterDraw(Graphics g) {
		drawName(g);
		if(isSelected()) {
			drawCenter(g);
		}
	}

	public String getName() {
		return name;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public Color getStrokeForCurrentState() {
		Color c = colorStroke;
		return transparent || building ? new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()/2) : c;
	}

	public Color getBgForCurrentState() {
		Color c = colorBackground;
		return transparent || building ? new Color(c.getRed(), c.getGreen(), c.getBlue(),  (c.getAlpha()*2)/3) : c;
	}

	public void setSelected(boolean s) {
		selected = s;
	}
	public void setTransparent(boolean o) {
		transparent = o;
	}
	public boolean isSelected() {
		return selected;
	}
	
	public abstract void init(Env env, int x, int y);
	
	public abstract void init(EnvUse env, int x, int y);
	
	public abstract boolean canBeFinished();
	
	public abstract boolean onFigureFinish();
	
	public abstract void onMovePoint(int x, int y);
	
	public abstract String getShapeName();
}