package com.dark.castle;

import com.artemis.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dark.castle.Components.UIElement;
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

    public static Rectangle GetBounds(VisPolygon polygon) {
        float x = polygon.vertices.get(0).x;
        float y = polygon.vertices.get(0).y;
        float width = polygon.vertices.get(1).x - polygon.vertices.get(0).x;
        float height = polygon.vertices.get(3).y - polygon.vertices.get(0).y;
        return new Rectangle(x, y, width, height);
    }

    private enum EdgeState
    {
        TOUCHING, CROSSING, INESSENTIAL
    }

    public static boolean IsPolygonContainsPoint(Entity e, Vector2 point) {
        Vector2 originPos = e.getComponent(UIElement.class).originPos;
        Transform currentPos = e.getComponent(Transform.class);
        point.x -= currentPos.getX() - originPos.x;
        point.y -= currentPos.getY() - originPos.y;
        VisPolygon polygon = e.getComponent(VisPolygon.class);
        Array<Vector2> vertices = polygon.vertices;
        int parity = 0;
        for (int i = 0; i < vertices.size; i++) {
            Vector2[] vertex = new Vector2[2];
            if (i == 0) {
                vertex[1] = (new Vector2(vertices.get(i).x, vertices.get(i).y));
                vertex[0] = (new Vector2(vertices.get(vertices.size - 1).x, vertices.get(vertices.size - 1).y));
            } else {
                vertex[1] = (new Vector2(vertices.get(i).x, vertices.get(i).y)) ;
                vertex[0] = (new Vector2(vertices.get(i - 1).x, vertices.get(i - 1).y));
            }
            switch (edgeType(point, vertex)) {
                case TOUCHING:
                    return true;
                case CROSSING:
                    parity = 1 - parity;
            }
        }
        return parity == 1;
    }

    private static EdgeState edgeType(Vector2 point, Vector2[] edge)
    {
        Vector2 p1 = edge[0];
        Vector2 p2 = edge[1];
        float result = (point.x - p1.x) * (p2.y - p1.y) - (point.y - p1.y) * (p2.x - p1.x);
        if (result < 0) {
            return ((p1.y<point.y)&&(point.y<=p2.y)) ? EdgeState.CROSSING : EdgeState.INESSENTIAL;
        }
        else if (result > 0) {
            return ((p2.y<point.y)&&(point.y<=p1.y)) ? EdgeState.CROSSING : EdgeState.INESSENTIAL;
        }
        return EdgeState.TOUCHING;
    }
}
