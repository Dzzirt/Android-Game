package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.State;
import com.dark.castle.Components.VisTouchpad;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.delegate.DeferredEntityProcessingSystem;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessPrincipal;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 30.04.2016.
 */
public class UserInputSystem extends IteratingSystem implements AfterSceneInit, InputProcessor {
    private VisIDManager idManager;
    private CameraManager cameraManager;
    private ComponentMapper<Button> buttonCmp;
    private ComponentMapper<VisTouchpad> touchpadCmp;
    private ComponentMapper<VisID> idCmp;
    private RenderBatchingSystem renderBatchingSystem;

    private Entity player;
    private State.EntityState prevState;

    private final int NONE = -1;
    private int pointerLeft = NONE;
    private int pointerRight = NONE;
    private int pointerJump = NONE;
    private int pointerAtk = NONE;

    private InputMultiplexer multiplexer;
    public UserInputSystem() {
        super(Aspect.all(VisTouchpad.class));

    }

    @Override
    public void inserted(int e) {
        multiplexer.clear();
        Touchpad touchpad = touchpadCmp.get(e).touchpad;
        //multiplexer.addProcessor(touchpad);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    protected void process(int e) {

    }

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        player.edit().add(new State());
    }

    @Override
    protected void initialize() {
        multiplexer = new InputMultiplexer();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = GetInWorldCoordinates(screenX, screenY);
        /*if (GetBounds(leftArrow).contains(touchPos.x, touchPos.y)) {
            pointerLeft = pointer;
            player.getComponent(State.class).state = State.EntityState.LeftMove;
            leftArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(rightArrow).contains(touchPos.x, touchPos.y)) {
            pointerRight = pointer;
            player.getComponent(State.class).state = State.EntityState.RightMove;
            rightArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(jumpArrow).contains(touchPos.x, touchPos.y)) {
            player.getComponent(State.class).state = State.EntityState.Jump;
            pointerJump = pointer;
            jumpArrow.getComponent(Button.class).state = Button.State.PRESSED;
        } else if (GetBounds(atkArrow).contains(touchPos.x, touchPos.y)) {
            pointerAtk = pointer;
            player.getComponent(State.class).state = State.EntityState.Attack;
            atkArrow.getComponent(Button.class).state = Button.State.PRESSED;
        }*/
        return true;
    }

    private Rectangle GetBounds(Entity entity) {
        VisPolygon polygon = entity.getComponent(VisPolygon.class);
        Transform transform = entity.getComponent(Transform.class);
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
        /*if (pointer == pointerLeft) {
            AnimationSystem.animPriority.get(3).isPlaying = false;
            player.getComponent(State.class).state = State.EntityState.Stop;
            leftArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerLeft = NONE;
        } else if (pointer == pointerRight) {
            AnimationSystem.animPriority.get(3).isPlaying = false;
            player.getComponent(State.class).state = State.EntityState.Stop;
            rightArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerRight = NONE;
        } else if (pointer == pointerJump) {
            jumpArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerJump = NONE;
        } else if (pointer == pointerAtk) {
            player.getComponent(State.class).state = State.EntityState.Stop;
            atkArrow.getComponent(Button.class).state = Button.State.NORMAL;
            pointerAtk = NONE;
        }*/
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
