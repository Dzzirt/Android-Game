package com.dark.castle.Systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.dark.castle.Components.AnimationStates;
import com.dark.castle.Components.CharacterStats;
import com.dark.castle.Components.HealthBar;
import com.dark.castle.Components.UIElement;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.VisIDManager;

/**
 * Created by nikita on 04.06.16.
 */
public class CharacterStatsUpdateSystem extends EntityProcessingSystem{

    private ComponentMapper<VisID> idCmp;
    private ComponentMapper<UIElement> uiCmp;
    private ComponentMapper<CharacterStats> statsCmp;
    private ComponentMapper<HealthBar> hpBarCmp;
    private VisIDManager idManager;

    public CharacterStatsUpdateSystem() {
        super(Aspect.all(CharacterStats.class, VisID.class));
    }

    void KillCheck(Entity entity, float hpPercent)
    {
        if (hpPercent < 0) {
            entity.edit().remove(HealthBar.class);
            entity.edit().remove(PhysicsBody.class);
            entity.edit().remove(CharacterStats.class);
            entity.getComponent(AnimationStates.class).getState("Death").isPlaying = true;
        }
    }

    @Override
    protected void process(Entity e) {
        String id = e.getComponent(VisID.class).id;
        CharacterStats characterStats = e.getComponent(CharacterStats.class);
        float hpPercent = (characterStats.health / characterStats.originHp);
        if (e.getComponent(HealthBar.class) != null) {
            HealthBar healthBar = e.getComponent(HealthBar.class);
            healthBar.region.setRegionWidth((int) (healthBar.region.getTexture().getWidth() * hpPercent));
        }
        KillCheck(e, hpPercent);
    }
}
