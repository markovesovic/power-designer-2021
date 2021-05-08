package modelEditor.figure;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;

import classEditor.Env;
import classEditor.Mode;
import modelEditor.figure.FigureGraphic;
import modelEditor.figure.Point_2D;
import useCaseEditor.CanvasAreaUse;
import useCaseEditor.EnvUse;

public class Entity extends FigureGraphic 
{
	private static long nbOfRectangles = 0;
	
	private Point_2D a, b;	
	private int width;
	private int height;
	private ArrayList<Attributes> list= new ArrayList<>();
	private Mode mode;

	private final UUID uuid;
	
	public Entity(Mode mode) {
		super("Entetitet"+(++nbOfRectangles), Color.black, new Color(200, 200, 250));
		if (mode.equals(Mode.DRAW_ACTOR)){
			width = 60;
			height = 60;
		}
		else {
			width = 80;
			height = 120;
		}
		this.mode = mode;
		setFirstPoint((int)(Math.random()*600), (int)(Math.random()*300)); //TODO get canvas dimensions
		setSecondPoint(a.getX()+width, a.getY()+height);

		this.uuid = UUID.randomUUID();
		
	}

    public void init(Env env, int x, int y) {
    	setColors(env);
    	setFirstPoint(x-width/2, y-height/2);
    	setSecondPoint(x+width/2, y+height/2);
        setSelected(true);
        setBuilding(true);
    }
    
    public ArrayList<Attributes> getList() {
		return list;
	}
    
	public Point_2D getCenter() {
		return new Point_2D ((a.getX()+b.getX())/2, (a.getY()+b.getY())/2);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Point_2D getTopLeft() {
		return new Point_2D(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
	}
 
	public int getXDistance(Entity e) {
		return Math.abs(getTopLeft().getX() - e.getTopLeft().getX());
	}
	
	public int getYDistance(Entity e) {
		return Math.abs(getTopLeft().getY() - e.getTopLeft().getY());
	}
	
	public void move(int dx, int dy) {
		a.move(dx, dy);
		b.move(dx, dy);
	}
	
	public Point_2D getA() {
		return a;
	}
	
	public Point_2D getB() {
		return b;
	}
	
	public void draw(Graphics g, int number) {
		if(mode.equals(Mode.DRAW_RECTANGLE)) {
			Point_2D topleft = getTopLeft();
			g.setColor(getBgForCurrentState());
			g.fillRect(topleft.getX(), topleft.getY(), width, height);
			g.setColor(getStrokeForCurrentState());
			g.drawRect(topleft.getX(), topleft.getY(), width, height);
			
			int y = topleft.getY()+30;
		}
//		else {
//			Image img = null;
//			 try {
//	                img = ImageIO.read(new File("images/man.png"));
//	            } catch (IOException ex) {
//	                ex.printStackTrace();
//	            }
//			if (img != null) {
////                int x = (getWidth() - img.getWidth()) / 2;
////                int y = (getHeight() - img.getHeight()) / 2;
//                g.drawImage(img, 0, 0, );
//            }
//		}
		afterDraw(g);
	}
	
	public Mode getMode() {
		return mode;
	}
	
	@Override
	protected void drawName(Graphics g) {
		Point_2D topLeft = getTopLeft();
		g.drawString(name, topLeft.getX()+2, topLeft.getY()+12);
		
		for(int i = 0; i < list.size(); i++)
		{
			g.drawString(list.get(i).getAccessModifiers(), topLeft.getX()+2, topLeft.getY()+42+i*10);
			g.drawString(list.get(i).getName(), topLeft.getX()+15, topLeft.getY()+42+i*10);
			g.drawString(list.get(i).getType(), topLeft.getX()+75, topLeft.getY()+42+i*10);
		}
	}
	
	public boolean contain(Point_2D p) {
		int width = getWidth();
		int height = getHeight();
		Point_2D topleft = getTopLeft();
		if ( topleft.getX() <= p.getX() && p.getX() <= topleft.getX()+width
		  && topleft.getY() <= p.getY() && p.getY() <= topleft.getY()+height) {
			return true;
		}
		return false;
	}

	public void setFirstPoint(int x, int y) {
	    a = new Point_2D(x, y);
	}
	public void setSecondPoint(int x, int y) {
		b = new Point_2D(x, y);
	}

    public boolean canBeFinished() {
        return true;
    }

	@Override
	public boolean onFigureFinish() {
		return true;
	}

	@Override
	public void onMovePoint(int x, int y) {
	}

	@Override
	public String getShapeName() {
		return "Rectangle";
	}

	@Override
	public void init(EnvUse env, int x, int y) {
		setColors(env);
    	setFirstPoint(x-width/2, y-height/2);
    	setSecondPoint(x+width/2, y+height/2);
        setSelected(true);
        setBuilding(true);
		
	}

	public UUID getUuid() {
		return uuid;
	}
}	