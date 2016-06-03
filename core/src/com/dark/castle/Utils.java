package com.dark.castle;

import com.artemis.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;

/**
 * Created by DzzirtNik on 02.06.2016.
 */
public class Utils {
    public static Rectangle GetBounds(Entity entity) {
        VisPolygon polygon = entity.getComponent(VisPolygon.class);
        Transform transform = entity.getComponent(Transform.class);
        float x = transform.getX();
        float y = transform.getY();
        float width = polygon.vertices.get(1).x - polygon.vertices.get(0).x;
        float height = polygon.vertices.get(3).y - polygon.vertices.get(0).y;
        return new Rectangle(x, y, width, height);
    }
}
