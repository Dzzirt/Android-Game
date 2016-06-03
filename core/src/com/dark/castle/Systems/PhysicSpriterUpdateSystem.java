package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.system.VisIDManager;

/**
 * Created by DzzirtNik on 02.06.2016.
 */
public class PhysicSpriterUpdateSystem extends EntityProcessingSystem {
    private VisIDManager idManager;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformCmp;
    private ComponentMapper<PhysicsBody> physicBodyCmp;

    public PhysicSpriterUpdateSystem() {
        super(Aspect.all(VisSpriter.class, Transform.class, PhysicsBody.class));
    }

    @Override
    protected void process(Entity e) {
        Transform transform = e.getComponent(Transform.class);
        PhysicsBody body = e.getComponent(PhysicsBody.class);
        VisSpriter spriter = e.getComponent(VisSpriter.class);
        Rectangle bounds = Utils.GetBounds(e);
        float offsetX = 0;
        if (spriter.isFlipX()) {
           offsetX = bounds.width;
        }
        transform.setPosition(body.body.getPosition().x + offsetX,
                body.body.getPosition().y);
        transform.setRotation(spriter.getPlayer().getAngle() + body.body.getAngle() * MathUtils.radiansToDegrees);
    }
}
