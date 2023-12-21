
package com.firehostredux.easaddon.tabs;
import com.firehostredux.easaddon.EventHandler;
import com.firehostredux.easaddon.items.ItemEASReceiver;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

@EventHandler.ModElement.Tag
public class TabEASAddon extends EventHandler.ModElement {
	public TabEASAddon(EventHandler instance) {
		super(instance, 143);
	}

	@Override
	public void initElements() {
		tab = new CreativeTabs("tabeasaddon") {
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemEASReceiver.block, (int) (1));
			}

			@SideOnly(Side.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static CreativeTabs tab;
}
