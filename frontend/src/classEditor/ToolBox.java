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
import java.util.*;

import javax.swing.*;

//import org.json.JSONObject;


import app.AppCore;
import classEditor.Mode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import modelEditor.figure.Attributes;
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
	private final JButton save = new JButton(new ImageIcon("images/save.png"));
	private final JButton undo = new JButton(new ImageIcon("images/undo.png"));
	private final JButton redo = new JButton(new ImageIcon("images/redo.png"));
	private final JButton merge = new JButton(new ImageIcon("images/merge.png"));
	private final JButton transformation = new JButton(new ImageIcon("images/transformation.png"));
	
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
		panel.add(addImageButton(merge));
		panel.add(addImageButton(transformation));
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

		transformation.addActionListener(e -> {
			try {

				JSONArray classArray = new JSONArray();

				env.getEntities().forEach(entity -> {
					JSONObject classModel = new JSONObject()
							.put("id", entity.getUuid())
							.put("type", entity.getType())
							.put("name", entity.getName())
							.put("is_abstract", entity.isAbstract())
							.put("x_coordinate",
									(entity.getA().getX() +
											entity.getB().getX()) / 2)
							.put("y_coordinate",
									(entity.getA().getY() +
											entity.getB().getY()) / 2);

					JSONArray attributesArray = new JSONArray();

					entity.getList().forEach(attribute ->  {
						JSONObject attributeJson = new JSONObject()
								.put("type", attribute.getType())
								.put("name", attribute.getName())
								.put("is_function", attribute.isFunction())
								.put("modifier", attribute.getAccessModifiers());


						attributesArray.put(attributeJson);
					});

					JSONArray from = new JSONArray();
					JSONArray to = new JSONArray();
					env.getRelationships().forEach(relationship -> {

						if (relationship.getEntity1().getUuid() == entity.getUuid()) {
							from.put(
									new JSONObject()
											.put("id", relationship.getEntity2().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
						if (relationship.getEntity2().getUuid() == entity.getUuid()) {
							to.put(
									new JSONObject()
											.put("id", relationship.getEntity1().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
					});

					classModel.put("attributes", attributesArray)
							.put("from", from)
							.put("to", to);

					classArray.put(classModel);
				});

				JSONObject requestBody = new JSONObject()
						.put("model_type", "class_model")
						.put("class_model", classArray);

				if(env.getUuid() != null) {
					requestBody.put("id", env.getUuid());
					requestBody.put("version", env.getVersion());
				}


				System.out.println(requestBody.toString(4));

				String url = AppCore.BACKEND_URL + "projects/" + AppCore.PROJECT_URL + "/models/generate/code";

				HttpClient client = HttpClient.newHttpClient();

				HttpRequest request = HttpRequest.newBuilder(URI.create(url))
						.header("Content-type", "application/json")
						.header("user_id", AppCore.USER_ID)
						.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
						.build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				JSONObject returnJson = new JSONObject(response.body());
				System.out.println(returnJson.toString(4));

				String message;
				if(returnJson.get("status").equals("ok") ) {
					message = "Transformation was successful";
				} else {
					message = "Operation failed with status: " + ((JSONObject)returnJson.get("error")).get("code")
							+ "\nMessage: " + ((JSONObject)returnJson.get("error")).get("message");
				}
				JOptionPane.showMessageDialog(null, message);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		merge.addActionListener(e -> {
			try {

				String url = AppCore.BACKEND_URL + "projects/" + AppCore.PROJECT_URL + "/models/" + AppCore.CLASS_MODEL_ID;

				HttpClient client = HttpClient.newHttpClient();

				HttpRequest request = HttpRequest.newBuilder(URI.create(url))
						.header("Content-type", "application/json")
						.header("user_id", AppCore.USER_ID)
						.GET()
						.build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				JSONObject responseJson = new JSONObject(response.body());

				AppCore.PULL_REQUIRED = false;

				System.out.println(responseJson.toString(4));
				if(responseJson.get("status").equals("fail")) {
					return;
				}
				System.out.println("version : " + responseJson.get("version"));


				if(Integer.parseInt(String.valueOf(responseJson.get("version"))) == env.getVersion()) {
					JOptionPane.showMessageDialog(null, "Local model is up to date");
					return;
				}
				env.setVersion(Integer.parseInt(String.valueOf(responseJson.get("version"))));
				env.setMaxVersion(Integer.parseInt(String.valueOf(responseJson.get("version"))));


				// Pulled class model
				JSONArray classArray = responseJson.getJSONArray("class_model");

				Env envPulled = Env.getInstancePulled();

				envPulled.getEntities().clear();
				envPulled.getRelationships().clear();

				// Creating env for pulled json
				{
					Map<String, Entity> entitiesMap = new HashMap<>();

					classArray.forEach(classEntity -> {
						Entity entity = new Entity(Mode.DRAW_RECTANGLE);
						JSONObject jsonEntity = (JSONObject) classEntity;

						entitiesMap.put(jsonEntity.getString("id"), entity);
					});

					classArray.forEach(classEntity -> {
						JSONObject jsonEntity = (JSONObject) classEntity;

						String entityID = jsonEntity.getString("id");
						Entity entity = entitiesMap.get(entityID);

						int x_coordinate = jsonEntity.getInt("x_coordinate");
						int y_coordinate = jsonEntity.getInt("y_coordinate");
						String entityType = jsonEntity.getString("type");
						boolean isAbstract = jsonEntity.getBoolean("is_abstract");

						String name = jsonEntity.getString("name");

						JSONArray attributes = jsonEntity.getJSONArray("attributes");

						attributes.forEach(jsonAttributeRaw -> {
							JSONObject jsonAttribute = (JSONObject) jsonAttributeRaw;
							String attrName = jsonAttribute.getString("name");
							String modifier = jsonAttribute.getString("modifier");
							String type = jsonAttribute.getString("type");
							boolean function = jsonAttribute.getBoolean("is_function");

							Attributes attribute = new Attributes(modifier, attrName, type, function);
							entity.getList().add(attribute);
						});

						entity.init(envPulled, x_coordinate, y_coordinate);
						entity.setName(name);
						entity.setType(entityType);
						entity.setUUID(entityID);
						entity.setAbstract(isAbstract);
						envPulled.addEntity(entity);
						envPulled.onSelectionChanged();

						JSONArray from = (JSONArray) jsonEntity.get("from");
						from.forEach(relationObjectRaw -> {
							String id = ((JSONObject) relationObjectRaw).getString("id");
							String connectionType = ((JSONObject) relationObjectRaw).getString("connection_type");
							Relationship r = new Relationship();
							r.setEntity1(entity);
							r.setEntity2(entitiesMap.get(id));
							r.setName(connectionType);
							envPulled.addRelationship(r);
							envPulled.onSelectionChanged();
						});

					});
				}


				// Local class model
				JSONArray classArrayEnv = new JSONArray();

				env.getEntities().forEach(entity -> {
					JSONObject classModel = new JSONObject()
							.put("id", entity.getUuid())
							.put("type", entity.getType())
							.put("name", entity.getName())
							.put("is_abstract", entity.isAbstract())
							.put("x_coordinate",
									(entity.getA().getX() +
											entity.getB().getX()) / 2)
							.put("y_coordinate",
									(entity.getA().getY() +
											entity.getB().getY()) / 2);

					JSONArray attributesArray = new JSONArray();

					entity.getList().forEach(attribute ->  {
						JSONObject attributeJson = new JSONObject()
								.put("type", attribute.getType())
								.put("name", attribute.getName())
								.put("is_function", attribute.isFunction())
								.put("modifier", attribute.getAccessModifiers());


						attributesArray.put(attributeJson);
					});

					JSONArray from = new JSONArray();
					JSONArray to = new JSONArray();
					env.getRelationships().forEach(relationship -> {

						if (relationship.getEntity1().getUuid() == entity.getUuid()) {
							from.put(
									new JSONObject()
											.put("id", relationship.getEntity2().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
						if (relationship.getEntity2().getUuid() == entity.getUuid()) {
							to.put(
									new JSONObject()
											.put("id", relationship.getEntity1().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
					});

					classModel.put("attributes", attributesArray)
							.put("from", from)
							.put("to", to);

					classArrayEnv.put(classModel);
				});

				System.out.println("class array local: " + classArrayEnv.toString(4));
				System.out.println("class array pulled: " + classArray.toString(4));

				Set<String> IDs = new HashSet<>();

				classArray.forEach(classEntity -> {
					IDs.add( String.valueOf( ((JSONObject)classEntity).get("id")) );
				});

				classArrayEnv.forEach(classEntity -> {
					System.out.println(((JSONObject)classEntity).get("id"));
					if(!IDs.contains( String.valueOf( ((JSONObject)classEntity).get("id") ) ) ) {
						classArray.put(classEntity);
					}
				});

				System.out.println("class array merged: " + classArray.toString(4));



				// Creating merged model from merged json
				Env envMerged = Env.getInstanceMerged();
				envMerged.getRelationships().clear();
				envMerged.getEntities().clear();

				// Creating env for merged json
				{
					Map<String, Entity> entitiesMap = new HashMap<>();

					classArray.forEach(classEntity -> {
						Entity entity = new Entity(Mode.DRAW_RECTANGLE);
						JSONObject jsonEntity = (JSONObject) classEntity;

						entitiesMap.put(String.valueOf(jsonEntity.get("id")), entity);
					});

					classArray.forEach(classEntity -> {
						JSONObject jsonEntity = (JSONObject) classEntity;

						String entityID = String.valueOf(jsonEntity.get("id"));
						Entity entity = entitiesMap.get(entityID);

						int x_coordinate = jsonEntity.getInt("x_coordinate");
						int y_coordinate = jsonEntity.getInt("y_coordinate");
						String entityType = jsonEntity.getString("type");
						boolean isAbstract = jsonEntity.getBoolean("is_abstract");

						String name = jsonEntity.getString("name");

						JSONArray attributes = jsonEntity.getJSONArray("attributes");

						attributes.forEach(jsonAttributeRaw -> {
							JSONObject jsonAttribute = (JSONObject) jsonAttributeRaw;
							String attrName = jsonAttribute.getString("name");
							String modifier = jsonAttribute.getString("modifier");
							String type = jsonAttribute.getString("type");
							boolean function = jsonAttribute.getBoolean("is_function");

							Attributes attribute = new Attributes(modifier, attrName, type, function);
							entity.getList().add(attribute);
						});

						entity.init(envMerged, x_coordinate, y_coordinate);
						entity.setName(name);
						entity.setType(entityType);
						entity.setUUID(entityID);
						entity.setAbstract(isAbstract);
						envMerged.addEntity(entity);
						envMerged.onSelectionChanged();

						JSONArray from = (JSONArray) jsonEntity.get("from");
						from.forEach(relationObjectRaw -> {
							String id = String.valueOf(((JSONObject) relationObjectRaw).get("id"));
							String connectionType = ((JSONObject) relationObjectRaw).getString("connection_type");
							Relationship r = new Relationship();
							r.setEntity1(entity);
							r.setEntity2(entitiesMap.get(id));
							r.setName(connectionType);
							envMerged.addRelationship(r);
							envMerged.onSelectionChanged();
						});

					});
				}


				if(env.getUuid() == null) {
					env.setUuid(UUID.fromString(String.valueOf(responseJson.get("id"))));
				}

				new CollaborationStage(env, envMerged, envPulled);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			AppCore.PULL_REQUIRED = false;
		});

		redo.addActionListener(e -> {
			try {
				if(env.getVersion() == env.getMaxVersion()) {
					JOptionPane.showMessageDialog(null, "Nema dalje brale");
					return;
				}

				String url = AppCore.BACKEND_URL + "projects/" + AppCore.PROJECT_URL + "/models/" + AppCore.CLASS_MODEL_ID + "?version=" + (env.getVersion() + 1);
				env.setVersion(env.getVersion() + 1);

				HttpClient client = HttpClient.newHttpClient();

				HttpRequest request = HttpRequest.newBuilder(URI.create(url))
						.header("Content-type", "application/json")
						.header("user_id", AppCore.USER_ID)
						.GET()
						.build();


				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				JSONArray returnJsonArray = new JSONArray(response.body());
				System.out.println(returnJsonArray.toString(4));

				JSONObject returnJson = returnJsonArray.getJSONObject(0);

				JSONArray classArray = returnJson.getJSONArray("class_model");



				env.getEntities().clear();
				env.getRelationships().clear();

				Map<String, Entity> entitiesMap = new HashMap<>();

				classArray.forEach(classEntity -> {
					Entity entity = new Entity(Mode.DRAW_RECTANGLE);
					JSONObject jsonEntity = (JSONObject) classEntity;

					entitiesMap.put(jsonEntity.getString("id"), entity);
				});

				classArray.forEach(classEntity -> {
					JSONObject jsonEntity = (JSONObject) classEntity;

					String entityID = jsonEntity.getString("id");
					Entity entity = entitiesMap.get(entityID);

					int x_coordinate = jsonEntity.getInt("x_coordinate");
					int y_coordinate = jsonEntity.getInt("y_coordinate");

					String name = jsonEntity.getString("name");

					JSONArray attributes = jsonEntity.getJSONArray("attributes");

					attributes.forEach(jsonAttributeRaw -> {
						JSONObject jsonAttribute = (JSONObject) jsonAttributeRaw;
						String attrName = jsonAttribute.getString("name");
						String modifier = jsonAttribute.getString("modifier");
						String type = jsonAttribute.getString("type");
						boolean function = jsonAttribute.getBoolean("is_function");

						Attributes attribute = new Attributes(modifier, attrName, type, function);
						entity.getList().add(attribute);
					});

					entity.init(env, x_coordinate, y_coordinate);
					entity.setName(name);
					env.addEntity(entity);
					env.onSelectionChanged();

					JSONArray from = (JSONArray) jsonEntity.get("from");
					from.forEach(relationObjectRaw -> {
						String id = ((JSONObject) relationObjectRaw).getString("id");
						String connectionType = ((JSONObject) relationObjectRaw).getString("connection_type");
						Relationship r = new Relationship();
						r.setEntity1(entity);
						r.setEntity2(entitiesMap.get(id));
						r.setName(connectionType);
						env.addRelationship(r);
						env.onSelectionChanged();
					});

				});


			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});

		undo.addActionListener(e -> {
			try {
				if(env.getVersion() == 1) {
					JOptionPane.showMessageDialog(null, "Cant undo, this is first version");
					return;
				}
				String url = AppCore.BACKEND_URL + "projects/" + AppCore.PROJECT_URL + "/models/" + AppCore.CLASS_MODEL_ID + "?version=" + (env.getVersion() - 1);
				env.setVersion(env.getVersion() - 1);

				HttpClient client = HttpClient.newHttpClient();

				HttpRequest request = HttpRequest.newBuilder(URI.create(url))
						.header("Content-type", "application/json")
						.header("user_id", AppCore.USER_ID)
						.GET()
						.build();


				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				JSONArray returnJsonArray = new JSONArray(response.body());
				System.out.println(returnJsonArray.toString(4));

				JSONObject returnJson = returnJsonArray.getJSONObject(0);

				JSONArray classArray = returnJson.getJSONArray("class_model");



				env.getEntities().clear();
				env.getRelationships().clear();

				Map<String, Entity> entitiesMap = new HashMap<>();

				classArray.forEach(classEntity -> {
					Entity entity = new Entity(Mode.DRAW_RECTANGLE);
					JSONObject jsonEntity = (JSONObject) classEntity;

					entitiesMap.put(jsonEntity.getString("id"), entity);
				});

				classArray.forEach(classEntity -> {
					JSONObject jsonEntity = (JSONObject) classEntity;

					String entityID = jsonEntity.getString("id");
					Entity entity = entitiesMap.get(entityID);

					int x_coordinate = jsonEntity.getInt("x_coordinate");
					int y_coordinate = jsonEntity.getInt("y_coordinate");

					String name = jsonEntity.getString("name");

					JSONArray attributes = jsonEntity.getJSONArray("attributes");

					attributes.forEach(jsonAttributeRaw -> {
						JSONObject jsonAttribute = (JSONObject) jsonAttributeRaw;
						String attrName = jsonAttribute.getString("name");
						String modifier = jsonAttribute.getString("modifier");
						String type = jsonAttribute.getString("type");
						boolean function = jsonAttribute.getBoolean("is_function");

						Attributes attribute = new Attributes(modifier, attrName, type, function);
						entity.getList().add(attribute);
					});

					entity.init(env, x_coordinate, y_coordinate);
					entity.setName(name);
					env.addEntity(entity);
					env.onSelectionChanged();

					JSONArray from = (JSONArray) jsonEntity.get("from");
					from.forEach(relationObjectRaw -> {
						String id = ((JSONObject) relationObjectRaw).getString("id");
						String connectionType = ((JSONObject) relationObjectRaw).getString("connection_type");
						Relationship r = new Relationship();
						r.setEntity1(entity);
						r.setEntity2(entitiesMap.get(id));
						r.setName(connectionType);
						env.addRelationship(r);
						env.onSelectionChanged();
					});

				});


			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		// Saving functionality
		save.addActionListener(e -> {

			if(AppCore.PULL_REQUIRED) {
				JOptionPane.showMessageDialog(null, "You have to pull first");
				return;
			}

			try {


				JSONArray classArray = new JSONArray();

				env.getEntities().forEach(entity -> {
					JSONObject classModel = new JSONObject()
							.put("id", entity.getUuid())
							.put("type", entity.getType())
							.put("name", entity.getName())
							.put("is_abstract", entity.isAbstract())
							.put("x_coordinate",
											(entity.getA().getX() +
											 entity.getB().getX()) / 2)
							.put("y_coordinate",
											(entity.getA().getY() +
											 entity.getB().getY()) / 2);

					JSONArray attributesArray = new JSONArray();

					entity.getList().forEach(attribute ->  {
						JSONObject attributeJson = new JSONObject()
									.put("type", attribute.getType())
									.put("name", attribute.getName())
									.put("is_function", attribute.isFunction())
									.put("modifier", attribute.getAccessModifiers());


						attributesArray.put(attributeJson);
					});

					JSONArray from = new JSONArray();
					JSONArray to = new JSONArray();
					env.getRelationships().forEach(relationship -> {

						if (relationship.getEntity1().getUuid() == entity.getUuid()) {
							from.put(
									new JSONObject()
											.put("id", relationship.getEntity2().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
						if (relationship.getEntity2().getUuid() == entity.getUuid()) {
							to.put(
									new JSONObject()
											.put("id", relationship.getEntity1().getUuid())
											.put("connection_type", relationship.getName())
							);
						}
					});

					classModel.put("attributes", attributesArray)
								.put("from", from)
								.put("to", to);

					classArray.put(classModel);
				});

				JSONObject requestBody = new JSONObject()
						.put("model_type", "class_model")
						.put("class_model", classArray);

				if(env.getUuid() != null) {
					requestBody.put("id", env.getUuid());
					requestBody.put("version", env.getVersion());
				}

				System.out.println(requestBody.toString(4));

				String url = AppCore.BACKEND_URL + "projects/" + AppCore.PROJECT_URL + "/models";

				HttpClient client = HttpClient.newHttpClient();

 				HttpRequest request = HttpRequest.newBuilder(URI.create(url))
						.header("Content-type", "application/json")
						.header("user_id", AppCore.USER_ID)
						.POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
						.build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				JSONObject returnJson = new JSONObject(response.body());
				System.out.println(returnJson.toString(4));

				if(returnJson.get("status").equals("ok") ) {
					if(env.getUuid() == null) {
						env.setUuid(UUID.fromString(String.valueOf(returnJson.get("id"))));
					}
					JOptionPane.showMessageDialog(null, "Saved!");
					AppCore.CLASS_MODEL_ID = String.valueOf(returnJson.get("id"));

					env.setVersion(Integer.parseInt(String.valueOf(returnJson.get("version"))));
					env.setMaxVersion(Integer.parseInt(String.valueOf(returnJson.get("version"))));
				} else {
					String message = "Operation failed with status: " + ((JSONObject)returnJson.get("error")).get("code")
							+ "\nMessage: " + ((JSONObject)returnJson.get("error")).get("message");
					JOptionPane.showMessageDialog(null, message);
				}

				AppCore.PULL_REQUIRED = true;


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
