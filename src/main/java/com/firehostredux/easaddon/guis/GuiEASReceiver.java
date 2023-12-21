
package com.firehostredux.easaddon.guis;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;

import com.firehostredux.easaddon.EventHandler;
import com.firehostredux.easaddon.NetworkHandler;
import com.firehostredux.easaddon.easAddon;
import com.firehostredux.easaddon.items.ItemEASReceiver;

import net.minecraft.item.Item;

@EventHandler.ModElement.Tag
public class GuiEASReceiver extends EventHandler.ModElement {
	public static int GUIID = 2;
	public static HashMap<Object, Object> guistate = new HashMap<Object, Object>();
	public GuiEASReceiver(EventHandler instance) {
		super(instance, 295);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		elements.addNetworkMessage(GUIButtonPressedMessageHandler.class, GUIButtonPressedMessage.class, Side.SERVER);
		elements.addNetworkMessage(GUISlotChangedMessageHandler.class, GUISlotChangedMessage.class, Side.SERVER);
	}
	public static class GuiContainerMod extends Container implements Supplier<Map<Integer, Slot>> {
		private IInventory internal;
		private Map<Integer, Slot> customSlots = new HashMap<>();
		public GuiContainerMod(World world, int x, int y, int z, EntityPlayer player) {
			this.internal = new InventoryBasic("", true, 0);
		}

		public Map<Integer, Slot> get() {
			return customSlots;
		}

		@Override
		public boolean canInteractWith(EntityPlayer player) {
			return internal.isUsableByPlayer(player);
		}

		@Override
		public void onContainerClosed(EntityPlayer playerIn) {
			super.onContainerClosed(playerIn);
			if ((internal instanceof InventoryBasic) && (playerIn instanceof EntityPlayerMP)) {
				this.clearContainer(playerIn, playerIn.world, internal);
			}
		}
	}

	public static class GuiWindow extends GuiContainer {
		private int x, y, z;
		private EntityPlayer entity;
		GuiTextField ReceiverFrequency;
		public GuiWindow(World world, int x, int y, int z, EntityPlayer entity) {
			super(new GuiContainerMod(world, x, y, z, entity));
			this.x = x;
			this.y = y;
			this.z = z;
			this.entity = entity;
			this.xSize = 176;
			this.ySize = 166;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			this.drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			this.renderHoveredToolTip(mouseX, mouseY);
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
			GL11.glColor4f(1, 1, 1, 1);
			zLevel = 100.0F;
		}

		@Override
		public void updateScreen() {
			super.updateScreen();
			ReceiverFrequency.updateCursorCounter();
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			ReceiverFrequency.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}

		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			ReceiverFrequency.textboxKeyTyped(typedChar, keyCode);
			if (ReceiverFrequency.isFocused())
				return;
			super.keyTyped(typedChar, keyCode);
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			if(entity.getHeldItemMainhand().hasTagCompound() == false)
				entity.getHeldItemMainhand().setTagCompound(new NBTTagCompound());
			this.fontRenderer.drawString("EAS Receiver", 3, 5, -1);
			ReceiverFrequency.drawTextBox();
			this.fontRenderer.drawString("Frequency (123.456 for example)", 3, 33, -1);
			this.fontRenderer.drawString("Sound enabled: " + String.valueOf(entity.getHeldItemMainhand().getTagCompound().getBoolean("easSound")), 4, 127, -1);
		}

		@Override
		public void onGuiClosed() {
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}

		@Override
		public void initGui() {
			super.initGui();
			this.guiLeft = (this.width - 176) / 2;
			this.guiTop = (this.height - 166) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
			ReceiverFrequency = (GuiTextField) guistate.get("text:ReceiverFrequency");
			if (ReceiverFrequency == null){
				 ReceiverFrequency = new GuiTextField(0, this.fontRenderer, 3, 48, 120, 20);
				 guistate.put("text:ReceiverFrequency", ReceiverFrequency);
				 ReceiverFrequency.setMaxStringLength(32767);
			}
			this.buttonList.add(new GuiButton(0, this.guiLeft + 122, this.guiTop + 139, 45, 20, "Done"));
			this.buttonList.add(new GuiButton(1, this.guiLeft + 4, this.guiTop + 139, 85, 20, "Toggle Sound"));
		}

		@Override
		protected void actionPerformed(GuiButton button) {
			easAddon.PACKET_HANDLER.sendToServer(new GUIButtonPressedMessage(button.id, x, y, z));
			handleButtonAction(entity, button.id, x, y, z);
		}

