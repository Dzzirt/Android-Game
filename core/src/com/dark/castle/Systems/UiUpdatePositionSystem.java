package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.AspectSubscriptionManager;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.dark.castle.Components.Button;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DzzirtNik on 01.05.2016.
 */
public class UiUpdatePositionSystem extends IteratingSystem {

    private ComponentMapper<Transform> transformCmp;
    private ComponentMapper<Button> buttonCmp;
    private CameraManager cameraManager;
    private HashMap<Integer, Vector2> distanceFromCamCenter;

    public UiUpdatePositionSystem() {
        super(Aspect.all(Button.class));
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

}
