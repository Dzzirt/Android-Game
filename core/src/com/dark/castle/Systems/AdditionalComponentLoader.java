package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.MovingPlatform;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.Velocity;
import com.dark.castle.DarkCastle;
import com.kotcrab.vis.runtime.assets.SpriterAsset;
import com.kotcrab.vis.runtime.component.AssetReference;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.util.SpriterData;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class AdditionalComponentLoader extends BaseEntitySystem{
    private ComponentMapper<VisID> idComponentMapper;
    private ComponentMapper<Variables> varsCmp;
    private ComponentMapper<AssetReference> assetRefCmp;
    private ComponentMapper<VisSprite> spriteCmp;
    private ComponentMapper<VisSpriter> spriterCmp;

    private ComponentMapper<Velocity> horVelCmp;
    private ComponentMapper<MovingPlatform> movingPlatformCmp;
    private ComponentMapper<PhysicStates> physicStatesCmp;
    private ComponentMapper<Button> buttonCmp;
    private ComponentMapper<AnimationStates> animStatesCmp;

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
        AddButton(entityId);
        AddSpriter(entityId, cfg.get(idComponentMapper.get(entityId).id));
        AddAnimationStates(entityId);
    }

    private void AddVelocity(int entityId, JsonValue val) {
        if (val != null && val.has("xVel") && val.has("yVel")) {
            Velocity vel =  horVelCmp.create(entityId);
            vel.x = val.get("xVel").asFloat();
            vel.y = val.get("yVel").asFloat();
        }
    }

    private void AddMovingPlatform(int entityId, JsonValue val) {
        if (val != null && val.has("distance")) {
            MovingPlatform mp =  movingPlatformCmp.create(entityId);
            mp.distance = val.get("distance").asFloat();
        }
    }

    private void AddPhysicStates(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        if (id.id.equals("player") || id.id.equals("enemy")) {
            physicStatesCmp.create(entityId);
        }
    }

    private void AddButton(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        if (id.id.equals("leftArrow")
                || id.id.equals("rightArrow")
                || id.id.equals("jumpArrow")
                || id.id.equals("leftAtkArrow")
                || id.id.equals("rightAtkArrow")
                || id.id.equals("slidingArrow")){
            buttonCmp.create(entityId);
        }
    }

    private void AddSpriter(int entityId, JsonValue val) {

        VisID id = idComponentMapper.get(entityId);
        if (id.id.equals("player")) {
            SpriterData spriterData = DarkCastle.manager.get(val.getString("animPath"));
            VisSpriter visSpriter = new VisSpriter(spriterData.loader, spriterData.data, 0.01f);
            visSpriter.getPlayer().scale(0.01f);
            visSpriter.getPlayer().setAnimation("Stop");
            visSpriter.setAnimationPlaying(true);
            assetRefCmp.create(entityId).asset = new SpriterAsset("spriter/Player/elisa.scml", 0.01f);
            spriteCmp.get(entityId).setSize(0, 0);
            world.getEntity(entityId).edit().add(visSpriter);
        }
    }

    private void AddAnimationStates(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        Boolean stop;
        if (id.id.equals("player") || id.id.equals("enemy")) {
           stop = animStatesCmp.create(entityId).getState("Stop").isPlaying;
        }
        System.out.println(1);
    }
}
