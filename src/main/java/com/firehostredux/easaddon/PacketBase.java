package com.firehostredux.easaddon;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBase implements IMessage, IMessageHandler<PacketBase, IMessage>
{
    private int index;
    private NBTTagCompound nbt;
    
    public PacketBase() {}
    
    public PacketBase(int index, NBTTagCompound nbt)
    {
        this.index = index;
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        index = buffer.readInt();
        nbt = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(index);
        ByteBufUtils.writeTag(buffer, nbt);
    }

    @Override
    public PacketBase onMessage(PacketBase message, MessageContext ctx)
    {
        if (ctx.side.isClient()) {
        	NetworkHandler.onClientMessage(message.index, message.nbt);
        	System.out.println("onClientMessage");
        }
        else
        	NetworkHandler.onServerMessage(message.index, message.nbt);
        return null;
    }
}
