
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

@EventHandler.ModElement.Tag
public class GuiEASUI extends EventHandler.ModElement {
	public static int GUIID = 1;
	public static HashMap<String, GuiTextField> guistate = new HashMap<String, GuiTextField>();
	public GuiEASUI(EventHandler instance) {
		super(instance, 288);
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
		private World world;
		private int x, y, z;
		private EntityPlayer entity;
		GuiTextField WarningMessage;
		GuiTextField Frequency;
		GuiTextField WatchMessage;
		public GuiWindow(World world, int x, int y, int z, EntityPlayer entity) {
			super(new GuiContainerMod(world, x, y, z, entity));
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.entity = entity;
			this.xSize = 200;
			this.ySize = 132;
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
			WatchMessage.updateCursorCounter();
			WarningMessage.updateCursorCounter();
			Frequency.updateCursorCounter();
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			WatchMessage.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
			WarningMessage.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
			Frequency.mouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}

		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			WarningMessage.textboxKeyTyped(typedChar, keyCode);
			if (WarningMessage.isFocused())
				return;
			WatchMessage.textboxKeyTyped(typedChar, keyCode);
			if (WatchMessage.isFocused())
				return;
			Frequency.textboxKeyTyped(typedChar, keyCode);
			if (Frequency.isFocused())
				return;
			super.keyTyped(typedChar, keyCode);
		}

		@Override
		protected void drawGuiContainerForegroundLayer(int par1, int par2) {
			BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
			NBTTagCompound easBlock = world.getTileEntity(pos).getTileData();
			this.fontRenderer.drawString("Frequency", 41, 93, -1);
			this.fontRenderer.drawString("Sensitivity", 144, 7, -1);
			this.fontRenderer.drawString("Watch Message", 32, 7, -1);
			this.fontRenderer.drawString("Warning Message", 27, 50, -1);
			Frequency.drawTextBox();
			WarningMessage.drawTextBox();
			WatchMessage.drawTextBox();
			this.fontRenderer.drawString(String.valueOf(easBlock.getInteger("easWatchSensitivity")), 167, 24, -1);
			this.fontRenderer.drawString(String.valueOf(easBlock.getInteger("easWarnSensitivity")), 167, 67, -1);
		}


		@Override
		public void onGuiClosed() {
			super.onGuiClosed();
			Keyboard.enableRepeatEvents(false);
		}

