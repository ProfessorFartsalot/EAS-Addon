package com.firehostredux.easaddon.blocks;
import com.firehostredux.easaddon.guis.GuiEASUI;
import com.firehostredux.easaddon.items.ItemEASReceiver;
import com.firehostredux.easaddon.mrbt0907util.Maths;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.SoundType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import java.util.ArrayList;
import java.util.List;

import com.firehostredux.easaddon.EventHandler;
import com.firehostredux.easaddon.easAddon;
import com.firehostredux.easaddon.tabs.TabEASAddon;

@EventHandler.ModElement.Tag
public class BlockEASBlock extends EventHandler.ModElement {
  @GameRegistry.ObjectHolder("easaddon:eas_block")
  public static final Block block = null;
  private static boolean easWarning = false;
  private static boolean easWatch = false;
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  public BlockEASBlock(EventHandler instance) {
    super(instance, 288);
  }

  @Override
  public void initElements() {
    elements.blocks.add(() -> new BlockCustom().setRegistryName("eas_block"));
    elements.items.add(() -> new ItemBlock(block).setRegistryName(block.getRegistryName()));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void init(FMLInitializationEvent event) {
    GameRegistry.registerTileEntity(TileEntityCustom.class, "easaddon:tileentityeas_block");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerModels(ModelRegistryEvent event) {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
      new ModelResourceLocation(block.getRegistryName(), "inventory"));
  }
  public static class BlockCustom extends Block implements ITileEntityProvider {
    public BlockCustom() {
      super(Material.ROCK);
      setUnlocalizedName("eas_block");
      setSoundType(SoundType.STONE);
      setHardness(1F);
      setResistance(10F);
      setLightLevel(0F);
      setLightOpacity(255);
      setCreativeTab(TabEASAddon.tab);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileEntityCustom();
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
      super.eventReceived(state, worldIn, pos, eventID, eventParam);
      TileEntity tileentity = worldIn.getTileEntity(pos);
      return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
      return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
      TileEntity tileentity = world.getTileEntity(pos);
      if (tileentity instanceof TileEntityCustom)
        InventoryHelper.dropInventoryItems(world, pos, (TileEntityCustom) tileentity);
      world.removeTileEntity(pos);
      super.breakBlock(world, pos, state);
    }

    public double GetRandomFrequency() {
      Double freq = 0.0;
      int rand = Maths.random(1, 7);
	      switch (rand) {
	      case 1: {
	        freq = 162.400;
	        return freq;
	      }
	      case 2: {
	        freq = 162.425;
	        return freq;
	      }
	      case 3: {
	        freq = 162.450;
	        return freq;
	      }
	      case 4: {
	        freq = 162.475;
	        return freq;
	      }
	      case 5: {
	        freq = 162.500;
	        return freq;
	      }
	      case 6: {
	        freq = 162.525;
	        return freq;
	      }
	      case 7: {
	        freq = 162.550;
	        return freq;
	      }
      }
      return freq;
    }
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
      super.onBlockAdded(world, pos, state);
    }
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }
    public void setDefaultBlockTags(World world, BlockPos pos) {
    	 NBTTagCompound easBlock = world.getTileEntity(pos).getTileData();
         IBlockState bs = world.getBlockState(pos);
         easBlock.setString("easWatchMessage", "");
         easBlock.setString("easWarningMessage", "");
         easBlock.setDouble("easFreq", GetRandomFrequency());
         easBlock.setBoolean("easSound", false);
         easBlock.setInteger("easWatchSensitivity", 3);
         easBlock.setInteger("easWarnSensitivity", 4);
         world.notifyBlockUpdate(pos, bs, bs, 3);
    }
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.withProperty(FACING, enumfacing);
        BlockPos blockpos = pos.north();
        BlockPos blockpos1 = pos.south();
        BlockPos blockpos2 = pos.west();
        BlockPos blockpos3 = pos.east();
        boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
        boolean flag1 = this == worldIn.getBlockState(blockpos1).getBlock();
        boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
        boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();
        if (!flag && !flag1 && !flag2 && !flag3)
        {
            worldIn.setBlockState(pos, state, 3);
            setDefaultBlockTags(worldIn, pos);
        }
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || !flag && !flag1)
        {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
            {
                if (flag2)
                {
                    worldIn.setBlockState(blockpos2, state, 3);
                    setDefaultBlockTags(worldIn, pos);
                }
                else
                {
                    worldIn.setBlockState(blockpos3, state, 3);
                    setDefaultBlockTags(worldIn, pos);
                }

                worldIn.setBlockState(pos, state, 3);
                setDefaultBlockTags(worldIn, pos);
            }
        }
        else
        {
            if (flag)
            {
                worldIn.setBlockState(blockpos, state, 3);
            }
            else
            {
                worldIn.setBlockState(blockpos1, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }
    }
    public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = null;

        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing1));

            if (iblockstate.getBlock() == this)
            {
                return state;
            }

            if (iblockstate.isFullBlock())
            {
                if (enumfacing != null)
                {
                    enumfacing = null;
                    break;
                }

                enumfacing = enumfacing1;
            }
        }

        if (enumfacing != null)
        {
            return state.withProperty(FACING, enumfacing.getOpposite());
        }
        else
        {
            EnumFacing enumfacing2 = (EnumFacing)state.getValue(FACING);

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock())
            {
                enumfacing2 = enumfacing2.getOpposite();
            }

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock())
            {
                enumfacing2 = enumfacing2.rotateY();
            }

            if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock())
            {
                enumfacing2 = enumfacing2.getOpposite();
            }

            return state.withProperty(FACING, enumfacing2);
        }
    }
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    public List<EntityPlayer> getLinkedEASClient(World world) {
      List<EntityPlayer> linkedClient = new ArrayList<EntityPlayer>();
      final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
      for (int i = 0; i < server.getPlayerList().getPlayers().size(); i++) {
        if (((server.getPlayerList().getPlayers().get(i) instanceof EntityPlayer) ? (server.getPlayerList().getPlayers().get(i).inventory.hasItemStack(new ItemStack(ItemEASReceiver.block, (int)(1)))) : false))
          linkedClient.add(server.getPlayerList().getPlayers().get(i));
      }
      return linkedClient;
    }
    public NBTTagCompound getLinkedEASItem(EntityPlayer player) {
      ItemStack linkedItem = null;
      for (int inv = 0; inv < 36; inv++) {
        if (player.inventory.getStackInSlot(inv).toString().equals("1xitem.eas_receiver@0"))
          linkedItem = player.inventory.getStackInSlot(inv);
      }
      return linkedItem.getTagCompound();
    }
    public boolean linkedItemMatchesFrequency(NBTTagCompound rx, NBTTagCompound tx) {
    	if(rx.getDouble("easFreq") == (tx.getDouble("easFreq")))
    		return true;
    	return false;
    }
    @SuppressWarnings("deprecation")
	@Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos)
    {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos);
        if (world.isRemote) return;
        Integer easSignal = world.isBlockIndirectlyGettingPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        NBTTagCompound easBlock = world.getTileEntity(pos).getTileData();
        easWarning = easSignal == easBlock.getInteger("easWarnSensitivity");
        easWatch = easWarning ? false : easSignal == easBlock.getInteger("easWatchSensitivity");
        TextFormatting TextFormatEAS = easWarning ? TextFormatting.DARK_RED : TextFormatting.GOLD;
        if (easWarning || easWatch)
        {
            List<EntityPlayer> players = getLinkedEASClient(world);
            for (EntityPlayer player : players)
            {
                NBTTagCompound linkedItem = getLinkedEASItem(player);
                if(linkedItemMatchesFrequency(linkedItem, easBlock))
                {
                    if(linkedItem.getBoolean("easSound"))
                        world.playSound(null, player.posX, player.posY, player.posZ, EventHandler.sounds.get(new ResourceLocation("easaddon:eas")), SoundCategory.BLOCKS, 0.5F, 1.0F);
                    player.sendMessage(new TextComponentString((TextFormatEAS + "--- EAS BROADCAST REQUESTED ---\n" + TextFormatting.RESET + easBlock.getString(easWarning  ? "easWarnMessage" : "easWatchMessage") + "\n" + TextFormatEAS + "------------------------------")));
                }
            }
        }
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing direction,
      float hitX, float hitY, float hitZ) {
      super.onBlockActivated(world, pos, state, entity, hand, direction, hitX, hitY, hitZ);
      int x = pos.getX();
      int y = pos.getY();
      int z = pos.getZ();
      if (entity instanceof EntityPlayer) {
        ((EntityPlayer) entity).openGui(easAddon.instance, GuiEASUI.GUIID, world, x, y, z);
      }
      return true;
    }
  }

  public static class TileEntityCustom extends TileEntityLockableLoot {
    private NonNullList < ItemStack > stacks = NonNullList. < ItemStack > withSize(0, ItemStack.EMPTY);
    @Override
    public int getSizeInventory() {
      return 0;
    }

    @Override
    public boolean isEmpty() {
      for (ItemStack itemstack: this.stacks)
        if (!itemstack.isEmpty())
          return false;
      return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      return stacks.get(slot);
    }

    @Override
    public String getName() {
      return "container.eas_block";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.stacks = NonNullList. < ItemStack > withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (!this.checkLootAndRead(compound))
        ItemStackHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      if (!this.checkLootAndWrite(compound))
        ItemStackHelper.saveAllItems(compound, this.stacks);
      return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
      this.readFromNBT(tag);
    }

    @Override
    public int getInventoryStackLimit() {
      return 64;
    }

    @Override
    public String getGuiID() {
      return "easaddon:eas_block";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
      return new GuiEASUI.GuiContainerMod(this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), playerIn);
    }

    @Override
    protected NonNullList < ItemStack > getItems() {
      return this.stacks;
    }
  }
}