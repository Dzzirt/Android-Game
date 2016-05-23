package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.MovingPlatform;
import com.dark.castle.Components.Velocity;
import com.kotcrab.vis.runtime.component.Variables;
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
    private ComponentMapper<MovingPlatform> movingPlatformCmp;
    private ComponentMapper<Variables> varsCmp;

    public static JsonValue cfg = new JsonReader().parse(Gdx.files.internal("config.json"));

    public GameConfigManager() {
        super(Aspect.all(VisID.class).exclude(Button.class));
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected void inserted(int entityId) {
        String index = "";
        if (varsCmp.has(entityId)) {
            index = varsCmp.get(entityId).get("index");
        }
        CreateVelocity(entityId, cfg.get(idComponentMapper.get(entityId).id + index));
        CreateMovingPlatform(entityId, cfg.get(idComponentMapper.get(entityId).id + index));
    }

    private void CreateVelocity(int entityId, JsonValue arr) {
        if (arr != null && arr.has("xVel") && arr.has("yVel")) {
            Velocity vel =  horVelCmp.create(entityId);
            vel.x = arr.get("xVel").asFloat();
            vel.y = arr.get("yVel").asFloat();
        }
    }

    private void CreateMovingPlatform(int entityId, JsonValue arr) {
        if (arr != null && arr.has("distance")) {
            MovingPlatform mp =  movingPlatformCmp.create(entityId);
            mp.distance = arr.get("distance").asFloat();
        }
    }
}
