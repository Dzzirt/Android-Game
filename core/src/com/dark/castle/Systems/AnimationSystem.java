package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.dark.castle.Components.State;
import com.dark.castle.DarkCastle;
import com.kotcrab.vis.runtime.assets.SpriterAsset;
import com.kotcrab.vis.runtime.component.AssetReference;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.spriter.Animation;
import com.kotcrab.vis.runtime.spriter.Mainline;
import com.kotcrab.vis.runtime.spriter.Player;
import com.kotcrab.vis.runtime.spriter.Timeline;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.kotcrab.vis.runtime.util.SpriterData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DzzirtNik on 29.05.2016.
 */
public class AnimationSystem extends IteratingSystem implements AfterSceneInit {
    public static class AnimationState
    {
        public String name;
        public Boolean isPlaying;
        public Boolean isXFlip;
        public AnimationState(String name, Boolean isPlaying, Boolean isXFlip) {
            this.name = name;
            this.isPlaying = isPlaying;
            this.isXFlip = isXFlip;
        }
    }

    private VisIDManager idManager;
    private ComponentMapper<State> stateCmp;
    private ComponentMapper<VisSpriter> spriterCmp;
    private ComponentMapper<Transform> transformrCmp;
    private ComponentMapper<VisPolygon> polygonCmp;
    private ComponentMapper<Origin> originCmp;
    public static ArrayList<AnimationState> animPriority = new ArrayList<AnimationState>() {
        {
            add(new AnimationState("Death", false, false));
            add(new AnimationState("Attack", false, false));
            add(new AnimationState("Jump", false, false));
            add(new AnimationState("Run", false, false));
            add(new AnimationState("Stop", true, false));
        }
    };
    private State.EntityState playerState;


    public AnimationSystem() {
        super(Aspect.all(VisSpriter.class, Transform.class, State.class));
    }


    @Override
    protected void process(int entityId) {
        final VisSpriter visSpriter = spriterCmp.get(entityId);
        Transform transform = transformrCmp.get(entityId);
        VisPolygon polygon = polygonCmp.get(entityId);
        visSpriter.updateValues(transform.getX() + (polygon.vertices.get(1).x - polygon.vertices.get(0).x) / 2.f,
                transform.getY() + (polygon.vertices.get(3).y - polygon.vertices.get(1).y) / 2.f, 0);

        playerState = stateCmp.get(entityId).state;
        if (playerState == State.EntityState.Jump && !PlayerMovementSystem.isInAir) {
            playerState = State.EntityState.Stop;
            animPriority.get(2).isPlaying = false;
        }
        animPriority.get(playerState.getValue()).isPlaying = true;
        animPriority.get(playerState.getValue()).isXFlip = (playerState == State.EntityState.LeftMove);

        for (AnimationState animState : animPriority) {
            if (animState.isPlaying) {
                visSpriter.getPlayer().setAnimation(animState.name);
                visSpriter.setFlip(animState.isXFlip, false);
                break;
            }
        }


        visSpriter.getPlayer().addListener(new Player.PlayerListener() {
            @Override
            public void animationFinished(Animation animation) {
                for (AnimationState animState : animPriority) {
                    if (animation.name.equals(animState.name) && animation.name != "Stop") {
                        animState.isPlaying = false;
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

    @Override
    public void afterSceneInit() {
        SpriterData spriterData = DarkCastle.manager.get("spriter/Player/elisa.scml");
        VisSpriter visSpriter = new VisSpriter(spriterData.loader, spriterData.data, 0.01f);
        visSpriter.getPlayer().scale(0.01f);
        visSpriter.getPlayer().setAnimation("Stop");
        visSpriter.setAnimationPlaying(true);
        Entity player = idManager.get("player");
        player.getComponent(VisSprite.class).setSize(0,0);
        player.edit().add(new AssetReference(new SpriterAsset("spriter/Player/elisa.scml", 0.01f)));
        player.edit().add(visSpriter);
        player.edit().add(new State());
    }
}
