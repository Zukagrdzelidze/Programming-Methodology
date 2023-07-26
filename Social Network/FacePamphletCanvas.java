/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		// You fill this in
		GLabel message = new GLabel(msg);
		message.setLocation(getWidth() / 2 - message.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
		message.setFont(MESSAGE_FONT);
		add(message);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		// You fill this in
		removeAll();
		// adds profile Name
		GLabel profileName = new GLabel(profile.getName());
		profileName.setLocation(LEFT_MARGIN, TOP_MARGIN);
		profileName.setFont(PROFILE_NAME_FONT);
		profileName.setColor(Color.BLUE);
		add(profileName);

		/*
		 * adds Image and makes sure if it is nyll or not
		 */
		if (profile.getImage() == null) {
			GRect imageRect = new GRect(LEFT_MARGIN, IMAGE_MARGIN + TOP_MARGIN + profileName.getHeight(), IMAGE_WIDTH,
					IMAGE_HEIGHT);
			add(imageRect);

			GLabel imageLabel = new GLabel("No Image");
			imageLabel.setLocation(LEFT_MARGIN + IMAGE_WIDTH / 2 - imageLabel.getWidth(),
					IMAGE_MARGIN + TOP_MARGIN + profileName.getHeight() + IMAGE_HEIGHT / 2);
			imageLabel.setFont(PROFILE_IMAGE_FONT);
			add(imageLabel);
		} else if (profile.getImage() != null) {
			GImage image = profile.getImage();
			image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			image.setLocation(LEFT_MARGIN, IMAGE_MARGIN + TOP_MARGIN + profileName.getHeight());
			add(image);
		}

		/*
		 * adds Status
		 */
		GLabel statusLabel = new GLabel("");
		statusLabel.setLocation(LEFT_MARGIN,
				STATUS_MARGIN + IMAGE_MARGIN + TOP_MARGIN + IMAGE_HEIGHT + profileName.getHeight());
		statusLabel.setFont(PROFILE_STATUS_FONT);
		if (profile.getStatus() != null) {
			statusLabel.setLabel(profile.getName() + " is " + profile.getStatus());
		} else if (profile.getStatus() == null) {
			statusLabel.setLabel("No current status");
		}
		add(statusLabel);

		/*
		 * adds list of friends
		 */
		GLabel friendLabel = new GLabel("Friends");
		friendLabel.setLocation(getWidth() / 2, IMAGE_MARGIN + TOP_MARGIN + profileName.getHeight());
		friendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendLabel);
		int i = 1;
		Iterator<String> it = profile.getFriends();
		while (it.hasNext()) {
			String str = it.next();
			GLabel eachFriendLabel = new GLabel(str);
			eachFriendLabel.setLocation(getWidth() / 2,
					IMAGE_MARGIN + TOP_MARGIN + profileName.getHeight() + friendLabel.getHeight() * i);
			eachFriendLabel.setFont(PROFILE_FRIEND_FONT);
			add(eachFriendLabel);
			i++;
		}

	}

}