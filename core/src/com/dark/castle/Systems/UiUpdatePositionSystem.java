package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.VisTouchpad;
import com.kotcrab.vis.runtime.component.Layer;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.Renderable;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.HashMap;

/**
 * Created by DzzirtNik on 01.05.2016.
 */
public class UiUpdatePositionSystem extends IteratingSystem implements AfterSceneInit {

    private ComponentMapper<Transform> transformCmp;
    private CameraManager cameraManager;
    private HashMap<Integer, Vector2> distanceFromCamCenter;

    public UiUpdatePositionSystem() {
        super(Aspect.one(Button.class, VisTouchpad.class));
        distanceFromCamCenter = new HashMap<Integer, Vector2>();
    }

    @Override
    protected void inserted(int entityId) {
        Vector3 camPos = cameraManager.getCamera().position;
        Transform btnTransform = transformCmp.get(entityId);
        Vector2 dist = new Vector2(camPos.x - btnTransform.getX(), camPos.y - btnTransform.getY());
        distanceFromCamCenter.put(entityId, dist);
    }

    @Override
    protected void process(int entityId) {
        Vector3 camPos = cameraManager.getCamera().position;
        Transform entityTransform = transformCmp.get(entityId);
        Vector2 newPos = new Vector2(camPos.x, camPos.y).sub(distanceFromCamCenter.get(entityId));
        entityTransform.setX(newPos.x);
        entityTransform.setY(newPos.y);
    }

    @Override
    public void afterSceneInit() {


    }

    @Override
    protected void initialize() {
        Entity moveTouchpad = CreateTouchpadEntity();
        Entity attackTouchpad = CreateTouchpadEntity();
        VisTouchpad moveVt = moveTouchpad.getComponent(VisTouchpad.class);
        VisTouchpad attackVt = attackTouchpad.getComponent(VisTouchpad.class);
        moveVt.bounds = new Rectangle(0.3829f, 0.0481f, 1.7f, 1.69f);
        attackVt.bounds = new Rectangle(3.8399f, 0.017f, 1.42f, 1.52f);
        moveVt.touchpad = CreateTouchpadData("gfx/moveBg.png", "gfx/moveKnob.png", 10, moveVt.bounds);
        attackVt.touchpad = CreateTouchpadData("gfx/attackBg.png", "gfx/attackKnob.png", 10, attackVt.bounds);
        Transform moveTransform = moveTouchpad.getComponent(Transform.class);
        Transform attackTransform = attackTouchpad.getComponent(Transform.class);
        moveTransform.setPosition(0.3829f, 0.0481f);
        attackTransform.setPosition(3.8399f, 0.017f);
    }

    private Entity CreateTouchpadEntity() {
        Entity e = world.createEntity();
        e.edit()
                .add(new Renderable(10))
                .add(new Layer(4))
                .add(new Transform())
                .add(new Tint())
                .add(new Origin())
                .add(new VisTouchpad());

        return e;
    }

    Touchpad CreateTouchpadData(String bgPath, String knobPath, float deadzone, Rectangle bounds) {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture(bgPath));
        touchpadSkin.add("touchKnob", new Texture(knobPath));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpadStyle.knob.setMinHeight(touchpadStyle.knob.getMinHeight() * 0.01f);
        touchpadStyle.knob.setMinWidth(touchpadStyle.knob.getMinWidth() * 0.01f);
        Touchpad touchpad = new Touchpad(deadzone, touchpadStyle);
        touchpad.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        return touchpad;

    }
}
