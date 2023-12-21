
package com.firehostredux.easaddon.items;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;


import com.firehostredux.easaddon.EventHandler;
import com.firehostredux.easaddon.easAddon;
import com.firehostredux.easaddon.guis.GuiEASReceiver;
import com.firehostredux.easaddon.tabs.TabEASAddon;

@EventHandler.ModElement.Tag
public class ItemEASReceiver extends EventHandler.ModElement {
	@GameRegistry.ObjectHolder("easaddon:eas_receiver")
	public static final Item block = null;
	public ItemEASReceiver(EventHandler instance) {
		super(instance, 292);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("easaddon:eas_receiver", "inventory"));
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			setMaxDamage(0);
			maxStackSize = 64;
			setUnlocalizedName("eas_receiver");
			setRegistryName("eas_receiver");
			setCreativeTab(TabEASAddon.tab);
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getMaxItemUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, IBlockState par2Block) {
			return 1F;
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand) {
			ActionResult<ItemStack> ar = super.onItemRightClick(world, entity, hand);
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			{
				entity.openGui(easAddon.instance, GuiEASReceiver.GUIID, world, x, y, z);
				if(world.isRemote) {
					net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
					if(mc.currentScreen instanceof com.firehostredux.easaddon.guis.GuiEASReceiver.GuiWindow) {
						
					}
				}
			}
			return ar;
		}
		
	}
}
