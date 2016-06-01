package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.runtime.assets.SpriterAsset;
import com.kotcrab.vis.runtime.component.AssetReference;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.spriter.Drawer;
import com.kotcrab.vis.runtime.spriter.Timeline;
import com.kotcrab.vis.runtime.system.delegate.DeferredEntityProcessingSystem;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessPrincipal;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;

/**
 * Created by DzzirtNik on 31.05.2016.
 */
public class SpriterRenderSystem extends DeferredEntityProcessingSystem {
    private ComponentMapper<VisSpriter> spriterCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<AssetReference> assetCm;

    private RenderBatchingSystem renderBatchingSystem;

    private SpriterDrawer drawer;

    public SpriterRenderSystem (EntityProcessPrincipal principal) {
        super(Aspect.all(VisSpriter.class).exclude(Invisible.class), principal);
    }

    @Override
    protected void initialize () {
        Batch batch = renderBatchingSystem.getBatch();
        drawer = new SpriterDrawer((SpriteBatch) batch);
    }

    @Override
    protected void process (int entityId) {
        VisSpriter spriter = spriterCm.get(entityId);
        Transform transform = transformCm.get(entityId);
        SpriterAsset asset = (SpriterAsset) assetCm.get(entityId).asset;

        /*if (transform.isDirty()) {
            spriter.updateValues(transform.getX(), transform.getY(), transform.getRotation());
        }*/

        if (spriter.isAnimationPlaying() == false) spriter.getPlayer().setTime(0);
        spriter.getPlayer().update();

        drawer.setLoader(spriter.getLoader());
        drawer.setImageScale(asset.getImageScale());
        drawer.draw(spriter.getPlayer());
    }

    public class SpriterDrawer extends Drawer<Sprite> {
        SpriteBatch batch;
        float spriteScale;

        public SpriterDrawer (SpriteBatch batch) {
            super(null);
            this.batch = batch;
        }

        @Override
        public void setColor (float r, float g, float b, float a) {
        }

        @Override
        public void rectangle (float x, float y, float width, float height) {
        }

        @Override
        public void line (float x1, float y1, float x2, float y2) {
        }

        @Override
        public void circle (float x, float y, float radius) {
        }

        @Override
        public void draw (Timeline.Key.Object object) {
            Sprite sprite = loader.get(object.ref);
            float newPivotX = (sprite.getWidth() * object.pivot.x);
            float newX = object.position.x - newPivotX;
            float newPivotY = (sprite.getHeight() * object.pivot.y);
            float newY = object.position.y - newPivotY;

            sprite.setX(newX);
            sprite.setY(newY);

            sprite.setOrigin(newPivotX, newPivotY);
            sprite.setRotation(object.angle);

            sprite.setColor(1f, 1f, 1f, object.alpha);
            sprite.setScale(object.scale.x * spriteScale, object.scale.y * spriteScale);
            sprite.draw(batch);
        }

        public void setImageScale (float imageScale) {
            this.spriteScale = 1f / imageScale;
        }
    }

}