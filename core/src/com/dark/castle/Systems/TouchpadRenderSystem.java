package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.dark.castle.Components.VisTouchpad;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.delegate.DeferredEntityProcessingSystem;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessPrincipal;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;

/**
 * Created by DzzirtNik on 31.05.2016.
 */
public class TouchpadRenderSystem extends DeferredEntityProcessingSystem {

    private ComponentMapper<VisTouchpad> toucpadCm;
    private ComponentMapper<Transform> transformCm;

    private RenderBatchingSystem renderBatchingSystem;
    public TouchpadRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.all(VisTouchpad.class, Transform.class), principal);
    }

    @Override
    protected void process(int e) {
        VisTouchpad touchpad = toucpadCm.get(e);
        Transform transform = transformCm.get(e);
        touchpad.touchpad.setPosition(transform.getX(), transform.getY());
        float x = touchpad.touchpad.getX();
        float y = touchpad.touchpad.getY();
        float knobX = touchpad.touchpad.getKnobX();
        float knobY = touchpad.touchpad.getKnobY();

        //renderBatchingSystem.getBatch().draw(new Texture("gfx/moveBg.png"), touchpad.touchpad.getX(), touchpad.touchpad.getY());
        //renderBatchingSystem.getBatch().draw(new Texture("gfx/moveKnob.png"), touchpad.touchpad.getKnobX(), knobY);
        touchpad.touchpad.draw(renderBatchingSystem.getBatch(), 100);
    }
}
