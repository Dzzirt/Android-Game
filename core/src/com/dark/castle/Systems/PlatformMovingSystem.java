package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.dark.castle.Components.Button;
import com.kotcrab.vis.runtime.component.VisID;

/**
 * Created by DzzirtNik on 03.05.2016.
 */
public class PlatformMovingSystem extends IteratingSystem {

    public PlatformMovingSystem() {
        super(Aspect.all(VisID.class).exclude(Button.class));
    }
    @Override
    protected void process(int entityId) {

    }
}
