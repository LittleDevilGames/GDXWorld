package com.blastedstudios.gdxworld.world.quest.manifestation;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Interface client must implement to be able to execute/run the given
 * manifestation of a quest
 */
public interface IQuestManifestationExecutor {
	/**
	 * Add dialog to related quest
	 */
	public void addDialog(String dialog, String origin);
	
	/**
	 * @return named physics object, up to client. Could set user data
	 * name string, or use GDXShape user data and map name, or whatever
	 */
	public Body getPhysicsObject(String name);
}