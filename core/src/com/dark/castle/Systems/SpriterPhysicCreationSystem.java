package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.component.PhysicsSprite;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;

/**
 * Created by DzzirtNik on 02.06.2016.
 */
public class SpriterPhysicCreationSystem extends EntitySystem {
    private PhysicsSystem physicsSystem;

    private ComponentMapper<PhysicsProperties> physicsPropCm;
    private ComponentMapper<PhysicsBody> physicsCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformCmp;
    private ComponentMapper<Origin> originCmp;

    private World world;

    public SpriterPhysicCreationSystem () {
        super(Aspect.all(PhysicsProperties.class, VisPolygon.class, VisSpriter.class));
    }

    @Override
    protected void processSystem () {

    }

    @Override
    protected void initialize () {
        world = physicsSystem.getPhysicsWorld();
    }

    @Override
    public void inserted (Entity entity) {
        PhysicsProperties physicsProperties = physicsPropCm.get(entity);
        VisPolygon polygon = polygonCmp.get(entity);
        Transform transform = transformCmp.get(entity);

        //if (physicsProperties.adjustOrigin) originCmp.get(entity).setOrigin(0, 0);

        Vector2 worldPos = new Vector2(transform.getX(), transform.getY());

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(worldPos);

        Body body = world.createBody(bodyDef);
        body.setType(physicsProperties.bodyType);
        body.setUserData(entity);

        body.setGravityScale(physicsProperties.gravityScale);
        body.setLinearDamping(physicsProperties.linearDamping);
        body.setAngularDamping(physicsProperties.angularDamping);

        body.setBullet(physicsProperties.bullet);
        body.setFixedRotation(physicsProperties.fixedRotation);
        body.setSleepingAllowed(physicsProperties.sleepingAllowed);
        body.setActive(physicsProperties.active);

        for (Vector2[] vs : polygon.faces) {
            for (Vector2 v : vs) { //polygon component stores data in world cords, we need to convert it to local cords
                v.sub(worldPos);
            }

            PolygonShape shape = new PolygonShape();
            shape.set(vs);

            FixtureDef fd = new FixtureDef();
            fd.density = physicsProperties.density;
            fd.friction = physicsProperties.friction;
            fd.restitution = physicsProperties.restitution;
            fd.isSensor = physicsProperties.sensor;
            fd.shape = shape;
            fd.filter.maskBits = physicsProperties.maskBits;
            fd.filter.categoryBits = physicsProperties.categoryBits;

            body.createFixture(fd);
            shape.dispose();
        }

        entity.edit()
                .add(new PhysicsBody(body))
                .add(new PhysicsSprite(transform.getRotation()));
    }
}
