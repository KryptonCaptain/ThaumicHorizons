// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.lib.networking;

import net.minecraft.world.World;
import com.kentington.thaumichorizons.common.ThaumicHorizons;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketFingersToServer implements IMessage, IMessageHandler<PacketFingersToServer, IMessage>
{
    private int playerid;
    private int dim;
    
    public PacketFingersToServer() {
    }
    
    public PacketFingersToServer(final EntityPlayer player, final int dim) {
        this.playerid = player.getEntityId();
        this.dim = dim;
    }
    
    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.playerid);
        buffer.writeInt(this.dim);
    }
    
    public void fromBytes(final ByteBuf buffer) {
        this.playerid = buffer.readInt();
        this.dim = buffer.readInt();
    }
    
    public IMessage onMessage(final PacketFingersToServer message, final MessageContext ctx) {
        final World world = (World)DimensionManager.getWorld(message.dim);
        final EntityPlayer player = (EntityPlayer)world.getEntityByID(message.playerid);
        player.openGui((Object)ThaumicHorizons.instance, 9, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        return null;
    }
}
