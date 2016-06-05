package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.dark.castle.Components.PhysicStates;
import com.dark.castle.Components.AnimationStates;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.spriter.Animation;
import com.kotcrab.vis.runtime.spriter.Mainline;
import com.kotcrab.vis.runtime.spriter.Player;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by DzzirtNik on 29.05.2016.
 */
public class AnimationSystem extends IteratingSystem {
    private VisIDManager idManager;
    private HitProcessingSystem hitProcessingSystem;
    private ComponentMapper<AnimationStates> statesCmp;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformrCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<Origin> originCmp;
    private ComponentMapper<PhysicStates> physicStatesCmp;
    private ComponentMapper<PhysicsBody> physicBodyCmp;

    public AnimationSystem() {
        super(Aspect.all(VisSpriter.class, Transform.class, AnimationStates.class));
    }


    @Override
    protected void inserted(final int entityId) {
        final VisSpriter visSpriter = spriterCmp.get(entityId);
        final AnimationStates states = statesCmp.get(entityId);
        visSpriter.getPlayer().addListener(new Player.PlayerListener() {
            @Override
            public void animationFinished(Animation animation) {
                if (animation.name.equals("Death")) {
                    visSpriter.setAnimationPlaying(false);
                    statesCmp.remove(entityId);
                }
                if (animation.name.equals("Attack")) {
                    hitProcessingSystem.check = entityId;
                }
                for (AnimationStates.StateData data : states.priorityList) {
                    if (animation.name.equals(data.name) && (data.name.equals("Slide") || data.name.equals("Hurt"))) {
                        data.isPlaying = false;
                        break;
                    }
                }
            }

            @Override
            public void animationChanged(Animation oldAnim, Animation newAnim) {
                if (states.getState(oldAnim.name).isFlip != states.getState(newAnim.name).isFlip) {
                    for (AnimationStates.StateData data : states.priorityList) {
                        if (!data.name.equals(newAnim.name)) {
                            data.isFlip = !data.isFlip;
                        }
                    }
                }
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

    @Override
    protected void process(final int entityId) {
        final VisSpriter visSpriter = spriterCmp.get(entityId);
        PhysicsBody physicsBody = physicBodyCmp.get(entityId);
        final AnimationStates states = statesCmp.get(entityId);
        if (physicsBody != null) {
            PlayerMovementSystem.JumpData jumpData = (PlayerMovementSystem.JumpData) physicsBody.body.getUserData();
            if (jumpData.canJump) {
                states.getState("Jump").isPlaying = false;
            } else {
                states.getState("Jump").isPlaying = true;

            }
        }
        for (AnimationStates.StateData data : states.priorityList) {
            if (data.isPlaying) {
                visSpriter.getPlayer().setAnimation(data.name);
                visSpriter.setFlip(data.isFlip, false);
                break;
            }
        }
    }


}
