package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.Velocity;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 01.05.2016.
 */
public class PlayerMovementSystem extends IteratingSystem implements AfterSceneInit {

    public class JumpData
    {
        public boolean canJump = true;
        public float jumpTimeout = 0;
    }

    private VisIDManager idManager;
    private CameraManager cameraManager;
    private ComponentMapper<PhysicStates> physicStatesCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<AnimationStates> animStatesCmp;
    private ComponentMapper<PhysicsBody> physicBodyCmp;
    private ComponentMapper<Velocity> velocityCmp;

    public PlayerMovementSystem() {
        super(Aspect.all(PhysicStates.class));
    }

    @Override
    public void afterSceneInit() {
        world.getSystem(PhysicsSystem.class).getPhysicsWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture first = contact.getFixtureA();
                Fixture second = contact.getFixtureB();
                if (first.getUserData() != null && first.getUserData() == "bottomSensor") {
                    JumpData userData = (JumpData) first.getBody().getUserData();
                    userData.canJump = true;
                } else if (second.getUserData() != null && second.getUserData() == "bottomSensor") {
                    JumpData userData = (JumpData) second.getBody().getUserData();
                    userData.canJump = true;
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
                    JumpData userData = (JumpData) first.getBody().getUserData();
                    userData.canJump = false;
                } else if (second.getUserData() != null && second.getUserData() == "bottomSensor") {
                    JumpData userData = (JumpData) second.getBody().getUserData();
                    userData.canJump = false;
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

    @Override
    protected void process(int entityId) {
        Body body = physicBodyCmp.get(entityId).body;
        Velocity vel = velocityCmp.get(entityId);
        PhysicStates states = physicStatesCmp.get(entityId);
        ProcessMove(body, vel, states);
        ProcessJump(body, vel, states);
    }

    @Override
    protected void inserted(int entityId) {
        PhysicsBody body = physicBodyCmp.get(entityId);
        body.body.setUserData(new JumpData());
        AllowJumpAndSlideOverWalls(world.getEntity(entityId));
    }

    private void AllowJumpAndSlideOverWalls(Entity e) {
        Rectangle bounds = Utils.GetBounds(e);
        float halfWidth = bounds.width / 2.f;
        float halfHeight = bounds.height / 2.f;
        Rectangle bottomSensorRect = new Rectangle(halfWidth, 0, halfWidth * 0.9f, halfHeight * 0.2f);
        Rectangle sideSensorRect = new Rectangle(halfWidth, halfHeight, halfWidth * 1.6f, halfHeight * 0.95f);
        CreateSensor(e, bottomSensorRect, "bottomSensor");
        CreateSensor(e, sideSensorRect, "sideSensor");
        String id = e.getComponent(VisID.class).id;

        if (id.equals("player") || id.equals("enemy")) {
            for (Fixture f : e.getComponent(PhysicsBody.class).body.getFixtureList()) {
                f.setFilterData(new Filter(){{groupIndex = -1;}});
            }
        }
    }

    private void CreateSensor(Entity e, Rectangle bounds, Object userData) {
        Body body = e.getComponent(PhysicsBody.class).body;
        FixtureDef groundSensor = new FixtureDef();
        groundSensor.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.width, bounds.height, new Vector2(bounds.x, bounds.y), 0);
        groundSensor.shape = shape;
        Fixture fixture = body.createFixture(groundSensor);
        fixture.setUserData(userData);
    }




    private void ProcessMove(Body body, Velocity vel, PhysicStates states) {
        if (states.isSliding) {
            body.setLinearVelocity(vel.x * 2.5f, body.getLinearVelocity().y);
            states.isSliding = false;
        }
        else if (states.isMovingLeft) {
            body.setLinearVelocity(-vel.x, body.getLinearVelocity().y);
        }
        else if (states.isMovingRight) {
            body.setLinearVelocity(vel.x, body.getLinearVelocity().y);
        }
    }

    private void ProcessJump(Body body, Velocity vel, PhysicStates states) {
        JumpData jumpData = (JumpData) body.getUserData();
        if (states.isJumping && jumpData.canJump   && jumpData.jumpTimeout <= 0) {
            body.applyLinearImpulse(new Vector2(0,
                            body.getMass() * vel.y),
                    body.getWorldCenter(),
                    true);
            jumpData.jumpTimeout = 15;
        }
        if (jumpData.jumpTimeout > 0) {
            jumpData.jumpTimeout--;
        }
    }


}
