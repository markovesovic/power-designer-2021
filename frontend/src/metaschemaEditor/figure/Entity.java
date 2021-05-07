package metaschemaEditor.figure;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import classEditor.Env;
import metaschemaEditor.figure.FigureGraphic;
import metaschemaEditor.figure.Point_2D;

public class Entity extends FigureGraphic 
{
	private static long nbOfRectangles = 0;
	
	private Point_2D a, b;	
	private static int width = 80;
	private static int height = 50;
//	private ArrayList<JSONObject> attrs;
	
	public Entity() {
		super("Entetitet"+(++nbOfRectangles), Color.black, new Color(200, 200, 250));
//		attrs = new ArrayList<JSONObject>();
		setFirstPoint((int)(Math.random()*600), (int)(Math.random()*300)); //TODO get canvas dimensions
		setSecondPoint(a.getX()+width, a.getY()+height);
	}

    public void init(Env env, int x, int y) {
    	setColors(env);
    	setFirstPoint(x-width/2, y-height/2);
    	setSecondPoint(x+width/2, y+height/2);
        setSelected(true);
        setBuilding(true);
    }
	
//    public void addAttribute(JSONObject attr) {
//    	attrs.add(attr);
//    }
//
//    public ArrayList<JSONObject> getAttributes(){
//    	return attrs;
//    }
    
	public Point_2D getCenter() {
		return new Point_2D ((a.getX()+b.getX())/2, (a.getY()+b.getY())/2);
	}

	public int getWidth() {
		return width;//Math.abs(a.getX() - b.getX());
	}

	public int getHeight() {
		return height;//Math.abs(a.getY() - b.getY());
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

	public void draw(Graphics g, int number) {		
		Point_2D topleft = getTopLeft();
		g.setColor(getBgForCurrentState());
		g.fillRect(topleft.getX(), topleft.getY(), width, height+number*10);
		g.setColor(getStrokeForCurrentState());
		g.drawRect(topleft.getX(), topleft.getY(), width, height+number*10);
		
		int y = topleft.getY()+30;
//		for (JSONObject attr : attrs) {
//			try {
//				g.drawString(attr.getString("name"), topleft.getX()+2, y);
//				y+=15;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
		
		afterDraw(g);
	}
	
//	public JSONObject toJSONObject() {
//		try {
//			JSONObject o = new JSONObject().put("name", getName());
//			JSONArray arr = new JSONArray();
//			for (JSONObject attr : attrs)
//				arr.put(attr);
//			o.put("attributes", arr);
//			return o;
//		} catch (Exception e) {
//			System.err.println("Greska pri konvertovanju entiteta");
//		}
//		return null;
//	}
	
	@Override
	protected void drawName(Graphics g) {
		Point_2D topLeft = getTopLeft();
		g.drawString(name, topLeft.getX()+2, topLeft.getY()+12);
		
		for(int i = 0; i < 0; i++)
		{
			g.drawString("d", topLeft.getX()+2, topLeft.getY()+42+i*10);
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
}	