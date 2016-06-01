package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.AnimationStates;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.spriter.Animation;
import com.kotcrab.vis.runtime.spriter.Mainline;
import com.kotcrab.vis.runtime.spriter.Player;
import com.kotcrab.vis.runtime.system.VisIDManager;

/**
 * Created by DzzirtNik on 29.05.2016.
 */
public class AnimationSystem extends IteratingSystem {
    private VisIDManager idManager;
    private ComponentMapper<AnimationStates> statesCmp;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformrCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<Origin> originCmp;
    private ComponentMapper<PhysicStates> physicStatesCmp;

    public AnimationSystem() {
        super(Aspect.all(VisSpriter.class, Transform.class, AnimationStates.class));
    }


    @Override
    protected void process(int entityId) {
        final VisSpriter visSpriter = spriterCmp.get(entityId);
        PhysicStates physicStates = physicStatesCmp.get(entityId);
        Transform transform = transformrCmp.get(entityId);
        VisPolygon polygon = polygonCmp.get(entityId);
        final AnimationStates states = statesCmp.get(entityId);
        visSpriter.updateValues(transform.getX() + (polygon.vertices.get(1).x - polygon.vertices.get(0).x) / 2.f,
                transform.getY() + (polygon.vertices.get(3).y - polygon.vertices.get(1).y) / 2.f, 0);


        for (AnimationStates.StateData data : states.priorityList) {
            if (data.isPlaying) {
                visSpriter.getPlayer().setAnimation(data.name);
                visSpriter.setFlip(data.isFlip, false);
                break;
            }
        }


        visSpriter.getPlayer().addListener(new Player.PlayerListener() {
            @Override
            public void animationFinished(Animation animation) {
                for (AnimationStates.StateData data : states.priorityList) {
                    if (animation.name.equals(data.name) && data.name != "Stop" && data.name != "Attack" && data.name != "Run" && data.name != "Jump") {
                        data.isPlaying = false;
                        break;
                    }
                }
            }

            @Override
            public void animationChanged(Animation oldAnim, Animation newAnim) {

            }

            @Override
            public void preProcess(Player player) {

            }

            @Override
            public void postProcess(Player player) {

            }

            @Override
            public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {

            }
        });
    }
}
