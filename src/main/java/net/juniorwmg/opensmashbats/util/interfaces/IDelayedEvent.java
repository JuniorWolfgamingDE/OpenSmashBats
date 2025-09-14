package net.juniorwmg.opensmashbats.util.interfaces;

import net.minecraft.entity.Entity;

public interface IDelayedEvent {
    void onEvent(SmashEntry event, Entity entity);

    public static class SmashEntry {
        public int victim;
        public double velocityX;
        public double velocityY;
        public double velocityZ;
        public String materialName;
        public int attacker;
        public boolean wasAirborne;
        public double lastMotionY;

        public SmashEntry(int victim, double velocityX, double velocityY, double velocityZ, String materialName, int attacker) {
            this.victim = victim;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.velocityZ = velocityZ;
            this.materialName = materialName;
            this.attacker = attacker;
            this.wasAirborne = false;
            this.lastMotionY = 0.0;
        }
    }
}