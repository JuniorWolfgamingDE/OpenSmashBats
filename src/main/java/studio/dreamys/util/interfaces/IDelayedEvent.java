package studio.dreamys.util.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface IDelayedEvent {
    void execute(EntityLivingBase target, EntityLivingBase attacker);
}