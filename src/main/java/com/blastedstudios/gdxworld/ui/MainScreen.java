package com.blastedstudios.gdxworld.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.worldeditor.WorldEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.ui.FileChooserWrapper;
import com.blastedstudios.gdxworld.util.ui.FileChooserWrapper.IFileChooserHandler;
import com.blastedstudios.gdxworld.world.GDXWorld;

public class MainScreen extends AbstractScreen{
	public MainScreen(final GDXGame game){
		super(game, "data/ui/uiskin.json");
		final Button newButton = new TextButton("New", skin);
		final Button loadButton = new TextButton("Load", skin);
		final Button exitButton = new TextButton("Exit", skin);
		newButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				game.pushScreen(new WorldEditorScreen(game, null, null));
			}
		});
		loadButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				IFileChooserHandler handler = new IFileChooserHandler() {
					@Override public void handle(FileHandle handle) {
						game.pushScreen(new WorldEditorScreen(game, GDXWorld.load(handle), handle));
					}
				};
				FileChooserWrapper.createFileChooser(getStage(), skin, handler);
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		Window window = new Window("GDX World Editor", skin);
		window.add(newButton);
		window.row();
		window.add(loadButton).colspan(2);
		window.row();
		window.add(exitButton).colspan(2);
		window.pack();
		window.setX(Gdx.graphics.getWidth()/2 - window.getWidth()/2);
		window.setY(Gdx.graphics.getHeight()/2 - window.getHeight()/2);
		stage.addActor(window);
	}
	
	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
	}
}
