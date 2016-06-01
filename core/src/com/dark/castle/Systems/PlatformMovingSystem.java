package com.dark.castle.Systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.dark.castle.Components.MovingPlatform;
import com.dark.castle.Components.Velocity;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.Random;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class PlatformMovingSystem extends BaseSystem implements AfterSceneInit {
    private VisIDManager idManager;
    private ComponentMapper<VisID> visIDCmp;
    private ComponentMapper<PhysicsBody> bodyCmp;
    private ComponentMapper<Variables> varCmp;
    private ComponentMapper<Velocity> velCmp;
    private ComponentMapper<MovingPlatform> movingPlatformCmp;


    Timer.Task CreatePlatformTask(final Entity e, final Velocity vel)
    {
        return new Timer.Task() {
            private Velocity localVel = vel;
            private Body body = e.getComponent(PhysicsBody.class).body;
            @Override
            public void run() {
                body.setLinearVelocity(localVel.x, localVel.y);
                localVel.x = -localVel.x;
                localVel.y = -localVel.y;
            }
        };
    }
    public Array<Entity> concat(Array<Entity> first, Array<Entity> second) {
        int aLen = first.size;
        int bLen = second.size;
        Array<Entity> result= new Array<Entity>(aLen+bLen);
        result.addAll(first);
        result.addAll(second);
        return result;
    }
    public void afterSceneInit() {
        Array<Entity> platforms = concat(idManager.getMultiple("hPlatform"),
                idManager.getMultiple("vPlatform"));
        Random random = new Random();
        for (Entity e : platforms)
        {
            Velocity vel = velCmp.get(e);
            MovingPlatform mp = movingPlatformCmp.get(e);
            Timer.Task task = CreatePlatformTask(e, vel);

            Timer.instance().scheduleTask(task, random.nextFloat(),
                    mp.distance / (vel.y > 0 ? vel.y : vel.x));
        }
    }

    @Override
    protected void processSystem() {
    }
}
