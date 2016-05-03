package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.Velocity;
import com.kotcrab.vis.runtime.component.VisID;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class GameConfigManager extends BaseEntitySystem{
    private ComponentMapper<VisID> idComponentMapper;
    private ComponentMapper<Velocity> horVelCmp;

    private static JsonValue cfg = new JsonReader().parse(Gdx.files.internal("config.json"));

    public GameConfigManager() {
        super(Aspect.all(VisID.class).exclude(Button.class));
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected void inserted(int entityId) {
        CreateVelocity(entityId, cfg.get(idComponentMapper.get(entityId).id));
    }

    private void CreateVelocity(int entityId, JsonValue arr) {
        if (arr != null && arr.get("xVel") != null && arr.get("yVel") != null) {
            Velocity vel =  horVelCmp.create(entityId);
            vel.x = arr.get("xVel").asFloat();
            vel.y = arr.get("yVel").asFloat();
        }
    }
}
