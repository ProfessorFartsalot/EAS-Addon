package com.firehostredux.easaddon;
import com.firehostredux.easaddon.guis.GuiEASReceiver;
import com.firehostredux.easaddon.guis.GuiEASUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
		@Override
		public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			if (id == GuiEASUI.GUIID)
				return new GuiEASUI.GuiContainerMod(world, x, y, z, player);
			if (id == GuiEASReceiver.GUIID)
				return new GuiEASReceiver.GuiContainerMod(world, x, y, z, player);
			return null;
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			if (id == GuiEASUI.GUIID)
				return new GuiEASUI.GuiWindow(world, x, y, z, player);
			if (id == GuiEASReceiver.GUIID)
				return new GuiEASReceiver.GuiWindow(world, x, y, z, player);
			return null;
	}
}