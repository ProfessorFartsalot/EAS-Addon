package com.firehostredux.easaddon;

import com.firehostredux.easaddon.items.ItemEASReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkHandler {
	public static void onClientMessage(int index, NBTTagCompound nbt)
	{
		 //Minecraft.getMinecraft().addScheduledTask(() -> {
			 
		 //});
	}
	public static void onServerMessage(int index, NBTTagCompound nbt)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		server.addScheduledTask(() -> {
			switch(index){
				default:{
					easAddon.logger.warn("Message went forward and back at the wrong time: " + index);
				}
				case 0:{
					EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(nbt.getUniqueId("playerUID"));
					if(server.getPlayerList() == null || player == null) {
						easAddon.logger.error("PlayerList or Player does not exist");
						return;
					}
					ItemStack item = player.getHeldItemMainhand();
					if (item.getItem() instanceof ItemEASReceiver.ItemCustom) {
						if(!item.hasTagCompound())
							item.setTagCompound(new NBTTagCompound());
						item.getTagCompound().setDouble("easFreq", nbt.getDouble("easFreq"));
					}
					return;
				}
				case 1:{
					EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(nbt.getUniqueId("playerUID"));
					if (server.getPlayerList() == null || player == null) {
						easAddon.logger.error("PlayerList or Player does not exist");
						return;
					}
					ItemStack item = player.getHeldItemMainhand();
					if (item.getItem() instanceof ItemEASReceiver.ItemCustom) {
						if(!item.hasTagCompound())
							item.setTagCompound(new NBTTagCompound());
						item.getTagCompound().setBoolean("easSound", nbt.getBoolean("easSound"));
					}
					return;
				}
				case 2:{
					World world = server.getWorld(nbt.getInteger("world"));
					BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
					IBlockState bs = world.getBlockState(pos);
					NBTTagCompound easBlock = world.getTileEntity(pos).getTileData();
					easBlock.setString("easWatchMessage", nbt.getString("easWatchMessage"));
					easBlock.setString("easWarnMessage", nbt.getString("easWarnMessage"));
					easBlock.setDouble("easFreq", nbt.getDouble("easFreq"));
					world.notifyBlockUpdate(pos, bs, bs, 3);
					return;
				}
				case 3:{
					World world = server.getWorld(nbt.getInteger("world"));
					BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
					IBlockState bs = world.getBlockState(pos);
					world.getTileEntity(pos).getTileData().setInteger("easWatchSensitivity", nbt.getInteger("easWatchSensitivity"));
					world.notifyBlockUpdate(pos, bs, bs, 3);
					return;
				}
				case 4:{
					World world = server.getWorld(nbt.getInteger("world"));
					BlockPos pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
					IBlockState bs = world.getBlockState(pos);
					world.getTileEntity(pos).getTileData().setInteger("easWarnSensitivity", nbt.getInteger("easWarnSensitivity"));
					world.notifyBlockUpdate(pos, bs, bs, 3);
					return;
				}
			}
		});
	}
	public static boolean sendClientPacket(int index, NBTTagCompound nbt, Object... targets)
    {
        return sendClientPacket(new PacketBase(index, nbt), targets);
    }
    
    public static boolean sendClientPacket(IMessage message, Object... targets)
    {
        if (message == null) return false;
        if (targets.length > 0)
            for (Object target : targets)
            {
                if (target instanceof Integer)
                    easAddon.PACKET_HANDLER.sendToDimension(message, (int)targets[0]);
                else if (target instanceof EntityPlayerMP)
                	easAddon.PACKET_HANDLER.sendTo(message, (EntityPlayerMP)targets[0]);
                else if (target instanceof World)
                	easAddon.PACKET_HANDLER.sendToDimension(message, ((World)targets[0]).provider.getDimension());
            }
        else
        	easAddon.PACKET_HANDLER.sendToAll(message);
        
        return true;
    }

    
    @SideOnly(Side.CLIENT)
    public static boolean sendServerPacket(int index, NBTTagCompound nbt)
    {
        return sendServerPacket(new PacketBase(index, nbt));
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean sendServerPacket(IMessage message)
    {
        if (message == null) return false;
        easAddon.PACKET_HANDLER.sendToServer(message);
        return true;
    }
}
