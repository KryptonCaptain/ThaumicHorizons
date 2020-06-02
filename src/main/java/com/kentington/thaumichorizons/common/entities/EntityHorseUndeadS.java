// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityHorse;

public class EntityHorseUndeadS extends EntityHorse
{
    public EntityHorseUndeadS(final World p_i1685_1_) {
        super(p_i1685_1_);
        this.setHorseType(4);
        this.getNavigator().setAvoidsWater(false);
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) { return false; }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    public int getHorseType() {
        return 4;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean func_110256_cu() {
        return false;
    }

    /* @Override
    public boolean func_110259_cr()
    {
        return true;
    } */

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + 0.2);
    }

    @Override
    public boolean canMateWith(EntityAnimal p_70878_1_) {
        return false;
    }

    private void func_110210_cH() { this.field_110278_bp = 1; }
    private int eatingHaystackCounter;

    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(200) == 0)
        {
            this.func_110210_cH();
        }

        super.onLivingUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
            {
                this.heal(1.0F);
            }

            if (!this.isEatingHaystack() && this.riddenByEntity == null && this.rand.nextInt(300) == 0 && this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ)) == Blocks.grass)
            {
                this.setEatingHaystack(true);
            }

            if (this.isEatingHaystack() && ++this.eatingHaystackCounter > 50)
            {
                this.eatingHaystackCounter = 0;
                this.setEatingHaystack(false);
            }

            if (this.func_110205_ce() && !this.isAdultHorse() && !this.isEatingHaystack())
            {
                EntityHorse entityhorse = this.getClosestHorse(this, 16.0D);

                if (entityhorse != null && this.getDistanceSqToEntity(entityhorse) > 4.0D)
                {
                    PathEntity pathentity = this.worldObj.getPathEntityToEntity(this, entityhorse, 16.0F, true, false, false, true);
                    this.setPathToEntity(pathentity);
                }
            }

            if (this.worldObj.isDaytime()) {
                float f = this.getBrightness(1.0F);

                if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
                    if (!this.isHorseSaddled()) {
                        this.setFire(8);
                    }
                }
            }
        }
    }
}
