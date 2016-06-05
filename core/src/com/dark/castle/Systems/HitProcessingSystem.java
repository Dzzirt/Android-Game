package com.dark.castle.Systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Components.CharacterStats;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.system.VisIDManager;

import java.util.Random;

/**
 * Created by nikita on 03.06.16.
 */
public class HitProcessingSystem extends BaseSystem{

    public int check = 0;
    private ComponentMapper<VisSpriter> spriterComponentMapper;
    private ComponentMapper<VisID> idMapper;
    private ComponentMapper<CharacterStats> statsCmp;
    private VisIDManager idManager;
    private Random random = new Random();
    @Override
    protected void processSystem() {
        if (check != 0) {
            processHit(check);
            check = 0;
        }
    }

    private void processHit(int entityId)
    {
        VisID id = idMapper.get(entityId);
        CharacterStats dealerStats = statsCmp.get(entityId);
        int damage = random.nextInt(dealerStats.damage[1] - dealerStats.damage[0]) + dealerStats.damage[0];
        VisSpriter dealerSpriter = spriterComponentMapper.get(entityId);
        if (id.id.equals("player")) {
            Array<Entity> enemies = idManager.getMultiple("enemy");
            for (Entity e : enemies) {
                VisSpriter takerSpriter = spriterComponentMapper.get(e);
                Rectangle takerRectangle = takerSpriter.getBoundingRectangle();
                Rectangle dealerRectangle = dealerSpriter.getBoundingRectangle();
                if (takerRectangle.overlaps(dealerRectangle)) {
                    if (e.getComponent(CharacterStats.class) != null) {
                        e.getComponent(CharacterStats.class).health -= damage;
                        e.getComponent(AnimationStates.class).getState("Hurt").isPlaying = true;
                    }
                }
            }
        } else if (id.id.equals("enemy")) {
            Entity player = idManager.get("player");
            VisSpriter takerSpriter = spriterComponentMapper.get(player);
            Rectangle takerRectangle = takerSpriter.getBoundingRectangle();
            Rectangle dealerRectangle = dealerSpriter.getBoundingRectangle();
            if (takerRectangle.overlaps(dealerRectangle)) {
                player.getComponent(CharacterStats.class).health -= damage;
                player.getComponent(AnimationStates.class).getState("Hurt").isPlaying = true;
            }
        }

    }
}
