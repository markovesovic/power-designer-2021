package useCaseEditor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import modelEditor.figure.Entity;
import modelEditor.figure.Figure;
import modelEditor.figure.FigureGraphic;
import modelEditor.figure.Point_2D;
import modelEditor.figure.Relationship;


public class EnvUse {
	
	protected Data data = new Data();
	
	protected WindowUse window;
	
	protected CanvasAreaUse canvas;
	protected CanvasMouseListenerUse canvasMouseListener;
	
	protected ToolBoxUse toolbox;
	protected SelectionPanelUse selectionPanel;

	protected Color bg = new Color(200, 200, 250);
	protected Color stroke = Color.BLACK;
	
	private static EnvUse instance;

	protected static class Data  {
		List<Entity> entities = new ArrayList<Entity>();
		List<Relationship> relationships = new ArrayList<Relationship>();
	}
	
	private EnvUse(WindowUse window) {
		this.window = window;
	}
	
	public static void CreateInstance(WindowUse window) {
		instance = new EnvUse(window);
	}
	
	public static EnvUse getInstance() {
		return instance;
	}

	public CanvasMouseListenerUse getCanvasMouseListener() {
		return canvasMouseListener;
	}
	public void setCanvasMouseListener(CanvasMouseListenerUse canvasMouseListener) {
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

	public void setCanvas(CanvasAreaUse c) {
		canvas = c;
	}
	public CanvasAreaUse getCanvas() {
		return canvas;
	}
	
	public void setToolbox(ToolBoxUse t) {
		toolbox = t;
	}
	public ToolBoxUse getToolbox() {
		return toolbox;
	}	
	public void setSelectionPanel(SelectionPanelUse t) {
		selectionPanel = t;
	}
	public SelectionPanelUse getSelectionPanel() {
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
	
	public void selectPoints(SelectionUse selection) {
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
	
}