		@Override
		public void initGui() {
			BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
			super.initGui();
			this.guiLeft = (this.width - 200) / 2;
			this.guiTop = (this.height - 132) / 2;
			Keyboard.enableRepeatEvents(true);
			this.buttonList.clear();
			WarningMessage = (GuiTextField) guistate.get("text:WarningMessage");
			if (WarningMessage == null){
				 WarningMessage = new GuiTextField(0, this.fontRenderer, 4, 62, 120, 20);
				 guistate.put("text:WarningMessage", WarningMessage);
				 WarningMessage.setMaxStringLength(32767);
			}
			else
				WarningMessage.setText(world.getTileEntity(pos).getTileData().getString("easWarningMessage"));
			this.buttonList.add(new GuiButton(0, this.guiLeft + 147, this.guiTop + 105, 45, 20, "Done"));
			Frequency = (GuiTextField) guistate.get("text:Frequency");
			if (Frequency == null){
				 Frequency = new GuiTextField(1, this.fontRenderer, 4, 105, 120, 20);
				 guistate.put("text:Frequency", Frequency);
				 Frequency.setMaxStringLength(32767);
			}
			else
				Frequency.setText(String.valueOf(world.getTileEntity(pos).getTileData().getDouble("easFreq")));
			Keyboard.enableRepeatEvents(true);
			WatchMessage = (GuiTextField) guistate.get("text:WatchMessage");
			if (WatchMessage == null){
				WatchMessage = new GuiTextField(2, this.fontRenderer, 4, 19, 120, 20);
				 guistate.put("text:WatchMessage", WatchMessage);
				 WatchMessage.setMaxStringLength(32767);
			}
			else
				WatchMessage.setText(world.getTileEntity(pos).getTileData().getString("easWatchMessage"));
			this.buttonList.add(new GuiButton(1, this.guiLeft + 154, this.guiTop + 19, 30, 20, " "));
			this.buttonList.add(new GuiButton(2, this.guiLeft + 154, this.guiTop + 62, 30, 20, " "));

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
	static boolean isValidString(String input) {
		return input.matches("^\\w.+( +\\w.+)*$");
	}
	private static void handleButtonAction(EntityPlayer entity, int buttonID, int x, int y, int z) {
		World world = entity.world;
		// security measure to prevent arbitrary chunk generation
		if (!world.isBlockLoaded(new BlockPos(x, y, z)))
			return;
		//Clicked Done
		else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			switch(buttonID) {
			case 0:{
				GuiTextField FreqField = (GuiTextField) guistate.get("text:Frequency");
				GuiTextField WarnField = (GuiTextField) guistate.get("text:WarningMessage");
				GuiTextField WatchField = (GuiTextField) guistate.get("text:WatchMessage");
				if (!isValidInput(FreqField.getText()) || !isValidString(WarnField.getText()) || !isValidString(WatchField.getText())){
					if (!isValidInput(FreqField.getText())){
						entity.sendMessage(new TextComponentString("Error: Frequency must be a numerical or decimal value."));
						entity.closeScreen();
						return;
					}
					else {
						entity.sendMessage(new TextComponentString("Error: Message boxes can not be empty."));
						entity.closeScreen();
						return;
					}
				}
				NBTTagCompound nbtEAS = new NBTTagCompound();
				nbtEAS.setInteger("world", entity.dimension);
				nbtEAS.setDouble("easFreq", Double.parseDouble(FreqField.getText()));
				nbtEAS.setString("easWarnMessage", WarnField.getText());
				nbtEAS.setString("easWatchMessage", WatchField.getText());
				nbtEAS.setInteger("x", x);
				nbtEAS.setInteger("y", y);
				nbtEAS.setInteger("z", z);
				NetworkHandler.sendServerPacket(2, nbtEAS);
				entity.closeScreen();
				return;
	 			}
				case 1:{
					BlockPos pos = new BlockPos(x, y, z);
					Integer watchSensitivity = world.getTileEntity(pos).getTileData().getInteger("easWatchSensitivity");
					if (watchSensitivity < 15)
						watchSensitivity++;
					else
						watchSensitivity = 2;
					NBTTagCompound nbtEASWatchSensitivity = new NBTTagCompound();
					nbtEASWatchSensitivity.setInteger("world", entity.dimension);
					nbtEASWatchSensitivity.setInteger("x", x);
					nbtEASWatchSensitivity.setInteger("y", y);
					nbtEASWatchSensitivity.setInteger("z", z);
					nbtEASWatchSensitivity.setInteger("easWatchSensitivity", watchSensitivity);
					NetworkHandler.sendServerPacket(3, nbtEASWatchSensitivity);
					return;
				}
				case 2:{
					BlockPos pos = new BlockPos(x, y, z);
					Integer warnSensitivity = world.getTileEntity(pos).getTileData().getInteger("easWarnSensitivity");
					if (warnSensitivity < 15)
						warnSensitivity++;
					else
						warnSensitivity = 3;
					NBTTagCompound nbtEASWarnSensitivity = new NBTTagCompound();
					nbtEASWarnSensitivity.setInteger("world", entity.dimension);
					nbtEASWarnSensitivity.setInteger("x", x);
					nbtEASWarnSensitivity.setInteger("y", y);
					nbtEASWarnSensitivity.setInteger("z", z);
					nbtEASWarnSensitivity.setInteger("easWarnSensitivity", warnSensitivity);
					NetworkHandler.sendServerPacket(4, nbtEASWarnSensitivity);
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
