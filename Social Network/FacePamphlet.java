/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		// You fill this in
		initThings();
		addThings();

		database = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		add(canvas);

		statusField.addActionListener(this);
		pictureField.addActionListener(this);
		friendField.addActionListener(this);
		addActionListeners();
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		// makes sure that String will be something and not only ""
		if (nameField.getText().length() > 0) {
			// this is addButton methods
			if (e.getSource() == addButton) {
				if (database.containsProfile(nameField.getText())) {
					FacePamphletProfile profile = new FacePamphletProfile(nameField.getText());
					current = database.getProfile(nameField.getText());
					canvas.displayProfile(current);
					currentString = "A Profile with the name " + nameField.getText() + " already exists";
					canvas.showMessage(currentString);
				} else {
					FacePamphletProfile profile = new FacePamphletProfile(nameField.getText());
					database.addProfile(profile);
					current = database.getProfile(nameField.getText());
					canvas.displayProfile(current);
					currentString = "New profile created";
					canvas.showMessage(currentString);
				}
			} // this is deleteButton Methods
			else if (e.getSource() == deleteButton) {
				if (database.containsProfile(nameField.getText())) {
					database.deleteProfile(nameField.getText());
					current = null;
					canvas.removeAll();
					currentString = "Profile of " + nameField.getText() + " deleted";
					canvas.showMessage(currentString);
				} else {
					current = null;
					canvas.removeAll();
					currentString = "A profile with the name " + nameField.getText() + " doesn't exist";
					canvas.showMessage(currentString);
				}
			} // this is lookUpButton Methods
			else if (e.getSource() == lookupButton) {
				if (database.containsProfile(nameField.getText())) {
					current = database.getProfile(nameField.getText());
					canvas.displayProfile(database.getProfile(nameField.getText()));
					currentString = "Displaying " + nameField.getText();
					canvas.showMessage(currentString);
				} else {
					current = null;
					canvas.removeAll();
					currentString = "A profile with the name " + nameField.getText() + " doesn't exist";
					canvas.showMessage(currentString);
				}
			} // this is statusButton and statusField Methods
			else if (e.getSource() == statusButton || e.getSource() == statusField) {
				if (current != null) {
					current.setStatus(statusField.getText());
					canvas.displayProfile(current);
					currentString = "Status updated to " + statusField.getText();
					canvas.showMessage(currentString);
				} else {
					canvas.displayProfile(current);
					currentString = "Please select a profile to change status";
					canvas.showMessage(currentString);
				}
			} // this is pictureButton and pictureField Methods
			else if (e.getSource() == pictureButton || e.getSource() == pictureField) {
				if (current != null) {
					GImage image = null;
					try {
						image = new GImage(pictureField.getText());
						current.setImage(image);
						if (current.getImage() == null) {
							canvas.displayProfile(current);
							currentString = "Unable to open image file: " + pictureField.getText();
							canvas.showMessage(currentString);
						} else {
							canvas.displayProfile(current);
							currentString = "Picture updated";
							canvas.showMessage(currentString);
						}
					} catch (ErrorException ex) {
						// Code that is executed if the filename cannot be opened.
						canvas.displayProfile(current);
						currentString = "Please select picture";
						canvas.showMessage(currentString);
					}
				} else {
					canvas.displayProfile(current);
					currentString = "Please select a profile to change picture";
					canvas.showMessage(currentString);
				}
			} // this is friendButton and friendField Methods
			else if (e.getSource() == friendButton || e.getSource() == friendField) {
				if (current != null) {
					if (database.containsProfile(friendField.getText())) {
						if (current.addFriend(friendField.getText())) {
							database.getProfile(friendField.getText()).addFriend(current.getName());
							canvas.displayProfile(current);
							currentString = friendField.getText() + " added as a friend";
							canvas.showMessage(currentString);
						} else {
							canvas.displayProfile(current);
							currentString = current.getName() + " alrady has " + friendField.getText() + "as a friend";
							canvas.showMessage(currentString);
						}
					} else {
						canvas.displayProfile(current);
						currentString = friendField.getText() + " doesn't exist";
						canvas.showMessage(currentString);
					}
				} else {
					canvas.displayProfile(current);
					currentString = "Please select a profile to add friend";
					canvas.showMessage(currentString);
				}
			}
		}
	}

	// initialization methods
	private void initThings() {
		initWest();
		initNorth();
	}

	// initialization of west buttons and textfields
	private void initWest() {
		statusField = new JTextField(TEXT_FIELD_SIZE);
		statusButton = new JButton("Change Status");
		pictureField = new JTextField(TEXT_FIELD_SIZE);
		pictureButton = new JButton("Change Picture");
		friendField = new JTextField(TEXT_FIELD_SIZE);
		friendButton = new JButton("Add Friend");
	}

	// initialization of North buttons and textfields
	private void initNorth() {
		nameField = new JTextField(TEXT_FIELD_SIZE);
		addButton = new JButton("Add");
		deleteButton = new JButton("Delete");
		lookupButton = new JButton("LookUp");
	}

	// add methods
	private void addThings() {
		addWest();
		addNorth();
	}

	// adding west buttons and textfields
	private void addWest() {
		add(statusField, WEST);
		add(statusButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(pictureField, WEST);
		add(pictureButton, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(friendField, WEST);
		add(friendButton, WEST);
	}

	// adding north buttons and textfields
	private void addNorth() {
		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(addButton, NORTH);
		add(deleteButton, NORTH);
		add(lookupButton, NORTH);
	}

	private String currentString;
	private FacePamphletCanvas canvas;
	private FacePamphletProfile current;
	private FacePamphletDatabase database;
	private JTextField statusField;
	private JButton statusButton;
	private JTextField pictureField;
	private JButton pictureButton;
	private JTextField friendField;
	private JButton friendButton;
	private JTextField nameField;
	private JButton addButton;
	private JButton deleteButton;
	private JButton lookupButton;
}