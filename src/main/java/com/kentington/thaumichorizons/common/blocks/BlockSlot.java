// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.items.wands.ItemWandCasting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import com.kentington.thaumichorizons.common.tiles.TileSlot;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockContainer;

public class BlockSlot extends BlockContainer
{
    public BlockSlot() {
        super(Material.rock);
        this.setHardness(2.5f);
        this.setResistance(2.5f);
        this.setBlockName("ThaumicHorizons_slot");
        this.setBlockTextureName("ThaumicHorizons:void");
        this.setCreativeTab(ThaumicHorizons.tabTH);
    }
    
    public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int md) {
        final TileSlot tco = (TileSlot)world.getTileEntity(x, y, z);
        if (tco.portalOpen) {
            tco.destroyPortal();
        }
        if (tco.hasKeystone) {
            final int dim = tco.removeKeystone();
            final ItemStack keystone = new ItemStack(ThaumicHorizons.itemKeystone);
            (keystone.stackTagCompound = new NBTTagCompound()).setInteger("dimension", dim);
            final EntityItem dropped = new EntityItem(world);
            dropped.setEntityItemStack(keystone);
            dropped.setPosition(x + 0.5, y + 0.5, z + 0.5);
            world.spawnEntityInWorld((Entity)dropped);
        }
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        final TileSlot tco = (TileSlot)world.getTileEntity(x, y, z);
        final ItemStack theItem = player.getHeldItem();
        if (tco.hasKeystone) {
            if (player.getHeldItem() == null) {
                final int dim = tco.removeKeystone();
                final ItemStack keystone = new ItemStack(ThaumicHorizons.itemKeystone);
                (keystone.stackTagCompound = new NBTTagCompound()).setInteger("dimension", dim);
                player.inventory.addItemStackToInventory(keystone);
                if (tco.portalOpen) {
                    tco.destroyPortal();
                }
            }
            else if (!tco.portalOpen && player.getHeldItem().getItem() instanceof ItemWandCasting && player.worldObj.provider.dimensionId != 1) {//don't come through from end. vanilla bug
                tco.makePortal(player);
            }
        }
        else if (theItem != null && theItem.getItem() == ThaumicHorizons.itemKeystone && theItem.stackTagCompound != null) {
            tco.insertKeystone(theItem.stackTagCompound.getInteger("dimension"));
            final ItemStack itemStack = theItem;
            --itemStack.stackSize;
        }
        world.markBlockForUpdate(x, y, z);
        return false;
    }
    
    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return new TileSlot();
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public int getRenderType() {
        return ThaumicHorizons.blockSlotRI;
    }
}
