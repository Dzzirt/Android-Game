package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Components.HealthBar;
import com.dark.castle.Components.UIElement;
import com.dark.castle.Components.CharacterStats;
import com.dark.castle.Components.MovingPlatform;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.Velocity;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.AssetReference;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.component.VisSpriter;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class AdditionalComponentLoader extends BaseEntitySystem{
    private ComponentMapper<VisID> idComponentMapper;
    private ComponentMapper<Variables> varsCmp;
    private ComponentMapper<AssetReference> assetRefCmp;
    private ComponentMapper<VisSprite> spriteCmp;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformCmp;
    private ComponentMapper<VisPolygon> polygonCmp;

    private ComponentMapper<Velocity> horVelCmp;
    private ComponentMapper<MovingPlatform> movingPlatformCmp;
    private ComponentMapper<PhysicStates> physicStatesCmp;
    private ComponentMapper<UIElement> buttonCmp;
    private ComponentMapper<AnimationStates> animStatesCmp;
    private ComponentMapper<CharacterStats> charStatsCmp;
    private ComponentMapper<HealthBar> healthBarCmp;

    public static JsonValue cfg = new JsonReader().parse(Gdx.files.internal("config.json"));

    public AdditionalComponentLoader() {
        super(Aspect.all(VisID.class));
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
        AddUIElement(entityId);
        AddAnimationStates(entityId);
        AddCharacterStats(entityId, cfg.get(idComponentMapper.get(entityId).id));
        AddHealthBar(entityId);
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

    private void AddUIElement(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        Transform transform = transformCmp.get(entityId);
        if (id.id.equals("leftArrow")
                || id.id.equals("rightArrow")
                || id.id.equals("jumpArrow")
                || id.id.equals("attackArrow")
                || id.id.equals("slidingArrow")
                || id.id.equals("hp_bar")
                || id.id.equals("hp_line")
                || id.id.equals("attack_up")
                || id.id.equals("hp_regen")
                || id.id.equals("speed_up")
                || id.id.equals("pause")){
            UIElement button = buttonCmp.create(entityId);
            button.originPos = new Vector2(transform.getX(), transform.getY());
        }
    }

    private void AddCharacterStats(int entityId, JsonValue val) {
        if (val != null && val.has("damage") && val.has("health")){
            CharacterStats characterStats = charStatsCmp.create(entityId);
            characterStats.damage = val.get("damage").asIntArray();
            characterStats.originHp = characterStats.health = val.get("health").asFloat();
        }
    }

    private void AddAnimationStates(int entityId) {
        VisID id = idComponentMapper.get(entityId);

        if (id.id.equals("player") || id.id.equals("enemy") || id.id.equals("test")) {
            AnimationStates animationStates = animStatesCmp.create(entityId);
            if (id.id.equals("enemy")) {
                animationStates.getState("Attack").isPlaying = true;
            }
        }

    }

    private void AddHealthBar(int entityId) {
        VisID id = idComponentMapper.get(entityId);
        if (id.id.equals("enemy")) {
            HealthBar healthBar = healthBarCmp.create(entityId);
            healthBar.region = new TextureRegion(new Texture("gfx/UI/mini-health.png"));
            healthBar.scale = new Vector2(0.006f, 0.006f);
            healthBar.position = new Vector2(0, 0);
        } else if (id.id.equals("player")) {
            HealthBar healthBar = healthBarCmp.create(entityId);
            healthBar.region = new TextureRegion(new Texture("gfx/UI/playerHpNew.png"));
            healthBar.scale = new Vector2(0.005f, 0.005f);
            healthBar.position = new Vector2(0, 0);
        }
    }
}