		@Override
		public boolean doesGuiPauseGame() {
			return false;
		}
	}

	public static class GUIButtonPressedMessageHandler implements IMessageHandler<GUIButtonPressedMessage, IMessage> {
		@Override
		public IMessage onMessage(GUIButtonPressedMessage message, MessageContext context) {
			EntityPlayerMP entity = context.getServerHandler().player;
			entity.getServerWorld().addScheduledTask(() -> {
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, buttonID, x, y, z);
			});
			return null;
		}
	}

	public static class GUISlotChangedMessageHandler implements IMessageHandler<GUISlotChangedMessage, IMessage> {
		@Override
		public IMessage onMessage(GUISlotChangedMessage message, MessageContext context) {
			EntityPlayerMP entity = context.getServerHandler().player;
			entity.getServerWorld().addScheduledTask(() -> {
				int slotID = message.slotID;
				int changeType = message.changeType;
				int meta = message.meta;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleSlotAction(entity, slotID, changeType, meta, x, y, z);
			});
			return null;
		}
	}

	public static class GUIButtonPressedMessage implements IMessage {
		int buttonID, x, y, z;
		public GUIButtonPressedMessage() {
		}

		public GUIButtonPressedMessage(int buttonID, int x, int y, int z) {
			this.buttonID = buttonID;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void toBytes(io.netty.buffer.ByteBuf buf) {
			buf.writeInt(buttonID);
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
		}

		@Override
		public void fromBytes(io.netty.buffer.ByteBuf buf) {
			buttonID = buf.readInt();
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
		}
	}

	public static class GUISlotChangedMessage implements IMessage {
		int slotID, x, y, z, changeType, meta;
		public GUISlotChangedMessage() {
		}

		public GUISlotChangedMessage(int slotID, int x, int y, int z, int changeType, int meta) {
			this.slotID = slotID;
			this.x = x;
			this.y = y;
			this.z = z;
			this.changeType = changeType;
			this.meta = meta;
		}

		@Override
		public void toBytes(io.netty.buffer.ByteBuf buf) {
			buf.writeInt(slotID);
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			buf.writeInt(changeType);
			buf.writeInt(meta);
		}

		@Override
		public void fromBytes(io.netty.buffer.ByteBuf buf) {
			slotID = buf.readInt();
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
			changeType = buf.readInt();
			meta = buf.readInt();
		}
	}
	static boolean isValidInput(String input) {
	    return input.matches(".*\\d.*");
	}
	
	private static void handleButtonAction(EntityPlayer entity, int buttonID, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;

		else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			switch (buttonID) {
				case 0:{
					GuiTextField textField = (GuiTextField) guistate.get("text:ReceiverFrequency");
					if(!isValidInput(textField.getText())) {
						entity.sendMessage(new TextComponentString("Error: Frequency must be a numerical or decimal value."));
						entity.closeScreen();
						return;
					}
					Item item = entity.getHeldItemMainhand().getItem();
					if (item instanceof ItemEASReceiver.ItemCustom) {
						NBTTagCompound nbtReceiver = new NBTTagCompound();
						nbtReceiver.setDouble("easFreq", Double.parseDouble(textField.getText()));
						nbtReceiver.setUniqueId("playerUID", entity.getPersistentID());
						NetworkHandler.sendServerPacket(0, nbtReceiver);
						entity.closeScreen();
						return;
					}
				}
				case 1:{
					if(!entity.getHeldItemMainhand().hasTagCompound())
						entity.getHeldItemMainhand().setTagCompound(new NBTTagCompound());
					boolean easSound = entity.getHeldItemMainhand().getTagCompound().getBoolean("easSound");
					if (easSound == false) {
						easSound = true;
						entity.sendMessage(new TextComponentString("EAS Sound Enabled"));
					}
					else{
						easSound = false;
						entity.sendMessage(new TextComponentString("EAS Sound Disabled"));
					}
					NBTTagCompound nbtReceiverSound = new NBTTagCompound();
					nbtReceiverSound.setBoolean("easSound", easSound);
					nbtReceiverSound.setUniqueId("playerUID", entity.getPersistentID());
					NetworkHandler.sendServerPacket(1, nbtReceiverSound);
					entity.closeScreen();
					return;
				}

			}
		}
	}

	private static void handleSlotAction(EntityPlayer entity, int slotID, int changeType, int meta, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
	}
}
