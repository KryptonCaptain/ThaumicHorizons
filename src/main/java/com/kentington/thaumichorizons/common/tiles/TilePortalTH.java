// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import java.util.Iterator;
import java.util.List;
import thaumcraft.common.Thaumcraft;
import net.minecraft.util.Vec3;
import com.kentington.thaumichorizons.common.lib.PocketPlaneData;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraft.world.Teleporter;
import com.kentington.thaumichorizons.common.lib.NightmareTeleporter;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.TileThaumcraft;

public class TilePortalTH extends TileThaumcraft
{
    public int opencount;
    private int count;
    public int dimension;
    public int pocket;
    
    public TilePortalTH() {
        this.opencount = -1;
        this.count = 0;
        this.dimension = 0;
        this.pocket = -1;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public double getMaxRenderDistanceSquared() {
        return 9216.0;
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox((double)(this.xCoord - 1), (double)(this.yCoord - 1), (double)(this.zCoord - 1), (double)(this.xCoord + 2), (double)(this.yCoord + 2), (double)(this.zCoord + 2));
    }
    
    public void updateEntity() {
        ++this.count;
        if (this.worldObj.isRemote && (this.count % 250 == 0 || this.count == 0)) {
            this.worldObj.playSound(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "thaumcraft:evilportal", 1.0f, 1.0f, false);
        }
        if (this.worldObj.isRemote && this.opencount < 30) {
            ++this.opencount;
        }
        if (!this.worldObj.isRemote && this.count % 5 == 0) {
            final List ents = this.worldObj.getEntitiesWithinAABB((Class)EntityPlayerMP.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(0.5, 1.0, 0.5));
            if (ents.size() > 0) {
                for (final Object e : ents) {
                    final EntityPlayerMP player = (EntityPlayerMP)e;
                    if (player.ridingEntity == null && player.riddenByEntity == null) {
                        final MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                        if (player.timeUntilPortal > 0) {
                            continue;
                        }
                        player.timeUntilPortal = 50;
                        final int oldDim = player.dimension;
                        /*player.mcServer.getConfigurationManager().transferPlayerToDimension(player, this.dimension, (Teleporter)new NightmareTeleporter(mServer.worldServerForDimension(this.dimension)));
                        if (oldDim == ThaumicHorizons.dimensionPocketId && PocketPlaneData.positions.containsKey(player.getCommandSenderName())) {
                            //System.out.println("Loading position " + PocketPlaneData.positions.get(player.getCommandSenderName()));
                            player.setPositionAndUpdate(PocketPlaneData.positions.get(player.getCommandSenderName()).xCoord, PocketPlaneData.positions.get(player.getCommandSenderName()).yCoord, PocketPlaneData.positions.get(player.getCommandSenderName()).zCoord);
                        }
                        else {
                            if (this.pocket < 0) {
                                continue;
                            }
                            PocketPlaneData.positions.put(player.getCommandSenderName(), Vec3.createVectorHelper(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5));
                            //System.out.println("Saving position " + PocketPlaneData.positions.get(player.getCommandSenderName()));
                            player.setPositionAndUpdate(0.5, 129.0, (double)(128 * (this.pocket - 3) + 0.5f));
                            this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
                        */}
                    }
                }
            }

        if (this.count > 250) {
            this.worldObj.playSound(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "thaumcraft:craftfail", 1.0f, 1.0f, false);
            Thaumcraft.proxy.burst(this.getWorldObj(), (double)(this.xCoord + 0.5f), (double)(this.yCoord + 0.5f), (double)(this.zCoord + 0.5f), 3.0f);
            this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        }
    }
    
    @Override
    public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("dimension", this.dimension);
        nbttagcompound.setInteger("count", this.count);
        nbttagcompound.setInteger("pocket", this.pocket);
    }
    
    @Override
    public void readCustomNBT(final NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.dimension = nbttagcompound.getInteger("dimension");
        this.count = nbttagcompound.getInteger("count");
        this.pocket = nbttagcompound.getInteger("pocket");
    }
}
