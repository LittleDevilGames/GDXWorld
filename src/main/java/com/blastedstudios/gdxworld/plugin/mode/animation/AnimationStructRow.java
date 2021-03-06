package com.blastedstudios.gdxworld.plugin.mode.animation;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.plugin.mode.animation.AnimationManifestationWindow.IRemovedListener;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.animation.AnimationStruct;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class AnimationStructRow {
	private final AnimationStruct struct;
	private final TextField timeField;
	private final TextButton editButton, removeButton;
	private final Skin skin;
	private final Stage stage;
	private AnimationManifestationWindow window;
	
	public AnimationStructRow(final Skin skin, Stage stage, final IAnimationRowListener listener, AnimationStruct struct){
		this.skin = skin;
		this.struct = struct;
		this.stage = stage;
		timeField = new TextField(struct.time+"", skin);
		timeField.setMessageText("<time>");
		removeButton = new TextButton("X", skin);
		removeButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.removed(AnimationStructRow.this);
			}
		});
		editButton = new TextButton("Edit", skin);
		editButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.createEditWindow();
				createEditWindow();
			}
		});
	}
	
	private AbstractWindow createEditWindow() {
		if(window != null)
			window.remove();
		IRemovedListener listener = new IRemovedListener() {
			@Override public void removed() {
				if(window != null);
					window.remove();
				window = null;
			}
		};
		stage.addActor(window = new AnimationManifestationWindow(skin, this, listener));
		return window;
	}

	public void addSelfToTable(Table table){
		LinkedList<AbstractQuestManifestation> manifestations = new LinkedList<AbstractQuestManifestation>();
		int selected = 0, i = 0;
		for(IQuestComponentManifestation manifestation : PluginUtil.getPlugins(IQuestComponentManifestation.class)){
			AbstractQuestManifestation toAdd = (AbstractQuestManifestation) manifestation.getDefault().clone();
			if(manifestation.getDefault().getClass() == struct.manifestation.getClass()){
				toAdd = struct.manifestation;
				selected = i;
			}
			manifestations.add(toAdd);
			i++;
		}
		final SelectBox<AnimationManifestationStruct> manifestationSelectBox = new SelectBox<>(skin);
		manifestationSelectBox.setItems(AnimationManifestationStruct.create(manifestations));
		manifestationSelectBox.setSelectedIndex(selected);
		manifestationSelectBox.addListener(new ChangeListener() {
			@Override public void changed(ChangeEvent event, Actor actor) {
				struct.manifestation = manifestationSelectBox.getSelected().manifestation;
			}
		});
		table.add(manifestationSelectBox);
		table.add(timeField);
		table.add(removeButton);
		table.add(editButton);
	}
	
	/**
	 * Apply current ui and grab struct
	 */
	public AnimationStruct apply(){
		struct.time = Float.parseFloat(timeField.getText());
		return struct;
	}
	
	public AnimationStruct getStruct() {
		return struct;
	}

	interface IAnimationRowListener{
		void createEditWindow();
		void removed(AnimationStructRow row);
	}
	
	public boolean remove(){
		return window == null || window.remove();
	}
}
