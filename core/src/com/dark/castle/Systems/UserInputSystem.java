package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.dark.castle.Components.Bounds;
import com.dark.castle.Components.Button;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisGroupManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 30.04.2016.
 */
public class UserInputSystem extends BaseSystem implements AfterSceneInit, InputProcessor {
    private VisIDManager idManager;
    private CameraManager cameraManager;

    private Entity leftArrow;
    private Entity rightArrow;
    private Entity jumpArrow;
    private Entity atkArrow;

    private final int NONE = -1;
    private int pointerLeft = NONE;
    private int pointerRight = NONE;
    private int pointerJump = NONE;
    private int pointerAtk = NONE;

    private ComponentMapper<Button> buttonCmp;

    public UserInputSystem() {

    }


    @Override
    protected void processSystem() {

    }

    @Override
    public void afterSceneInit() {
        Gdx.input.setInputProcessor(this);
        leftArrow = idManager.get("leftArrow");
        rightArrow = idManager.get("rightArrow");
        jumpArrow = idManager.get("jumpArrow");
        atkArrow = idManager.get("atkArrow");

        buttonCmp.create(leftArrow);
        buttonCmp.create(rightArrow);
        buttonCmp.create(jumpArrow);
        buttonCmp.create(atkArrow);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = GetInWorldCoordinates(screenX, screenY);
        VisPolygon leftArrowPolygon = leftArrow.getComponent(VisPolygon.class);
        VisPolygon rightArrowPolygon = rightArrow.getComponent(VisPolygon.class);
        VisPolygon jumpArrowPolygon = jumpArrow.getComponent(VisPolygon.class);
        VisPolygon atkArrowPolygon = atkArrow.getComponent(VisPolygon.class);

        Transform leftArrowTransform = leftArrow.getComponent(Transform.class);
        Transform rightArrowTransform = rightArrow.getComponent(Transform.class);
        Transform jumpArrowTransform = jumpArrow.getComponent(Transform.class);
        Transform atkArrowTransform = atkArrow.getComponent(Transform.class);

        if (GetBounds(leftArrowPolygon, leftArrowTransform).contains(touchPos.x, touchPos.y)) {
            pointerLeft = pointer;
            leftArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(rightArrowPolygon, rightArrowTransform).contains(touchPos.x, touchPos.y)) {
            pointerRight = pointer;
            rightArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(jumpArrowPolygon, jumpArrowTransform).contains(touchPos.x, touchPos.y)) {
            pointerJump = pointer;
            jumpArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(atkArrowPolygon, atkArrowTransform).contains(touchPos.x, touchPos.y)) {
            pointerAtk = pointer;
            atkArrow.getComponent(Button.class).state = Button.State.PRESSED;
        }
        return true;
    }

    private Rectangle GetBounds(VisPolygon polygon, Transform transform) {
        float x = transform.getX();
        float y = transform.getY();
        float width = polygon.vertices.get(1).x - polygon.vertices.get(0).x;
        float height = polygon.vertices.get(3).y - polygon.vertices.get(0).y;
        return new Rectangle(x, y, width, height);
    }

    private Vector3 GetInWorldCoordinates(int screenX, int screenY) {
        Vector3 unproject = new Vector3(screenX, screenY, 0);
        return cameraManager.getCamera().unproject(unproject);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == pointerLeft) {
            leftArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerLeft = NONE;
        } else if (pointer == pointerRight) {
            rightArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerRight = NONE;
        } else if (pointer == pointerJump) {
            jumpArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerJump = NONE;
        } else if (pointer == pointerAtk) {
            atkArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerAtk = NONE;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
