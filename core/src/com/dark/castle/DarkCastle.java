package com.dark.castle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dark.castle.Systems.AnimationSystem;
import com.dark.castle.Systems.CameraTrackingSystem;
import com.dark.castle.Systems.AdditionalComponentLoader;
import com.dark.castle.Systems.PhysicSpriterUpdateSystem;
import com.dark.castle.Systems.PlatformMovingSystem;
import com.dark.castle.Systems.PlayerMovementSystem;
import com.dark.castle.Systems.SpriterPhysicCreationSystem;
import com.dark.castle.Systems.UiUpdatePositionSystem;
import com.dark.castle.Systems.UserInputSystem;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneFeature;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.util.SpriterData;

public class DarkCastle extends ApplicationAdapter {
	
	private SpriteBatch batch;
	public static VisAssetManager manager;
	Scene scene;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new VisAssetManager(batch);
		SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
		parameter.config.addSystem(CameraTrackingSystem.class, 2);
		parameter.config.addSystem(UserInputSystem.class, 1);
		parameter.config.addSystem(PlayerMovementSystem.class, 1);
		parameter.config.addSystem(UiUpdatePositionSystem.class, 1);
		parameter.config.addSystem(AdditionalComponentLoader.class, 0);
		parameter.config.addSystem(PlatformMovingSystem.class, 1);
		parameter.config.addSystem(AnimationSystem.class, 1);
		parameter.config.addSystem(PhysicSpriterUpdateSystem.class, 1);
		parameter.config.addSystem(SpriterPhysicCreationSystem.class, 0);
		parameter.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
		parameter.config.disable(SceneFeature.DIRTY_CLEANER_SYSTEM);
		//parameter.config.disable(SceneFeature.SPRITER_RENDER_SYSTEM);
		manager.load("spriter/Player/elisa.scml", SpriterData.class);
		manager.load("spriter/Goblin_enemy/Goblin_enemy_smallhead.scml", SpriterData.class);
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
