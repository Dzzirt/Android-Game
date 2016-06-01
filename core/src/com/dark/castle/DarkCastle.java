package com.dark.castle;

import com.artemis.BaseSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dark.castle.Systems.AnimationSystem;
import com.dark.castle.Systems.CameraTrackingSystem;
import com.dark.castle.Systems.AdditionalComponentLoader;
import com.dark.castle.Systems.PlatformMovingSystem;
import com.dark.castle.Systems.PlayerMovementSystem;
import com.dark.castle.Systems.SpriterRenderSystem;
import com.dark.castle.Systems.UiUpdatePositionSystem;
import com.dark.castle.Systems.UserInputSystem;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneFeature;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.SystemProvider;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.EntityEngineConfiguration;
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
		parameter.config.addSystem(CameraTrackingSystem.class,1);
		parameter.config.addSystem(UserInputSystem.class, 1);
		parameter.config.addSystem(PlayerMovementSystem.class, 1);
		parameter.config.addSystem(UiUpdatePositionSystem.class, 1);
		parameter.config.addSystem(AdditionalComponentLoader.class, 0);
		parameter.config.addSystem(PlatformMovingSystem.class, 1);
		parameter.config.addSystem(AnimationSystem.class, 1);
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				return new SpriterRenderSystem(config.getSystem(RenderBatchingSystem.class));
			}
		});
		//parameter.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
		parameter.config.disable(SceneFeature.SPRITER_RENDER_SYSTEM);
		manager.load("spriter/Player/elisa.scml", SpriterData.class);
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
