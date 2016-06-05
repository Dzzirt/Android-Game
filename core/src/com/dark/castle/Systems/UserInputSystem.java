package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dark.castle.Components.UIElement;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 30.04.2016.
 */
public class UserInputSystem extends IteratingSystem implements AfterSceneInit, InputProcessor {
    private VisIDManager idManager;
    private CameraManager cameraManager;
    private ComponentMapper<UIElement> buttonCmp;
    private ComponentMapper<VisID> idCmp;
    private RenderBatchingSystem renderBatchingSystem;

    private Entity player;
    //private AnimationStates.EntityState prevState;

    Entity leftArrow;
    Entity rightArrow;
    Entity jumpArrow;
    Entity attackArrow;
    Entity rightAtkArrow;
    Entity slidingArrow;

    private final int NONE = -1;
    private int pointerLeft = NONE;
    private int pointerRight = NONE;
    private int pointerJump = NONE;
    private int pointerLeftAtk = NONE;
    private int pointerRightAtk = NONE;
    private int pointerSlide = NONE;

    private AnimationStates states;
    public UserInputSystem() {
        super(Aspect.all(UIElement.class));

    }

    @Override
    protected void process(int e) {
        System.out.println();
    }

    @Override
    public void afterSceneInit() {
        Gdx.input.setInputProcessor(this);
        player = idManager.get("player");
        leftArrow = idManager.get("leftArrow");
        rightArrow = idManager.get("rightArrow");
        attackArrow = idManager.get("attackArrow");
        slidingArrow = idManager.get("slidingArrow");
        jumpArrow = idManager.get("jumpArrow");
        states = player.getComponent(AnimationStates.class);

    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = GetInWorldCoordinates(screenX, screenY);
        System.out.println(screenX + " " + screenY);
        if (Utils.IsPolygonContainsPoint(leftArrow, new Vector2(touchPos.x, touchPos.y))&& pointerRight == NONE) {
            pointerLeft = pointer;
            states.getState("Run").isPlaying = true;
            states.getState("Run").isFlip = true;
            player.getComponent(PhysicStates.class).isMovingLeft = true;
        }
        else if (Utils.IsPolygonContainsPoint(rightArrow, new Vector2(touchPos.x, touchPos.y)) && pointerLeft == NONE) {
            pointerRight = pointer;
            states.getState("Run").isPlaying = true;
            states.getState("Run").isFlip = false;
            player.getComponent(PhysicStates.class).isMovingRight = true;
        }
        if (Utils.IsPolygonContainsPoint(jumpArrow, new Vector2(touchPos.x, touchPos.y))) {
            pointerJump = pointer;
            states.getState("Jump").isPlaying = true;
            player.getComponent(PhysicStates.class).isJumping = true;
        }
        if (Utils.IsPolygonContainsPoint(slidingArrow, new Vector2(touchPos.x, touchPos.y))) {
            pointerSlide = pointer;
            states.getState("Slide").isPlaying = true;
            if (states.getState("Slide").isFlip) {
                player.getComponent(PhysicStates.class).isSlidingLeft = true;
            } else {
                player.getComponent(PhysicStates.class).isSlidingRight = true;
            }

        }
        if (Utils.IsPolygonContainsPoint(attackArrow, new Vector2(touchPos.x, touchPos.y))) {
            pointerLeftAtk = pointer;
            states.getState("Attack").isPlaying = true;
        }
        return true;
    }



    private Vector3 GetInWorldCoordinates(int screenX, int screenY) {
        Vector3 unproject = new Vector3(screenX, screenY, 0);
        return cameraManager.getCamera().unproject(unproject);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == pointerLeft) {
            states.getState("Run").isPlaying = false;
            player.getComponent(PhysicStates.class).isMovingLeft = false;
            pointerLeft = NONE;
        } else if (pointer == pointerRight) {
            states.getState("Run").isPlaying = false;
            player.getComponent(PhysicStates.class).isMovingRight = false;
            pointerRight = NONE;
        }
        if (pointer == pointerJump) {
            player.getComponent(PhysicStates.class).isJumping = false;
            pointerJump = NONE;
        }
        if (pointer == pointerSlide) {
            states.getState("Slide").isPlaying = false;
            player.getComponent(PhysicStates.class).isSlidingLeft = false;
            player.getComponent(PhysicStates.class).isSlidingRight = false;
            pointerSlide = NONE;
        }
        if (pointer == pointerLeftAtk) {
            states.getState("Attack").isPlaying = false;
            pointerLeftAtk = NONE;
        } else if (pointer == pointerRightAtk) {
            states.getState("Attack").isPlaying = false;
            pointerRightAtk = NONE;
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
