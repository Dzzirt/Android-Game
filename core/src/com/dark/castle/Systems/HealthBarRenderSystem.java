package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dark.castle.Components.HealthBar;
import com.dark.castle.Utils;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.delegate.DeferredEntityProcessingSystem;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessPrincipal;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;

/**
 * Created by nikita on 05.06.16.
 */
public class HealthBarRenderSystem extends DeferredEntityProcessingSystem {
    private ComponentMapper<HealthBar> hpBarCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<Transform> transformCmp;
    private ComponentMapper<VisID> idCmp;
    private VisIDManager idManager;
    private RenderBatchingSystem renderBatchingSystem;

    public HealthBarRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(HealthBar.class), principal);
    }

    void UpdatePosition(int e)
    {
        HealthBar healthBar = hpBarCmp.get(e);
        VisPolygon polygon = polygonCmp.get(e);
        String id = idCmp.get(e).id;
        if (id.equals("player")) {
            Transform hpBarTransform = idManager.get("hp_bar").getComponent(Transform.class);
            healthBar.position.x = hpBarTransform.getX() + 0.0515f;
            healthBar.position.y = hpBarTransform.getY() + 0.1284f;
        } else if (id.equals("enemy")) {
            Rectangle rectangle = Utils.GetBounds(polygon);
            healthBar.position.x = rectangle.x + (rectangle.width - healthBar.region.getRegionWidth() * healthBar.scale.x) / 2.f;
            healthBar.position.y = rectangle.y + rectangle.height + 0.08f;
        }
    }
    @Override
    protected void process(int e) {
        UpdatePosition(e);
        HealthBar healthBar = hpBarCmp.get(e);
        TextureRegion region = healthBar.region;
        renderBatchingSystem.getBatch().draw(region,
                healthBar.position.x,
                healthBar.position.y,
                0,
                0,
                region.getRegionWidth(), region.getRegionHeight(),
                healthBar.scale.x,
                healthBar.scale.y,
                0);
    }
}
