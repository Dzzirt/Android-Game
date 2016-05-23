package com.dark.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Systems.CameraTrackingSystem;
import com.dark.castle.Systems.GameConfigManager;
import com.dark.castle.Systems.PlatformMovingSystem;
import com.dark.castle.Systems.PlayerMovementSystem;
import com.dark.castle.Systems.UiUpdatePositionSystem;
import com.dark.castle.Systems.UserInputSystem;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneFeature;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DarkCastle extends ApplicationAdapter {

	SpriteBatch batch;
	VisAssetManager manager;
	Scene scene;

	@Override
	public void create () {
		batch = new SpriteBatch();
		SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
		parameter.config.addSystem(CameraTrackingSystem.class,2);
		parameter.config.addSystem(UserInputSystem.class, 1);
		parameter.config.addSystem(PlayerMovementSystem.class, 1);
		parameter.config.addSystem(UiUpdatePositionSystem.class, 0);
		parameter.config.addSystem(GameConfigManager.class, 0);
		parameter.config.addSystem(PlatformMovingSystem.class, 0);
		parameter.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
		manager = new VisAssetManager(batch);
		scene = manager.loadSceneNow("scene/main.scene", parameter);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.render();
	}

	@Override
	public void resize(int width, int height) {
		scene.resize(width, height);
	}
}
