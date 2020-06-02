// 
// Decompiled by Procyon v0.5.30
// 

package com.kentington.thaumichorizons.common.lib.networking;

import java.util.Iterator;
import java.util.Set;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.kentington.thaumichorizons.common.entities.EntityWizardCow;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.aspects.Aspect;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketGetCowData implements IMessage, IMessageHandler<PacketGetCowData, IMessage>
{
    int id;
    static Aspect[] sorted;
    
    public PacketGetCowData() {
    }
    
    public PacketGetCowData(final int id) {
        this.id = id;
    }
    
    public IMessage onMessage(final PacketGetCowData message, final MessageContext ctx) {
        final World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        final Entity ent = world.getEntityByID(message.id);
        if (ent instanceof EntityWizardCow) {
            final EntityWizardCow cow = (EntityWizardCow)ent;
            final AspectList ess = cow.getEssentia();
            final int mod = cow.nodeMod;
            final int type = cow.nodeType;
            final int[] types = new int[ess.size()];
            final int[] amounts = new int[ess.size()];
            int pointer = 0;
            for (final Aspect asp : ess.getAspects()) {
                amounts[pointer] = ess.getAmount(asp);
                for (int i = 0; i < PacketGetCowData.sorted.length; ++i) {
                    if (PacketGetCowData.sorted[i].getTag().equals(asp.getTag())) {
                        types[pointer] = i;
                        ++pointer;
                        break;
                    }
                }
            }
            return (IMessage)new PacketCowUpdate(types, amounts, type, mod, message.id);
        }
        return null;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.id = buf.readInt();
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.id);
    }
    
    static {
        final Set keys = Aspect.aspects.keySet();
        final Iterator it = keys.iterator();
        final AspectList list = new AspectList();
        while (it.hasNext()) {
            list.add(Aspect.aspects.get(it.next()), 1);
        }
        PacketGetCowData.sorted = list.getAspectsSorted();
    }
}
