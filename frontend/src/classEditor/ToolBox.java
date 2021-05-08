package classEditor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

//import org.json.JSONObject;


import app.AppCore;
import classEditor.Mode;
import modelEditor.figure.Entity;
import modelEditor.figure.Relationship;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ToolBox extends JPanel {
	
	public JButton select = new JButton(new ImageIcon("images/select.png"));
	public JButton move = new JButton(new ImageIcon("images/move.png"));
	public JButton newRectangle = new JButton(new ImageIcon("images/rectangle.png"));
	public JButton deleteFigure = new JButton(new ImageIcon("images/deleteFigure.png"));
	public JButton newLine = new JButton(new ImageIcon("images/line.png"));
	private JButton save = new JButton(new ImageIcon("images/save.png"));
	private JButton undo = new JButton(new ImageIcon("images/undo.png"));
	private JButton redo = new JButton(new ImageIcon("images/redo.png"));
	
	protected List<JButton> buttons = new ArrayList<JButton>();

	protected Canvas bgColor = new Canvas();
	protected Canvas strokeColor = new Canvas();
	
	protected Env env;
	protected Window window;
	protected CanvasArea canvas;
	
	private JButton addImageButton(JButton b) {
		Dimension d = new Dimension(36, 36);
		b.setPreferredSize(d);
		b.setMinimumSize(d);
		b.setMaximumSize(d);
		buttons.add(b);
		return b;
	}
	private void select(JButton button) {
		for(JButton b : buttons) {
			b.setSelected(false);
			b.setBackground(null);
		}
		button.setSelected(true);
		button.setBackground(Color.white);
	}
	
	public ToolBox(Window window, Env env, CanvasArea canvas) {
		this.env = env;
		this.window = window;
		this.canvas = canvas;
		setBorder(BorderFactory.createEtchedBorder());
		GridBagLayout l = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(l);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		
		JPanel panel = new JPanel();
		
		panel.add(addImageButton(select));
		panel.add(addImageButton(move));
		panel.add(addImageButton(deleteFigure));
		panel.add(addImageButton(undo));
		panel.add(addImageButton(redo));
		
		add(panel, c);
		c.gridy++;
		panel = new JPanel();
		panel.add(addImageButton(newRectangle));
		panel.add(addImageButton(newLine));
		panel.add(addImageButton(save));
		add(panel, c);
		
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		
		panel = new JPanel();
		add(panel, c);		
		
		select.addActionListener(new SelectListener());
		move.addActionListener(new MoveListener());
		deleteFigure.addActionListener(new DeleteListener());
		newRectangle.addActionListener(new NewRectangleListener());
		newLine.addActionListener(new NewLineListener());
		save.addActionListener(e -> {
			try {
				JSONArray classArray = new JSONArray();

				env.getEntities().forEach(entity -> {
					JSONObject classModel = new JSONObject();

					classModel.put("id", entity.getUuid());
					classModel.put("type", "class");
					classModel.put("name", entity.getName());
					classModel.put("is_abstract", false);

					JSONArray attributesArray = new JSONArray();

					entity.getList().forEach(attribute ->  {
						JSONObject attributeJson = new JSONObject();
						attributeJson.put("type", attribute.getType());
						attributeJson.put("name", attribute.getName());
						attributeJson.put("is_function", false);
						attributeJson.put("is_private", attribute.getAccessModifiers());

						attributesArray.put(attributeJson);
					});

					classModel.put("attributes", attributesArray);

					classArray.put(classModel);
				});



				JSONObject requestBody = new JSONObject()
						.put("model_type", "class_model")
						.put("class_model", classArray);
				System.out.println(requestBody.toString());

				String url = AppCore.BACKEND_URL + "projects/_project_id/models";


				HttpClient client = HttpClient.newHttpClient();

 				HttpRequest request = HttpRequest.newBuilder(
						URI.create(url))
						.header("accept", "application/json")
						.build();

				HttpResponse<Object> response = client.send(request, responseInfo -> null);

				System.out.println(response.body().toString());

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		select(move);
	}

	class SelectListener extends ButtonListener {
		public SelectListener() {
			super(Mode.SELECT);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	
	class DeleteListener extends ButtonListener {
		
		public DeleteListener() {
			super(Mode.DELETE);
		}

		public void actionPerformed(ActionEvent e) {
			env.removeSelected();
		}
	}
	
	class MoveListener extends ButtonListener {
		public MoveListener() {
			super(Mode.MOVE);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	class NewRectangleListener extends ButtonListener {
		public NewRectangleListener() {
			super(Mode.DRAW_RECTANGLE);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	
	class NewLineListener extends ButtonListener{
		
		public NewLineListener() {
			super(Mode.DRAW_LINE);
		}		
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
		
	}
	
	class ButtonListener implements ActionListener {
		Mode mode;
		public ButtonListener(Mode mode) {
			this.mode = mode;
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JButton) 
				select((JButton)e.getSource());
			env.getCanvas().setMode(mode);
			env.getCanvasMouseListener().onToolChanged(mode);
		}
	}
}
