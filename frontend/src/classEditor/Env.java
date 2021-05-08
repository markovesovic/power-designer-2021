package classEditor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import modelEditor.figure.Entity;
import modelEditor.figure.Figure;
import modelEditor.figure.FigureGraphic;
import modelEditor.figure.Point_2D;
import modelEditor.figure.Relationship;


public class Env {

	private UUID uuid;
	private int version = 0;
	
	protected Data data = new Data();
	
	protected Window window;
	
	protected CanvasArea canvas;
	protected CanvasMouseListener canvasMouseListener;
	
	protected ToolBox toolbox;
	protected SelectionPanel selectionPanel;

	protected Color bg = new Color(200, 200, 250);
	protected Color stroke = Color.BLACK;
	
	private static Env instance;

	protected static class Data  {
		List<Entity> entities = new ArrayList<Entity>();
		List<Relationship> relationships = new ArrayList<Relationship>();
	}
	
	private Env(Window window) {
		this.window = window;
	}
	
	public static void CreateInstance(Window window) {
		instance = new Env(window);
	}
	
	public static Env getInstance() {
		return instance;
	}

	public CanvasMouseListener getCanvasMouseListener() {
		return canvasMouseListener;
	}
	public void setCanvasMouseListener(CanvasMouseListener canvasMouseListener) {
		this.canvasMouseListener = canvasMouseListener;
	}
	
	public Color getBackgroundColor() {
		return bg;
	}
	public Color getStrokeColor() {
		return stroke;
	}
	public void setBackgroundColor(Color c) {
		bg = c;
	}
	public void setStrokeColor(Color c) {
		stroke = c;
	}
	
	public void setData(Data d) {
		data = d;
		canvas.repaint();
	}

	public void setCanvas(CanvasArea c) {
		canvas = c;
	}
	public CanvasArea getCanvas() {
		return canvas;
	}
	
	public void setToolbox(ToolBox t) {
		toolbox = t;
	}
	public ToolBox getToolbox() {
		return toolbox;
	}	
	public void setSelectionPanel(SelectionPanel t) {
		selectionPanel = t;
	}
	public SelectionPanel getSelectionPanel() {
		return selectionPanel;
	}

	public List<Entity> getEntities() {
		return data.entities;
	}
	public List<Relationship> getRelationships() {
		return data.relationships;
	}
		

		
	public void addEntity(Entity e) {
		getEntities().add(0, e);
	}
	
	public void addRelationship(Relationship r) {
		getRelationships().add(0, r);
	}
	
	
	public void empty() {
		data = new Data();
	}
	
	public boolean listsAreSame(List<FigureGraphic> f, List<FigureGraphic> g) {
		return f.size() == g.size() && f.containsAll(g);
	}
	
	private List<FigureGraphic> lastSelection = new ArrayList<FigureGraphic>();

	public void onSelectionChanged() {
		List<FigureGraphic> s = new ArrayList<FigureGraphic>( getSelected() );
		if(!listsAreSame(s, lastSelection)) {
			canvas.repaint();
			selectionPanel.onSelectionChanged();
		}
		lastSelection = s;
	}
	
	private void emptySelection() {
		for(Entity ent : getEntities())
			setSelected(ent, false);
		for(Relationship rel : getRelationships())
			setSelected(rel, false);
	}
	
	public void unselectAll() {
		emptySelection();
		onSelectionChanged();
	}
	
	public FigureGraphic getOneByPosition(Point_2D p) {
		for(Entity ent : getEntities())
			if(ent.contain(p))
				return ent;
		for(Relationship rel : getRelationships())
			if(rel.contain(p))
				return rel;
		return null;
	}
	
	public Entity getEntityByPosition(Point_2D p) {
		for(Entity ent : getEntities())
			if(ent.contain(p))
				return ent;
		return null;
	}
	
	public Entity getEntityByName(String name) {
		for(Entity ent : getEntities())
			if(ent.getName().equals(name))
				return ent;
		return null;
	}
	
	public boolean entityNameExists(String name) {
		for(Entity ent : getEntities())
			if(ent.getName().equals(name))
				return true;
		return false;
	}
	
	private void setSelected(FigureGraphic figure, boolean value) {
		figure.setSelected(value);
		figure.setTransparent(value && canvasMouseListener.mouseIsDown);
	}
	
	public List<FigureGraphic> getSelected() {
		List<FigureGraphic> figures = new ArrayList<FigureGraphic>();
		for(Entity ent : getEntities())
			if(ent.isSelected())
				figures.add(ent);
		for(Relationship rel : getRelationships())
			if(rel.isSelected())
				figures.add(rel);
		return figures;
	}
	
	public void selectFigure(FigureGraphic figure) {
		emptySelection();
		setSelected(figure, true);
		onSelectionChanged();
	}
	
	public FigureGraphic selectOneByPosition(Point_2D p) {
		emptySelection();
		FigureGraphic figure = getOneByPosition(p);
		if(figure!=null) {
			setSelected(figure, true);
		}
		onSelectionChanged();
		return figure;
	}
	
	public void selectPoints(Selection selection) {
		for(Entity ent : getEntities())
			setSelected(ent, selection.contain(ent.getCenter()));
		for(Relationship rel : getRelationships())
			setSelected(rel, selection.contain(rel.getCenter()));
		//sortFigures();
		onSelectionChanged();
	}
	
	public void selectAll() {
		for(Entity ent : getEntities())
			setSelected(ent, true);
		for(Relationship rel : getRelationships())
			setSelected(rel, true);
		onSelectionChanged();
	}
	
	public int countSelected() {
		return getSelected().size();
	}
	
	public void moveSelected(int dx, int dy) {
		for(FigureGraphic f : getSelected())
			f.move(dx, dy);
	}
	
	public void remove(Figure figure) {
		if(figure instanceof Entity)
			getEntities().remove((Entity)figure);
		else
			getRelationships().remove((Relationship)figure);
		onSelectionChanged();
	}
	
	public void removeSelected() {
		List<Entity> ents = getEntities();
		List<Relationship> rels = getRelationships();
		List<Relationship> relsDel = new ArrayList<>();
		for(FigureGraphic f : getSelected())
			if(f instanceof Entity){
				for(Relationship r : rels)
				{
					if(r.getEntity1().equals(f)||r.getEntity2().equals(f))
						relsDel.add(r);
				}
				ents.remove((Entity)f);
			}
			else rels.remove((Relationship)f);
			rels.removeAll(relsDel);
		onSelectionChanged();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID id) {
		this.uuid = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
