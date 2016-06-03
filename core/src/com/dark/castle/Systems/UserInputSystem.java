package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
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
    private ComponentMapper<Button> buttonCmp;
    private ComponentMapper<VisID> idCmp;
    private RenderBatchingSystem renderBatchingSystem;

    private Entity player;
    //private AnimationStates.EntityState prevState;

    Entity leftArrow;
    Entity rightArrow;
    Entity jumpArrow;
    Entity leftAtkArrow;
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
        super(Aspect.all(Button.class));

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
        leftAtkArrow = idManager.get("leftAtkArrow");
        rightAtkArrow = idManager.get("rightAtkArrow");
        slidingArrow = idManager.get("slidingArrow");
        jumpArrow = idManager.get("jumpArrow");
        states = player.getComponent(AnimationStates.class);

    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = GetInWorldCoordinates(screenX, screenY);
        if (Utils.GetBounds(leftArrow).contains(touchPos.x, touchPos.y)&& pointerRight == NONE) {
            pointerLeft = pointer;
            states.getState("Run").isPlaying = true;
            states.getState("Run").isFlip = true;
            player.getComponent(PhysicStates.class).isMovingLeft = true;
        }
        else if (Utils.GetBounds(rightArrow).contains(touchPos.x, touchPos.y) && pointerLeft == NONE) {
            pointerRight = pointer;
            states.getState("Run").isPlaying = true;
            states.getState("Run").isFlip = false;
            player.getComponent(PhysicStates.class).isMovingRight = true;
        }
        else if (Utils.GetBounds(jumpArrow).contains(touchPos.x, touchPos.y)) {
            pointerJump = pointer;
            states.getState("Jump").isPlaying = true;
            player.getComponent(PhysicStates.class).isJumping = true;
        }
        else if (Utils.GetBounds(slidingArrow).contains(touchPos.x, touchPos.y)) {
            pointerSlide = pointer;
            states.getState("Hurt").isPlaying = true;
            //player.getComponent(PhysicStates.class).isSliding = true;
        }
        else if (Utils.GetBounds(leftAtkArrow).contains(touchPos.x, touchPos.y)) {
            pointerLeftAtk = pointer;
            states.getState("Attack").isPlaying = true;
            states.getState("Attack").isFlip = true;
        }
        else if (Utils.GetBounds(rightAtkArrow).contains(touchPos.x, touchPos.y)) {
            pointerRightAtk = pointer;
            states.getState("Attack").isPlaying = true;
            states.getState("Attack").isFlip = false;
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
        } else if (pointer == pointerJump) {
            player.getComponent(PhysicStates.class).isJumping = false;
            pointerJump = NONE;
        } else if (pointer == pointerSlide) {
            states.getState("Slide").isPlaying = false;
            player.getComponent(PhysicStates.class).isSliding = false;
            pointerSlide = NONE;
        } else if (pointer == pointerLeftAtk) {
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
