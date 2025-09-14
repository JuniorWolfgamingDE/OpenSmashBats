package net.juniorwmg.opensmashbats.util.delayedEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.juniorwmg.opensmashbats.util.interfaces.IDelayedEvent;

public class DelayedEventThunder implements IDelayedEvent {
    @Override
    public void onEvent(SmashEntry event, Entity entity) {
        EntityLightningBolt lightning = new EntityLightningBolt(entity.world, entity.posX, entity.posY, entity.posZ, false);
        entity.world.addWeatherEffect(lightning);
    }
}