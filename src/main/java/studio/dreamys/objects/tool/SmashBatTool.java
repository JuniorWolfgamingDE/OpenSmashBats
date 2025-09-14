package studio.dreamys.objects.tool;

import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import studio.dreamys.ExampleMod;
import studio.dreamys.init.ItemInit;
import studio.dreamys.util.ConfigManager;
import studio.dreamys.util.delayedEvents.DelayedEventManager;
import studio.dreamys.util.interfaces.IDelayedEvent;
import studio.dreamys.util.interfaces.IHasModel;

public class SmashBatTool extends ItemSword implements IHasModel {
    private final EnumMaterial enumMaterial;
    private final float weaponDamage;

    public SmashBatTool(String name, EnumMaterial enumMaterial) {
        super(enumMaterial.getMaterial());
        this.enumMaterial = enumMaterial;
        this.weaponDamage = 2.0F + enumMaterial.getAttackDamage();
        setTranslationKey(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.COMBAT);
        ItemInit.ITEMS.add(this);
    }

    public String getDelayedEventType() {
        return enumMaterial.getDelayedEventType();
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier",
                            (double)this.weaponDamage, 0));

            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Knockback modifier",
                            (double)enumMaterial.getGeneralForce(), 0));
        }

        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!attacker.world.isRemote) {
            boolean allowFlyRide = ConfigManager.allowRidingFlyingMobs;
            boolean targetCanFly = target instanceof EntityFlying || target instanceof EntityBat || target instanceof EntityBlaze || target instanceof EntityParrot;

            if (enumMaterial == EnumMaterial.RIDING) {
                if (targetCanFly && !allowFlyRide) {
                    return false;
                }

                if (target.getRidingEntity() != null) {
                    target.dismountRidingEntity();
                } else {
                    attacker.startRiding(target, true);
                }
            } else if (enumMaterial == EnumMaterial.BLAST) {
                attacker.world.createExplosion(attacker, attacker.posX, attacker.posY, attacker.posZ, 2.0f, false);
            }

            // Handle delayed events
            String delayedEventType = getDelayedEventType();
            if (delayedEventType != null) {
                boolean isCritical = isCriticalHit(attacker);

                if ("creeper".equals(delayedEventType) && !targetCanFly) {
                    target.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
                }

                double yawRadians = (attacker.rotationYaw * Math.PI) / 180.0;
                double vecX = -Math.sin(yawRadians) * enumMaterial.getGeneralForce() * 0.5F;
                double vecY = enumMaterial.getUpwardsForce() * enumMaterial.getGeneralForce() * (isCritical ? 1.5F : 1.0F);
                double vecZ = Math.cos(yawRadians) * enumMaterial.getGeneralForce() * 0.5F;

                IDelayedEvent.SmashEntry smashEntry = new IDelayedEvent.SmashEntry(
                        target.getEntityId(), vecX, vecY, vecZ, delayedEventType, attacker.getEntityId()
                );

                DelayedEventManager.addDelayedEvent(target.getEntityId(), smashEntry, delayedEventType);
            }

            applyCustomKnockback(target, attacker);
        }

        boolean result = super.hitEntity(stack, target, attacker);
        stack.damageItem(enumMaterial.getDurabilityCost() - 1, attacker);
        return result;
    }

    private void applyCustomKnockback(EntityLivingBase target, EntityLivingBase attacker) {
        boolean isCritical = isCriticalHit(attacker);

        float bashPower = enumMaterial.getGeneralForce();
        float bashUpRatio = enumMaterial.getUpwardsForce();

        float horizontalForce = bashPower * 0.5F;
        float verticalForce = bashUpRatio * bashPower;

        if (isCritical) {
            verticalForce *= 1.5F;
        }

        double yawRadians = (attacker.rotationYaw * Math.PI) / 180.0;
        double vecX = -Math.sin(yawRadians) * horizontalForce;
        double vecZ = Math.cos(yawRadians) * horizontalForce;

        target.addVelocity(vecX, verticalForce, vecZ);
        target.velocityChanged = true;
        playHitSound(attacker, isCritical);

        if (isCritical && attacker instanceof EntityPlayer) {
            ((EntityPlayer) attacker).onCriticalHit(target);
        }
    }

    private boolean isCriticalHit(EntityLivingBase attacker) {
        return attacker.fallDistance > 0.0F
                && !attacker.onGround
                && !attacker.isOnLadder()
                && !attacker.isInWater()
                && attacker.getRidingEntity() == null
                && !attacker.isSneaking();
    }

    private void playHitSound(EntityLivingBase attacker, boolean isCritical) {
        String soundName = enumMaterial.getHitSound();
        SoundEvent sound = SoundEvents.ENTITY_PLAYER_ATTACK_STRONG;

        if (soundName != null) {
            SoundEvent customSound = SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
            if (customSound != null) {
                sound = customSound;
            }
        }

        attacker.world.playSound(
                null, attacker.posX, attacker.posY, attacker.posZ,
                sound, attacker.getSoundCategory(),
                isCritical ? 1.0F : 0.75F,
                0.9F + attacker.world.rand.nextFloat() * 0.2F
        );
    }

    @Override
    public void registerModels() {
        ExampleMod.proxy.registerModel(this, 0, "inventory");
    }

    public EnumMaterial getEnumMaterial() {
        return enumMaterial;
    }

    public float getWeaponDamage() {
        return weaponDamage;
    }
}