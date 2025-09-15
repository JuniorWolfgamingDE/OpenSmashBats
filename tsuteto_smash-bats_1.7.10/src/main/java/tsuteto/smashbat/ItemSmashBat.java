package tsuteto.smashbat;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSmashBat extends ItemSword
{
    enum EnumMaterial
    {
        WOOD(   0,  4.0F, 0.15F, 1, ToolMaterial.WOOD, null, "smashbat:woodbat"),
        IRON(   1,  6.0F, 0.18F, 1, ToolMaterial.IRON, null, "smashbat:metalbat"),
        EMERALD(3, 12.0F, 0.22F, 1, ToolMaterial.EMERALD, null, "smashbat:diamondbat"),
        GOLD(   2,  2.0F, 2.50F, 1, ToolMaterial.GOLD, null, "smashbat:metalbat"),
        CREEPER(0,  2.5F, 0.45F, 2, ToolMaterial.WOOD, new DelayedEventCreeper(), "smashbat:woodbat"),
        BLAST(  0,  6.0F, 0.20F, 2, ToolMaterial.WOOD, null, null),
        THUNDER(0,  4.0F, 0.19F, 2, ToolMaterial.WOOD, new DelayedEventThunder(), "smashbat:metalbat"),
        RIDING( 0, 15.0F, 0.12F, 2, ToolMaterial.WOOD, null, "smashbat:woodbat");

        public final int damageVsEntity;
        public final float bashPow;
        public final float bashUpRatio;
        public final int cost;
        public final ToolMaterial toolMaterial;
        public final IDelayedEvent delayedEvent;
        public final String hitSound;

        EnumMaterial(int damageVsEntity, float bashPow, float bashUpRatio, int cost, ToolMaterial toolMaterial,
                IDelayedEvent delayedEvent, String hitSound)
        {
            this.damageVsEntity = damageVsEntity;
            this.bashPow = bashPow;
            this.bashUpRatio = bashUpRatio;
            this.cost = cost;
            this.toolMaterial = toolMaterial;
            this.delayedEvent = delayedEvent;
            this.hitSound = hitSound;
        }
    }

    private int weaponDamage;
    private final EnumMaterial material;

    public static HashMap<Integer, SmashEntry> smashEntryMap = new HashMap<Integer, SmashEntry>();
    public static HashMap<Integer, SmashEntry> eventEntryMap = new HashMap<Integer, SmashEntry>();

    public ItemSmashBat(EnumMaterial material)
    {
        super(material.toolMaterial);
        this.weaponDamage = 2 + material.damageVsEntity;
        this.material = material;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity user, int par4, boolean par5)
    {
        super.onUpdate(itemstack, world, user, par4, par5); // for the future

        if (world.isRemote)
        {
            return;
        }

        if (user instanceof EntityLivingBase)
        {
            EntityLivingBase userLiving = (EntityLivingBase) user;

            if (userLiving.getHeldItem() != itemstack || !userLiving.isSwingInProgress)
            {
                return;
            }

            boolean isCriticalHit = isCriticalHit(userLiving);
            Entity entityHitBack = null;

            Vec3 lookVec = user.getLookVec();
            List<Entity> entitiesWithinReach = world.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox
                    .expand(1.0D, 0.5D, 0.0D).offset(lookVec.xCoord, 0.5D, lookVec.zCoord));

            for (Entity entity : entitiesWithinReach)
            {
                // System.out.println(String.format("tick: %d, id: %d, existed: %d, dead: %s",
                // world.getWorldTime(), entity.entityId, entity.ticksExisted,
                // Boolean.toString(entity.isDead)));
                if (entity instanceof EntityThrowable && entity.ticksExisted > 3 && !entity.isDead)
                {
                    entityHitBack = hitBackThrowableEntity((EntityThrowable) entity, world, userLiving);

                    if (entityHitBack != null)
                    {
                        Vec3 smash = calcSmashVector(user, isCriticalHit);
                        entityHitBack.motionX = entity.motionX + smash.xCoord;
                        entityHitBack.motionY = entity.motionY + smash.yCoord;
                        entityHitBack.motionZ = entity.motionZ + smash.zCoord;
                        entityHitBack.isAirBorne = true;
                    }
                }
            }
            if (entityHitBack != null)
            {
                itemstack.damageItem(1, userLiving);
                makeHitSound(user, isCriticalHit);
                if (isCriticalHit && user instanceof EntityPlayer)
                {
                    ((EntityPlayer) user).onCriticalHit(entityHitBack);
                }
            }
        }
    }

    private Entity hitBackThrowableEntity(EntityThrowable entity, World world, EntityLivingBase userliving)
    {
        Class clazz = entity.getClass();

        try
        {
            Constructor con;
            Object[] args;

            if (entity instanceof EntityPotion)
            {
                EntityPotion potion = (EntityPotion) entity;
                con = clazz.getDeclaredConstructor(World.class, EntityLivingBase.class, int.class);
                args = new Object[] { world, userliving, potion.getPotionDamage() };
            }
            else
            {
                con = clazz.getDeclaredConstructor(World.class, EntityLivingBase.class);
                args = new Object[] { world, userliving };
            }

            EntityThrowable newEntity = (EntityThrowable) con.newInstance(args);

            if (world.spawnEntityInWorld(newEntity))
            {
                entity.setDead();
                return newEntity;
            }
        }
        catch (Exception e)
        {
            FMLLog.warning("[SmashBat] Failed to spawn EntityTrowable: %s", entity.toString());
        }
        return null;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityHurt, EntityLivingBase entityAttacking)
    {
        itemstack.damageItem(material.cost, entityAttacking);

        if (!entityAttacking.worldObj.isRemote)
        {
            boolean isCriticalHit = isCriticalHit(entityAttacking);
            Vec3 smash = calcSmashVector(entityAttacking, isCriticalHit);
            boolean isFlyingMob = entityHurt instanceof EntityFlying || entityHurt instanceof EntityBat;

            if (this.material == EnumMaterial.BLAST)
            {
                entityHurt.worldObj.createExplosion(
                        entityAttacking, entityAttacking.posX,
                        entityAttacking.posY + entityAttacking.getEyeHeight(),
                        entityAttacking.posZ, 2.0F, false);
            }

            if (this.material == EnumMaterial.CREEPER && !isFlyingMob)
            {
                entityHurt.playSound("random.fuse", 1.0F, 0.5F);
            }

            if (this.material == EnumMaterial.RIDING && !isFlyingMob)
            {
                entityAttacking.mountEntity(entityHurt);
            }

            // Set delayed events
            if (material.delayedEvent != null)
            {
                SmashEntry newEntry = new SmashEntry();
                newEntry.entityId = entityHurt.getEntityId();
                newEntry.velocity = Vec3.createVectorHelper(smash.xCoord, smash.yCoord, smash.zCoord);
                newEntry.attackerEntityId = entityAttacking.getEntityId();
                newEntry.material = this.material;
                eventEntryMap.put(entityHurt.getEntityId(), newEntry);
            }

            // Smash!
            if (entityHurt instanceof EntityPlayer)
            {
                SmashEntry newEntry = new SmashEntry();
                newEntry.entityId = entityHurt.getEntityId();
                newEntry.velocity = Vec3.createVectorHelper(smash.xCoord, smash.yCoord, smash.zCoord);
                newEntry.attackerEntityId = entityAttacking.getEntityId();
                newEntry.material = this.material;
                smashEntryMap.put(entityHurt.getEntityId(), newEntry);

            }
            else
            {
                entityHurt.addVelocity(smash.xCoord, smash.yCoord, smash.zCoord);
            }

            makeHitSound(entityAttacking, isCriticalHit);
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (material == EnumMaterial.RIDING && player.ridingEntity != null)
        {
            player.mountEntity(player.ridingEntity);
        }
        return super.onItemRightClick(itemstack, world, player);
    }

    @Override
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
                new AttributeModifier(field_111210_e, "Weapon modifier", this.weaponDamage, 0));
        return multimap;
    }

    @Override
    public float func_150893_a(ItemStack itemstack, Block block) // getStrVsBlock
    {
        return Blocks.melon_block.equals(block) ? 15.0F : 1.0F;
    }

    @Override
    public boolean func_150897_b(Block par1Block) // canHarvestBlock
    {
        return false;
    }

    private Vec3 calcSmashVector(Entity entityAttacking, boolean isCriticalHit)
    {
        double vec1 = material.bashPow;
        double vec2 = material.bashUpRatio * material.bashPow;
        if (isCriticalHit)
        {
            vec2 *= 1.5D;
        }
        double vecX = -MathHelper.sin((entityAttacking.rotationYaw * 3.141593F) / 180F) * (float) vec1 * 0.5F;
        double vecY = vec2;
        double vecZ = MathHelper.cos((entityAttacking.rotationYaw * 3.141593F) / 180F) * (float) vec1 * 0.5F;

        return Vec3.createVectorHelper(vecX, vecY, vecZ);
    }

    public boolean isCriticalHit(EntityLivingBase entityAttacking)
    {
        return entityAttacking.fallDistance > 0.0F && !entityAttacking.onGround && !entityAttacking.isOnLadder()
                && !entityAttacking.isInWater() && !entityAttacking.isPotionActive(Potion.blindness)
                && entityAttacking.ridingEntity == null && (entityAttacking instanceof EntityLiving);
    }

    public void makeHitSound(Entity entity, boolean isCriticalHit)
    {
        if (this.material.hitSound != null)
        {
            entity.worldObj.playSoundAtEntity(entity, material.hitSound, isCriticalHit ? 1.0F : 0.75F,
                    itemRand.nextFloat() * 0.2F + 0.9F);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getIconString().replace("item.", "") + (SmashBatMod.use32xTexture ? "_32x" : ""));
    }

    public static interface IDelayedEvent
    {
        void onEvent(SmashEntry entryImpact, Entity entity);
    }

    public static class DelayedEventCreeper implements IDelayedEvent
    {
        @Override
        public void onEvent(SmashEntry event, Entity entity)
        {
            if (entity.isInWater())
            {
                return;
            }
            if (entity instanceof EntityCreeper)
            {
                boolean isMobGriefing = entity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
                float explosionRadius = ((EntityCreeper) entity).getPowered() ? 6.0F : 3.0F;
                entity.worldObj.createExplosion(null, entity.posX, entity.posY, entity.posZ, explosionRadius,
                        isMobGriefing);
                ((EntityCreeper) entity).setDead();
            }
            else
            {
                Entity attacker = entity.worldObj.getEntityByID(event.attackerEntityId);
                entity.worldObj.createExplosion(attacker, entity.posX, entity.posY - 2D, entity.posZ, 2.0F, false);
            }
        }
    }

    public static class DelayedEventThunder implements IDelayedEvent
    {
        @Override
        public void onEvent(SmashEntry event, Entity entity)
        {
            entity.worldObj.addWeatherEffect(new EntityLightningBolt(entity.worldObj, entity.posX, entity.posY,
                    entity.posZ));
        }
    }

    public static class SmashEntry
    {
        public int entityId;
        public Vec3 velocity;
        public EnumMaterial material;
        public int attackerEntityId;
    }
}
