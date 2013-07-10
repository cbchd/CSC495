package com.storytime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.storytime.client.skill.offense.LetterAdditionAttack;
import com.storytime.client.skill.offense.LetterSubstitutionAttack;
import com.storytime.server.Room;
import com.storytime.server.StoryTimeEngine;
import com.storytime.server.User;

public class StoryTimeEngineTest {
	StoryTimeEngine engine;

	/**
	 * Tests to make sure that when a user logs in, they appear in the engine's
	 * onlineUsers list & all of their user object has been properly initialized
	 * 
	 */
	@Test
	public void testLoginVsOnlineUsers() {
		String username = "Tom";
		String password = "1234";
		engine = new StoryTimeEngine();
		engine.loginUser(username, password);
		int numberOfUsers = 1;
		User foundUser = engine.getOnlineUsers().get(username);
		assertEquals(foundUser.getUsername(), username);
		assertEquals(foundUser.getPassword(), password);
		assertEquals(foundUser.getRoom(), null);
		assertEquals(foundUser.getScore(), 0);
	}

	/**
	 * Tests to make sure a user can host a room
	 * 
	 */
	@Test
	public void testHostRoom() {
		String username = "Tom";
		String password = "1234";
		String roomName = "Dragons";
		String theme = "D&D";
		int numberOfPlayers = 3;
		engine = new StoryTimeEngine();
		engine.loginUser(username, password);
		User foundUser = engine.getOnlineUsers().get(username);
		engine.hostRoom(foundUser, roomName, theme, password, numberOfPlayers);
		Room foundRoom = engine.getLobbyRooms().get(roomName);
		assertNotNull(foundRoom);
		assertEquals(foundRoom.getRoomName(), roomName);
		assertEquals(foundRoom.getHost().getUsername(), foundUser.getUsername());
		assertEquals(foundRoom.getTheme(), theme);
	}

	/**
	 * Tests to make sure a user can join a hosted room
	 * 
	 */
	@Test
	public void testJoinRoom() {
		engine = new StoryTimeEngine();
		String username1 = "Tom";
		String password1 = "1234";
		String username2 = "Jerry";
		String password2 = "4321";
		String roomName = "Dragons";
		String theme = "D&D";
		String password = "";
		int numberOfPlayers = 3;
		engine.loginUser(username1, password1);
		engine.loginUser(username2, password2);

		User foundUser1 = engine.getOnlineUsers().get(username1);
		User foundUser2 = engine.getOnlineUsers().get(username2);

		engine.hostRoom(foundUser1, roomName, theme, password, numberOfPlayers);
		engine.joinRoom(foundUser2, roomName);

		Room foundRoom = engine.getLobbyRooms().get(roomName);
		assertNotNull(foundRoom.getUsers().get(username2));
	}
	
	@Test
	public void testActivateLetterAddition() {
		engine = new StoryTimeEngine();
		User tom = new User("Tom", "1", engine);
		String phrase = "Yolo yolo yolo yolo yolo";
		String comboToLookFor = "yolo";
		String comboToAddAfter = "r";
		tom.setPhrase(phrase);
		LetterAdditionAttack letterAdditionAttack = new LetterAdditionAttack();
		letterAdditionAttack.setAddThis(comboToAddAfter);
		letterAdditionAttack.setAfterThis(comboToLookFor);
		String editedPhrase = engine.activateLetterAdditon(tom,
				letterAdditionAttack);
//		System.out.println("The untouched phrase is: " + phrase
//				+ "\n The combination to look for is: " + comboToLookFor
//				+ "\n The combo to add after is: " + comboToAddAfter
//				+ "\n The finished phrase is: " + editedPhrase);
		assertEquals("Yolor yolor yolor yolor yolor", editedPhrase);
	}
	
	@Test
	public void testActivateLetterSubstitution() {
		engine = new StoryTimeEngine();
		User tom = new User("Tom", "1", engine);
		String phrase = "Tom slipped on a banana peel";
		String comboToLookFor = "Tom";
		String comboToSwapFor = "Santa Claus";
		LetterSubstitutionAttack substitutionAttack = new LetterSubstitutionAttack();
		tom.setPhrase(phrase);
		substitutionAttack.setPhraseToLookFor(comboToLookFor);
		substitutionAttack.setPhraseToSwapFor(comboToSwapFor);
		String editedPhrase = engine.activateLetterSubstitution(tom, substitutionAttack);
		System.out.println("Normal phrase: " + phrase);
		System.out.println("Edited: " + editedPhrase);
	}
	
	// /**Tests to make sure a user can join a hosted room
	// *
	// */
	// @Test
	// public void testLeaveRoom() {
	// engine = new StoryTimeEngine();
	// String username1 = "Tom";
	// String password1 = "1234";
	// String username2 = "Jerry";
	// String password2 = "4321";
	// String roomName = "Dragons";
	// String theme = "D&D";
	//
	// engine.loginUser(username1, password1);
	// engine.loginUser(username2, password2);
	//
	// User foundUser1 = engine.getOnlineUsers().get(username1);
	// User foundUser2 = engine.getOnlineUsers().get(username2);
	//
	// engine.hostRoom(foundUser1, roomName, theme);
	// engine.joinRoom(foundUser2, roomName);
	// engine.
	//
	// Room foundRoom = engine.getLobbyRooms().get(roomName);
	// assertNotNull(foundRoom.getUsers().get(username2));
	// }
}
