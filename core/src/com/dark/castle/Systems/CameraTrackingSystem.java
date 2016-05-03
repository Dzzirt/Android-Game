package com.dark.castle.Systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;

/**
 * Created by DzzirtNik on 30.04.2016.
 */
public class CameraTrackingSystem extends BaseSystem {
    private CameraManager cameraManager;
    private ComponentMapper<Transform> mTransform;
    private ComponentMapper<VisPolygon> polygon;
    private OrthographicCamera camera;
    private VisIDManager idManager;


    public CameraTrackingSystem() {
    }

    @Override
    protected void processSystem() {
        camera = cameraManager.getCamera();
        SetCameraPositionRelativeToPlayer(idManager.get("player"));
        MoveCameraInsideScene(camera);
    }

    public void SetCameraPositionRelativeToPlayer(Entity entity) {
        Transform playerTransform = mTransform.get(entity);
        VisPolygon playerPolygon = polygon.get(entity);
        float width = playerPolygon.vertices.get(1).x - playerPolygon.vertices.get(0).x;
        float height = playerPolygon.vertices.get(3).y - playerPolygon.vertices.get(0).y;
        camera.position.x = playerTransform.getX() + width / 2.f;
        camera.position.y = playerTransform.getY() + height;
    }
    public void MoveCameraInsideScene(OrthographicCamera camera) {
        if (camera.position.x - camera.viewportWidth / 2.f < 0) {
            camera.position.x = camera.viewportWidth / 2.f;
        }
        if (camera.position.x + camera.viewportWidth / 2.f > 16) {
            camera.position.x = 16f - camera.viewportWidth / 2.f;
        }
        if (camera.position.y + camera.viewportHeight / 2.f > 5) {
            camera.position.y = 5 - camera.viewportWidth / 2.f;
        }
    }
}
