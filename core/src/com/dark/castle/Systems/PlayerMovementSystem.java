package com.dark.castle.Systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dark.castle.Components.Button;
import com.dark.castle.Components.Velocity;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 01.05.2016.
 */
public class PlayerMovementSystem extends BaseSystem implements AfterSceneInit {

    private VisIDManager idManager;
    private CameraManager cameraManager;

/*    private Button leftArrow;
    private Button rightArrow;
    private Button jumpArrow;
    private Button atkArrow;*/


    private boolean canJump;
    private float jumpTimeout = 0;

    public static boolean isInAir;

    public PlayerMovementSystem() {

    }

    @Override
    public void afterSceneInit() {
/*        leftArrow = idManager.get("leftArrow").getComponent(Button.class);
        rightArrow = idManager.get("rightArrow").getComponent(Button.class);
        jumpArrow = idManager.get("jumpArrow").getComponent(Button.class);
        atkArrow = idManager.get("atkArrow").getComponent(Button.class);*/
        AllowPlayerToJumpAndSlideOverWalls();
    }

    private void AllowPlayerToJumpAndSlideOverWalls() {
        Body playerBody = idManager.get("player").getComponent(PhysicsBody.class).body;
        playerBody.setUserData("player");
        Vector2 playerSize = new Vector2(playerBody.getWorldCenter().sub(playerBody.getPosition()));
        Rectangle bottomSensorRect = new Rectangle(playerSize.x, 0, playerSize.x * 0.9f, playerSize.y * 0.2f);
        Rectangle sideSensorRect = new Rectangle(playerSize.x, playerSize.y, playerSize.x * 1.6f, playerSize.y * 0.95f);
        CreatePlayerSensor(bottomSensorRect, "bottomSensor");
        CreatePlayerSensor(sideSensorRect, "sideSensor");

        world.getSystem(PhysicsSystem.class).getPhysicsWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture first = contact.getFixtureA();
                Fixture second = contact.getFixtureB();
                if (first.getUserData() != null && first.getUserData() == "bottomSensor") {
                    canJump = true;
                    isInAir = false;
                    AnimationSystem.animPriority.get(2).isPlaying = false;
                } else if (second.getUserData() != null && second.getUserData() == "bottomSensor") {
                    canJump = true;
                    isInAir = false;
                    AnimationSystem.animPriority.get(2).isPlaying = false;
                } else if (first.getUserData() != null && first.getUserData() == "sideSensor") {
                    second.setUserData("markedToZeroFriction");
                } else if (second.getUserData() != null && second.getUserData() == "sideSensor") {
                    first.setUserData("markedToZeroFriction");
                }

            }

            @Override
            public void endContact(Contact contact) {
                Fixture first = contact.getFixtureA();
                Fixture second = contact.getFixtureB();
                if (first.getUserData() != null && first.getUserData() == "bottomSensor") {
                    canJump = false;
                    isInAir = true;
                } else if (second.getUserData() != null && second.getUserData() == "bottomSensor") {
                    canJump = false;
                    isInAir = true;
                } else if (first.getUserData() != null && first.getUserData() == "sideSensor") {
                    second.setUserData(null);
                } else if (second.getUserData() != null && second.getUserData() == "sideSensor") {
                    first.setUserData(null);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Fixture first = contact.getFixtureA();
                Fixture second = contact.getFixtureB();
                if (first.getUserData() != null && first.getUserData() == "markedToZeroFriction") {
                    contact.setFriction(0.f);
                } else if (second.getUserData() != null && second.getUserData() == "markedToZeroFriction") {
                    contact.setFriction(0.f);
                } else {
                    contact.setFriction(1.f);
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private void CreatePlayerSensor(Rectangle bounds, Object userData) {
        Body playerBody = idManager.get("player").getComponent(PhysicsBody.class).body;
        FixtureDef groundSensor = new FixtureDef();
        groundSensor.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.width, bounds.height, new Vector2(bounds.x, bounds.y), 0);
        groundSensor.shape = shape;
        Fixture fixture = playerBody.createFixture(groundSensor);
        fixture.setUserData(userData);
    }

    @Override
    protected void processSystem() {
        Body playerBody = idManager.get("player").getComponent(PhysicsBody.class).body;
        Velocity playerVel = idManager.get("player").getComponent(Velocity.class);
        ProcessMove(playerBody, playerVel);
        ProcessJump(playerBody, playerVel);

    }

    private void ProcessMove(Body playerBody, Velocity playerVel) {
       /* if (leftArrow.state == Button.State.PRESSED) {
            playerBody.setLinearVelocity(-playerVel.x, playerBody.getLinearVelocity().y);
        }
        if (rightArrow.state == Button.State.PRESSED) {
            playerBody.setLinearVelocity(playerVel.x, playerBody.getLinearVelocity().y);
        }*/
    }

    private void ProcessJump(Body playerBody, Velocity playerVel) {
        /*if (jumpArrow.state == Button.State.PRESSED && canJump && jumpTimeout <= 0) {
            playerBody.applyLinearImpulse(new Vector2(0,
                            playerBody.getMass() * playerVel.y),
                    playerBody.getWorldCenter(),
                    true);
            jumpTimeout = 15;
        }
        if (jumpTimeout > 0) {
            jumpTimeout--;
        }*/
    }


}
