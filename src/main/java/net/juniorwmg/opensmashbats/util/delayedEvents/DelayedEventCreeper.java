package net.juniorwmg.opensmashbats.util.delayedEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.juniorwmg.opensmashbats.util.interfaces.IDelayedEvent;

public class DelayedEventCreeper implements IDelayedEvent {
    @Override
    public void onEvent(SmashEntry event, Entity entity) {
        if (entity.isInWater()) {
            return;
        }

        if (entity instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) entity;
            boolean isMobGriefing = entity.world.getGameRules().getBoolean("mobGriefing");
            float explosionRadius = creeper.getPowered() ? 6.0F : 3.0F;
            entity.world.createExplosion(null, entity.posX, entity.posY, entity.posZ, explosionRadius, isMobGriefing);
            creeper.setDead();
        } else {
            Entity attacker = entity.world.getEntityByID(event.attacker);
            entity.world.createExplosion(attacker, entity.posX, entity.posY - 2D, entity.posZ, 2.0F, false);
        }
    }
}