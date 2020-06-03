// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.server.MinecraftServer;
import com.kentington.thaumichorizons.common.tiles.TileSlot;
import net.minecraft.world.Teleporter;
import com.kentington.thaumichorizons.common.lib.GatewayTeleporter;
import cpw.mods.fml.common.FMLCommonHandler;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.block.BlockBreakable;

public class BlockPortalTH extends BlockBreakable
{
    public BlockPortalTH() {
        super("portal", ThaumicHorizons.portal, false);
        this.setTickRandomly(true);
        this.setBlockUnbreakable();
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World p_149668_1_, final int p_149668_2_, final int p_149668_3_, final int p_149668_4_) {
        return null;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World p_149734_1_, final int p_149734_2_, final int p_149734_3_, final int p_149734_4_, final Random p_149734_5_) {
        for (int l = 0; l < 4; ++l) {
            double d0 = p_149734_2_ + p_149734_5_.nextFloat();
            final double d2 = p_149734_3_ + p_149734_5_.nextFloat();
            double d3 = p_149734_4_ + p_149734_5_.nextFloat();
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            final int i1 = p_149734_5_.nextInt(2) * 2 - 1;
            d4 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            d5 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            d6 = (p_149734_5_.nextFloat() - 0.5) * 0.5;
            if (p_149734_1_.getBlock(p_149734_2_ - 1, p_149734_3_, p_149734_4_) != this && p_149734_1_.getBlock(p_149734_2_ + 1, p_149734_3_, p_149734_4_) != this) {
                d0 = p_149734_2_ + 0.5 + 0.25 * i1;
                d4 = p_149734_5_.nextFloat() * 2.0f * i1;
            }
            else {
                d3 = p_149734_4_ + 0.5 + 0.25 * i1;
                d6 = p_149734_5_.nextFloat() * 2.0f * i1;
            }
            p_149734_1_.spawnParticle("portal", d0, d2, d3, d4, d5, d6);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(final World p_149694_1_, final int p_149694_2_, final int p_149694_3_, final int p_149694_4_) {
        return null;
    }
    
    public int quantityDropped(final Random p_149745_1_) {
        return 0;
    }
    
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity player) {
        if (player.ridingEntity == null && player.riddenByEntity == null && player instanceof EntityPlayerMP) {
            if (player.timeUntilPortal > 0) {
                player.timeUntilPortal = 100;
                return;
            }
            player.timeUntilPortal = 100;
            int targetX = 0;
            int targetY = 0;
            int targetZ = 0;
            int targetDim = 0; //let's see if I can get this to work
            if (world.provider.dimensionId == ThaumicHorizons.dimensionPocketId) {
                final int planeNum = (z + 128) / 256;
                final int which = world.getBlockMetadata(x, y, z);
                switch (which) {
                    case 0: {
                        targetX = PocketPlaneData.planes.get(planeNum).portalA[0];
                        targetY = PocketPlaneData.planes.get(planeNum).portalA[1] - 2;
                        targetZ = PocketPlaneData.planes.get(planeNum).portalA[2];
                        targetDim = PocketPlaneData.planes.get(planeNum).portalA[3];
                        break;
                    }
                    case 2: {
                        targetX = PocketPlaneData.planes.get(planeNum).portalB[0];
                        targetY = PocketPlaneData.planes.get(planeNum).portalB[1] - 2;
                        targetZ = PocketPlaneData.planes.get(planeNum).portalB[2];
                        targetDim = PocketPlaneData.planes.get(planeNum).portalB[3];
                        break;
                    }
                    case 1: {
                        targetX = PocketPlaneData.planes.get(planeNum).portalC[0];
                        targetY = PocketPlaneData.planes.get(planeNum).portalC[1] - 2;
                        targetZ = PocketPlaneData.planes.get(planeNum).portalC[2];
                        targetDim = PocketPlaneData.planes.get(planeNum).portalC[3];
                        break;
                    }
                    case 3: {
                        targetX = PocketPlaneData.planes.get(planeNum).portalD[0];
                        targetY = PocketPlaneData.planes.get(planeNum).portalD[1] - 2;
                        targetZ = PocketPlaneData.planes.get(planeNum).portalD[2];
                        targetDim = PocketPlaneData.planes.get(planeNum).portalD[3];
                        break;
                    }
                }
                final MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                //TODO send player to more than just dim0.
                ((EntityPlayerMP)player).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, targetDim, (Teleporter)new GatewayTeleporter(mServer.worldServerForDimension(ThaumicHorizons.dimensionPocketId), targetX, targetY, targetZ, player.rotationYaw));
            }
            else {
                int slotY = y;
                int slotX = x;
                int slotZ = z;
                do {
                    ++slotY;
                } while (world.getBlock(slotX, slotY, slotZ) == ThaumicHorizons.blockPortal);
                if (world.getBlock(slotX, slotY, slotZ) == ThaumicHorizons.blockGateway) {
                    if (world.getBlock(slotX + 1, slotY, slotZ) == ThaumicHorizons.blockSlot) {
                        ++slotX;
                    }
                    else if (world.getBlock(slotX - 1, slotY, slotZ) == ThaumicHorizons.blockSlot) {
                        --slotX;
                    }
                    else if (world.getBlock(slotX, slotY, slotZ + 1) == ThaumicHorizons.blockSlot) {
                        ++slotZ;
                    }
                    else if (world.getBlock(slotX, slotY, slotZ - 1) == ThaumicHorizons.blockSlot) {
                        --slotZ;
                    }
                }
                final TileEntity te = world.getTileEntity(slotX, slotY, slotZ);
                if (te instanceof TileSlot) {
                    final TileSlot tco = (TileSlot)te;
                    targetY = 128;
                    float targetYaw = 0.0f;
                    switch (tco.which) {
                        case 1: {
                            targetZ = tco.pocketID * 256 + PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 180.0f;
                            break;
                        }
                        case 2: {
                            targetZ = tco.pocketID * 256 - PocketPlaneData.planes.get(tco.pocketID).radius;
                            break;
                        }
                        case 3: {
                            targetZ = tco.pocketID * 256;
                            targetX = PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 90.0f;
                            break;
                        }
                        case 4: {
                            targetZ = tco.pocketID * 256;
                            targetX = -PocketPlaneData.planes.get(tco.pocketID).radius;
                            targetYaw = 270.0f;
                            break;
                        }
                    }
                    final MinecraftServer mServer2 = FMLCommonHandler.instance().getMinecraftServerInstance();
                    //go from portal to pocket plane
                    ((EntityPlayerMP)player).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, ThaumicHorizons.dimensionPocketId, (Teleporter)new GatewayTeleporter(mServer2.worldServerForDimension(ThaumicHorizons.dimensionPocketId), targetX, targetY, targetZ, targetYaw));
                }
            }
        }
    }
}
