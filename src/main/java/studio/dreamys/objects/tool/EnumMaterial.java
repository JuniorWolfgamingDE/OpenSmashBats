package studio.dreamys.objects.tool;

import net.minecraft.item.Item.ToolMaterial;

public enum EnumMaterial {
    // Standard type smash bats
    WOOD(ToolMaterial.WOOD, 0, 1, 0.15f, 4.0f, "opensmashbats:wooden_smash_bat", null),
    IRON(ToolMaterial.IRON, 1, 1, 0.18f, 6.0f, "opensmashbats:metal_smash_bat", null),
    GOLD(ToolMaterial.GOLD, 2, 1, 2.50f, 2.0f, "opensmashbats:metal_smash_bat", null),
    DIAMOND(ToolMaterial.DIAMOND, 3, 1, 0.22f, 12.0f, "opensmashbats:diamond_smash_bat", null),

    // Special type smash bats
    BLAST(ToolMaterial.WOOD, 0, 2, 0.20f, 6.0f, null, null),
    CREEPER(ToolMaterial.WOOD, 0, 2, 0.45f, 2.5f, "opensmashbats:wooden_smash_bat", "creeper"),
    RIDING(ToolMaterial.WOOD, 0, 2, 0.12f, 15.0f, "opensmashbats:wooden_smash_bat", null),
    THUNDER(ToolMaterial.WOOD, 0, 2, 0.12f, 4.0f, "opensmashbats:metal_smash_bat", "thunder");

    private final ToolMaterial material;
    private final int attackDamage;
    private final int durabilityCost;
    private final float upwardsForce;
    private final float generalForce;
    private final String hitSound;
    private final String delayedEventType;

    EnumMaterial(ToolMaterial material, int attackDamage, int durabilityCost, float upwardsForce, float generalForce, String hitSound, String delayedEventType) {
        this.material = material;
        this.attackDamage = attackDamage;
        this.durabilityCost = durabilityCost;
        this.upwardsForce = upwardsForce;
        this.generalForce = generalForce;
        this.hitSound = hitSound;
        this.delayedEventType = delayedEventType;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getDurabilityCost() {
        return durabilityCost;
    }

    public float getUpwardsForce() {
        return upwardsForce;
    }

    public float getGeneralForce() {
        return generalForce;
    }

    public String getHitSound() {
        return hitSound;
    }

    public String getDelayedEventType() {
        return delayedEventType;
    }
}