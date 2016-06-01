package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.MovingPlatform;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.Velocity;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class AdditionalComponentLoader extends BaseEntitySystem{
    private ComponentMapper<VisID> idComponentMapper;
    private ComponentMapper<Variables> varsCmp;

    private ComponentMapper<Velocity> horVelCmp;
    private ComponentMapper<MovingPlatform> movingPlatformCmp;
    private ComponentMapper<PhysicStates> physicStatesCmp;

    public static JsonValue cfg = new JsonReader().parse(Gdx.files.internal("config.json"));

    public AdditionalComponentLoader() {
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
        AddVelocity(entityId, cfg.get(idComponentMapper.get(entityId).id + index));
        AddMovingPlatform(entityId, cfg.get(idComponentMapper.get(entityId).id + index));
        AddPhysicStates(entityId);
    }

    private void AddVelocity(int entityId, JsonValue arr) {
        if (arr != null && arr.has("xVel") && arr.has("yVel")) {
            Velocity vel =  horVelCmp.create(entityId);
            vel.x = arr.get("xVel").asFloat();
            vel.y = arr.get("yVel").asFloat();
        }
    }

    private void AddMovingPlatform(int entityId, JsonValue arr) {
        if (arr != null && arr.has("distance")) {
            MovingPlatform mp =  movingPlatformCmp.create(entityId);
            mp.distance = arr.get("distance").asFloat();
        }
    }

    private void AddPhysicStates(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        if (id.equals("player") || id.equals("enemy")) {
            physicStatesCmp.create(entityId);
        }
    }
}
