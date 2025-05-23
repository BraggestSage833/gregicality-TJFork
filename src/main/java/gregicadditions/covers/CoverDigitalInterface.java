package gregicadditions.covers;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import gregicadditions.client.ClientHandler;
import gregicadditions.client.renderer.RenderHelper;
import gregicadditions.widgets.WidgetOreList;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IWorkable;
import gregtech.api.capability.impl.*;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.CoverWithUI;
import gregtech.api.cover.ICoverable;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.*;
import gregtech.api.metatileentity.IRenderMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pipenet.tile.PipeCoverableImplementation;
import gregtech.api.util.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CoverDigitalInterface extends CoverBehavior implements IRenderMetaTileEntity, ITickable, CoverWithUI {

    public static String path = "cover.digital";
    public static IEnergyContainer proxyCapability = new IEnergyContainer() {
        @Override
        public long acceptEnergyFromNetwork(EnumFacing enumFacing, long l, long l1) {
            return 0;
        }

        @Override
        public boolean inputsEnergy(EnumFacing enumFacing) {
            return false;
        }

        @Override
        public long changeEnergy(long l) {
            return 0;
        }

        @Override
        public long getEnergyStored() {
            return 0;
        }

        @Override
        public long getEnergyCapacity() {
            return 0;
        }

        @Override
        public long getInputAmperage() {
            return 0;
        }

        @Override
        public long getInputVoltage() {
            return 0;
        }
    };

    public enum MODE {
        FLUID,
        ITEM,
        ENERGY,
        MACHINE,
        PROXY;
        public static MODE[] VALUES;

        static {
            VALUES = MODE.values();
        }
    }

    // run-time data
    private FluidTankProperties[] fluids = new FluidTankProperties[0];
    private ItemStack[] items = new ItemStack[0];
    private int maxItemCapability = 0;
    private long energyStored = 0;
    private long energyCapability = 0;
    private long energyInputPerDur = 0;
    private long energyOutputPerDur = 0;
    private final List<Long> inputEnergyList = new ArrayList<>();
    private final List<Long> outputEnergyList = new ArrayList<>();
    private int progress = 0;
    private int maxProgress = 0;
    private boolean isActive = true;
    private boolean isWorkingEnable = false;
    private long lastClickTime;
    private UUID lastClickUUID;
    // persistent data
    private int slot = 0;
    private MODE mode = MODE.PROXY;
    private EnumFacing spin = EnumFacing.NORTH;
    private final int[] proxyMode = new int[]{0, 0, 0, 0}; // server-only


    public CoverDigitalInterface(ICoverable coverHolder, EnumFacing attachedSide) {
        super(coverHolder, attachedSide);
    }

    public MODE getMode() {
        return mode;
    }

    public boolean isProxy() {
        return mode == MODE.PROXY;
    }

    public void setMode(MODE mode, int slot, EnumFacing spin) {
        if (this.coverHolder instanceof PipeCoverableImplementation || (this.mode == mode && (this.slot == slot || slot < 0) && this.spin == spin)) return;
        if (!isRemote()) {
            if (this.mode != MODE.PROXY && mode == MODE.PROXY) {
                proxyMode[0] = 0;
                proxyMode[1] = 0;
                proxyMode[2] = 0;
                proxyMode[3] = 0;
            }
            this.mode = mode;
            this.slot = slot;
            this.spin = spin;
            writeUpdateData(1, packetBuffer -> {
                packetBuffer.writeByte(mode.ordinal());
                packetBuffer.writeInt(slot);
                packetBuffer.writeByte(spin.getIndex());
            });
            if (this.coverHolder != null) {
                this.coverHolder.notifyBlockUpdate();
                this.coverHolder.markDirty();
            }
        } else {
            if ((this.mode != mode || this.spin != spin) && this.coverHolder != null) {
                this.coverHolder.scheduleRenderUpdate();
            }
            this.mode = mode;
            this.slot = slot;
            this.spin = spin;
        }
    }

    public void setMode(EnumFacing spin) {
        this.setMode(this.mode, this.slot, spin);
    }

    public void setMode(int slot) {
        this.setMode(this.mode, slot, this.spin);
    }

    public void setMode(MODE mode) {
        this.setMode(mode, this.slot, this.spin);
    }

    public void setEnergyChanged(long energyAdded) {
        if (!isRemote() && (this.mode == MODE.ENERGY || (this.mode == MODE.PROXY && this.proxyMode[2] > 0))) {
            if (energyAdded > 0) {
                energyInputPerDur = energyInputPerDur + energyAdded;
            } else if (energyAdded < 0) {
                energyOutputPerDur = energyOutputPerDur - energyAdded;
            }
        }
    }

    public boolean subProxyMode(MODE mode) {
        if (this.mode == MODE.PROXY) {
            proxyMode[mode.ordinal()]++;
            this.markAsDirty();
            return true;
        }
        return false;
    }

    public boolean unSubProxyMode(MODE mode) {
        if (this.mode == MODE.PROXY) {
            if (proxyMode[mode.ordinal()] > 0) {
                proxyMode[mode.ordinal()]--;
                this.markAsDirty();
                return true;
            }
        }
        return false;
    }

    public TileEntity getCoveredTE() {
        if (this.coverHolder instanceof PipeCoverableImplementation) {
            return this.coverHolder.getWorld().getTileEntity(this.coverHolder.getPos().offset(this.attachedSide));
        } else if (this.coverHolder instanceof MetaTileEntity){
            return ((MetaTileEntity) this.coverHolder).getHolder();
        }
        return null;
    }

    public EnumFacing getCoveredFacing() {
        if (this.coverHolder instanceof PipeCoverableImplementation) {
            return this.attachedSide.getOpposite();
        } else {
            return this.attachedSide;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setByte("cdiMode", (byte) this.mode.ordinal());
        tagCompound.setByte("cdiSpin", (byte) this.spin.ordinal());
        tagCompound.setInteger("cdiSlot", this.slot);
        tagCompound.setInteger("cdi0", this.proxyMode[0]);
        tagCompound.setInteger("cdi1", this.proxyMode[1]);
        tagCompound.setInteger("cdi2", this.proxyMode[2]);
        tagCompound.setInteger("cdi3", this.proxyMode[3]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.mode = tagCompound.hasKey("cdiMode") ? MODE.VALUES[tagCompound.getByte("cdiMode")] : MODE.PROXY;
        this.spin = tagCompound.hasKey("cdiSpin") ? EnumFacing.byIndex(tagCompound.getByte("cdiSpin")) : EnumFacing.NORTH;
        this.slot = tagCompound.hasKey("cdiSlot") ? tagCompound.getInteger("cdiSlot") : 0;
        this.proxyMode[0] = tagCompound.hasKey("cdi0") ? tagCompound.getInteger("cdi0") : 0;
        this.proxyMode[1] = tagCompound.hasKey("cdi1") ? tagCompound.getInteger("cdi1") : 0;
        this.proxyMode[2] = tagCompound.hasKey("cdi2") ? tagCompound.getInteger("cdi2") : 0;
        this.proxyMode[3] = tagCompound.hasKey("cdi3") ? tagCompound.getInteger("cdi3") : 0;
    }

    @Override
    public void onAttached(ItemStack itemStack) { // called when cover placed.
        super.onAttached(itemStack);
        if (this.coverHolder instanceof PipeCoverableImplementation) {
            this.mode = MODE.PROXY;
        } else if (getFluidCapability() != null) {
            fluids = new FluidTankProperties[getFluidCapability().getTankProperties().length];
            this.mode = MODE.FLUID;
        } else if (getItemCapability() != null) {
            items = new ItemStack[getItemCapability().getSlots()];
            this.mode = MODE.ITEM;
        } else if (getEnergyCapability() != null) {
            this.mode = MODE.ENERGY;
        } else if (getMachineCapability() != null) {
            this.mode = MODE.MACHINE;
        }
    }

    @Override
    public void writeInitialSyncData(PacketBuffer packetBuffer) {
        super.writeInitialSyncData(packetBuffer);
        packetBuffer.writeEnumValue(mode);
        packetBuffer.writeEnumValue(spin);
        packetBuffer.writeInt(slot);
        syncAllInfo();
        writeAllFluids(packetBuffer);
        writeAllItems(packetBuffer);
        packetBuffer.writeInt(maxItemCapability);
        packetBuffer.writeLong(energyStored);
        packetBuffer.writeLong(energyCapability);
        packetBuffer.writeInt(inputEnergyList.size());
        for (int i = 0; i < inputEnergyList.size(); i++) {
            packetBuffer.writeLong(inputEnergyList.get(i));
            packetBuffer.writeLong(outputEnergyList.get(i));
        }
        packetBuffer.writeInt(progress);
        packetBuffer.writeInt(maxProgress);
        packetBuffer.writeBoolean(isActive);
        packetBuffer.writeBoolean(isWorkingEnable);
    }

    @Override
    public void readInitialSyncData(PacketBuffer packetBuffer) {
        super.readInitialSyncData(packetBuffer);
        this.mode = packetBuffer.readEnumValue(MODE.class);
        this.spin = packetBuffer.readEnumValue(EnumFacing.class);
        this.slot = packetBuffer.readInt();
        readFluids(packetBuffer);
        readItems(packetBuffer);
        maxItemCapability = packetBuffer.readInt();
        energyStored = packetBuffer.readLong();
        energyCapability = packetBuffer.readLong();
        int size = packetBuffer.readInt();
        inputEnergyList.clear();
        outputEnergyList.clear();
        for (int i = 0; i < size; i++) {
            inputEnergyList.add(packetBuffer.readLong());
            outputEnergyList.add(packetBuffer.readLong());
        }
        progress = packetBuffer.readInt();
        maxProgress = packetBuffer.readInt();
        isActive = packetBuffer.readBoolean();
        isWorkingEnable = packetBuffer.readBoolean();
    }

    @Override
    public void update() {
        if (!isRemote()) {
            syncAllInfo();
        }
    }

    @Override
    public EnumActionResult onScrewdriverClick(EntityPlayer playerIn, EnumHand hand, CuboidRayTraceResult hitResult) {
        if (!this.coverHolder.getWorld().isRemote) {
            this.openUI((EntityPlayerMP) playerIn);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public EnumActionResult onRightClick(EntityPlayer playerIn, EnumHand hand, CuboidRayTraceResult rayTraceResult) {
        if (!isRemote()) {
            if (this.coverHolder.getWorld().getTotalWorldTime() - lastClickTime < 2 && playerIn.getPersistentID().equals(lastClickUUID)) {
                return EnumActionResult.SUCCESS;
            }
            lastClickTime = this.coverHolder.getWorld().getTotalWorldTime();
            lastClickUUID = playerIn.getPersistentID();
            if (playerIn.isSneaking() && playerIn.getHeldItemMainhand().isEmpty()) {
                if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    double x = 0;
                    double y = 1 - rayTraceResult.hitVec.y + rayTraceResult.getBlockPos().getY();
                    if (rayTraceResult.sideHit == EnumFacing.EAST) {
                        x = 1 - rayTraceResult.hitVec.z + rayTraceResult.getBlockPos().getZ();
                    } else if (rayTraceResult.sideHit == EnumFacing.SOUTH) {
                        x = rayTraceResult.hitVec.x - rayTraceResult.getBlockPos().getX();
                    } else if (rayTraceResult.sideHit == EnumFacing.WEST) {
                        x = rayTraceResult.hitVec.z - rayTraceResult.getBlockPos().getZ();
                    } else if (rayTraceResult.sideHit == EnumFacing.NORTH) {
                        x = 1 - rayTraceResult.hitVec.x + rayTraceResult.getBlockPos().getX();
                    }
                    if (1f / 16 < x && x < 4f / 16 && 1f / 16 < y && y < 4f / 16) {
                        this.setMode(this.slot - 1);
                        return EnumActionResult.SUCCESS;
                    } else if (12f / 16 < x && x < 15f / 16 && 1f / 16 < y && y < 4f / 16) {
                        this.setMode(this.slot + 1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            return modeRightClick(playerIn, hand, this.mode, this.slot);
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean onLeftClick(EntityPlayer entityPlayer, CuboidRayTraceResult hitResult) {
        if (!isRemote()) {
            if (this.coverHolder.getWorld().getTotalWorldTime() - lastClickTime < 2 && entityPlayer.getPersistentID().equals(lastClickUUID)) {
                return true;
            }
            lastClickTime = this.coverHolder.getWorld().getTotalWorldTime();
            lastClickUUID = entityPlayer.getPersistentID();
            return modeLeftClick(entityPlayer, this.mode, this.slot);
        }
        return false;
    }

    public EnumActionResult modeRightClick(EntityPlayer playerIn, EnumHand hand, MODE mode, int slot) {
        IFluidHandler fluidHandler = this.getFluidCapability();
        if (mode == MODE.FLUID && fluidHandler != null) {
            if (!FluidUtil.interactWithFluidHandler(playerIn, hand, fluidHandler)) {
                if (fluidHandler instanceof FluidHandlerProxy && FluidUtil.interactWithFluidHandler(playerIn, hand, ((FluidHandlerProxy) fluidHandler).input)) {
                    return EnumActionResult.SUCCESS;
                }
                return EnumActionResult.PASS;
            }
            return EnumActionResult.SUCCESS;
        }
        IItemHandler itemHandler = this.getItemCapability();
        if (mode == MODE.ITEM && itemHandler != null) {
            if (itemHandler.getSlots() > slot && slot >= 0) {
                ItemStack hold = playerIn.getHeldItemMainhand();
                if (!hold.isEmpty()) {
                    ItemStack origin = hold.copy();
                    boolean flag = false;
                    if (playerIn.isSneaking()) {
                        int size = playerIn.getHeldItemMainhand().getCount();
                        playerIn.setHeldItem(EnumHand.MAIN_HAND, itemHandler.insertItem(slot, hold, false));
                        flag = playerIn.getHeldItemMainhand().getCount() != size;
                    } else {
                        ItemStack itemStack = hold.copy();
                        itemStack.setCount(1);
                        if (itemHandler.insertItem(slot, itemStack, false).isEmpty()) {
                            hold.setCount(hold.getCount() - 1);
                            flag = true;
                        }
                    }
                    if (playerIn.getHeldItemMainhand().isEmpty()) {
                        for (ItemStack itemStack : playerIn.inventory.mainInventory) {
                            if (origin.isItemEqual(itemStack)) {
                                playerIn.setHeldItem(EnumHand.MAIN_HAND, itemStack.copy());
                                itemStack.setCount(0);
                                break;
                            }
                        }
                    }
                    return flag ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
                }
            }
            return EnumActionResult.PASS;
        }
        IWorkable workable = this.getMachineCapability();
        if (mode == MODE.MACHINE && workable != null) {
            if (playerIn.isSneaking()) {
                workable.setWorkingEnabled(!isWorkingEnable);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    public boolean modeLeftClick(EntityPlayer entityPlayer, MODE mode, int slot) {
        IItemHandler itemHandler = this.getItemCapability();
        if (mode == MODE.ITEM && itemHandler != null) {
            if (itemHandler.getSlots() > slot && slot >= 0) {
                ItemStack itemStack;
                if (entityPlayer.isSneaking()) {
                    itemStack = itemHandler.extractItem(slot, 64, false);
                } else {
                    itemStack = itemHandler.extractItem(slot, 1, false);
                }
                if (itemStack.isEmpty() && itemHandler instanceof ItemHandlerProxy) {
                    IItemHandler insertHandler = ObfuscationReflectionHelper.getPrivateValue(ItemHandlerProxy.class, (ItemHandlerProxy) itemHandler, "insertHandler");
                    if (slot < insertHandler.getSlots()) {
                        if (entityPlayer.isSneaking()) {
                            itemStack = insertHandler.extractItem(slot, 64, false);
                        } else {
                            itemStack = insertHandler.extractItem(slot, 1, false);
                        }
                    }
                }
                if (!itemStack.isEmpty()) {
                    EntityItem entity = new EntityItem(entityPlayer.world, entityPlayer.posX + .5f, entityPlayer.posY + .3f, entityPlayer.posZ + .5f, itemStack);
                    entity.addVelocity(-entity.motionX, -entity.motionY, -entity.motionZ);
                    entityPlayer.world.spawnEntity(entity);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        WidgetGroup primaryGroup = new WidgetGroup(new Position(0, 10));
        primaryGroup.addWidget(new LabelWidget(10, 5, "metaitem.cover.digital.name", 0));
        ToggleButtonWidget[] buttons = new ToggleButtonWidget[5];
        buttons[0] = new ToggleButtonWidget(40, 20, 20, 20, ClientHandler.BUTTON_FLUID, () -> this.mode == MODE.FLUID, (pressed) -> {
            if (pressed) setMode(MODE.FLUID);
        }).setTooltipText("metaitem.cover.digital.mode.fluid");
        ;
        buttons[1] = new ToggleButtonWidget(60, 20, 20, 20, ClientHandler.BUTTON_ITEM, () -> this.mode == MODE.ITEM, (pressed) -> {
            if (pressed) setMode(MODE.ITEM);
        }).setTooltipText("metaitem.cover.digital.mode.item");
        ;
        buttons[2] = new ToggleButtonWidget(80, 20, 20, 20, ClientHandler.BUTTON_ENERGY, () -> this.mode == MODE.ENERGY, (pressed) -> {
            if (pressed) setMode(MODE.ENERGY);
        }).setTooltipText("metaitem.cover.digital.mode.energy");
        ;
        buttons[3] = new ToggleButtonWidget(100, 20, 20, 20, ClientHandler.BUTTON_MACHINE, () -> this.mode == MODE.MACHINE, (pressed) -> {
            if (pressed) setMode(MODE.MACHINE);
        }).setTooltipText("metaitem.cover.digital.mode.machine");
        ;
        buttons[4] = new ToggleButtonWidget(140, 20, 20, 20, ClientHandler.BUTTON_INTERFACE, () -> this.mode == MODE.PROXY, (pressed) -> {
            if (pressed) setMode(MODE.PROXY);
        }).setTooltipText("metaitem.cover.digital.mode.proxy");
        ;
        primaryGroup.addWidget(new LabelWidget(10, 25, "metaitem.cover.digital.title.mode", 0));
        primaryGroup.addWidget(buttons[0]);
        primaryGroup.addWidget(buttons[1]);
        primaryGroup.addWidget(buttons[2]);
        primaryGroup.addWidget(buttons[3]);
        primaryGroup.addWidget(buttons[4]);

        primaryGroup.addWidget(new LabelWidget(10, 50, "monitor.gui.title.slot", 0));
        primaryGroup.addWidget(new ClickButtonWidget(40, 45, 20, 20, "-1", (data) -> setMode(slot - (data.isShiftClick ? 10 : 1))));
        primaryGroup.addWidget(new ClickButtonWidget(140, 45, 20, 20, "+1", (data) -> setMode(slot + (data.isShiftClick ? 10 : 1))));
        primaryGroup.addWidget(new ImageWidget(60, 45, 80, 20, GuiTextures.DISPLAY));
        primaryGroup.addWidget(new SimpleTextWidget(100, 55, "", 16777215, () -> Integer.toString(this.slot)));

        primaryGroup.addWidget(new LabelWidget(10, 75, "metaitem.cover.digital.title.spin", 0));
        primaryGroup.addWidget(new ClickButtonWidget(40, 70, 20, 20, "R", (data) -> setMode(this.spin.rotateY())));
        primaryGroup.addWidget(new ImageWidget(60, 70, 80, 20, GuiTextures.DISPLAY));
        primaryGroup.addWidget(new SimpleTextWidget(100, 80, "", 16777215, () -> this.spin.toString()));
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND, 176, 202).widget(primaryGroup).bindPlayerInventory(player.inventory, GuiTextures.SLOT, 8, 120);
        return builder.build(this, player);
    }

    private void syncAllInfo() {
        if (mode == MODE.FLUID || (mode == MODE.PROXY && proxyMode[0] > 0)) {
            boolean syncFlag = false;
            IFluidHandler fluidHandler = this.getFluidCapability();
            if (fluidHandler != null) {
                IFluidTankProperties[] fluidTankProperties = fluidHandler.getTankProperties();
                if (fluidTankProperties.length != fluids.length) {
                    fluids = new FluidTankProperties[fluidTankProperties.length];
                    syncFlag = true;
                }
                List<Integer> toUpdate = new ArrayList<>();
                for (int i = 0; i < fluidTankProperties.length; i++) {
                    FluidStack content = fluidTankProperties[i].getContents();
                    if (fluids[i] == null || (content == null && fluids[i].getContents() != null) || (content != null && fluids[i].getContents() == null) ||
                            fluidTankProperties[i].getCapacity() != fluids[i].getCapacity() ||
                            fluidTankProperties[i].canDrain() != fluids[i].canDrain() ||
                            fluidTankProperties[i].canFill() != fluids[i].canFill()) {
                        syncFlag = true;
                        fluids[i] = new FluidTankProperties(content, fluidTankProperties[i].getCapacity(), fluidTankProperties[i].canFill(), fluidTankProperties[i].canDrain());
                        toUpdate.add(i);
                    } else if(content != null && (content.amount != fluids[i].getContents().amount || !content.isFluidEqual(fluids[i].getContents()))) {
                        syncFlag = true;
                        fluids[i] = new FluidTankProperties(content, fluidTankProperties[i].getCapacity(), fluidTankProperties[i].canFill(), fluidTankProperties[i].canDrain());
                        toUpdate.add(i);
                    }
                }
                if (syncFlag) writeUpdateData(2, packetBuffer->{
                    packetBuffer.writeVarInt(fluids.length);
                    packetBuffer.writeVarInt(toUpdate.size());
                    for (Integer index : toUpdate) {
                        packetBuffer.writeVarInt(index);
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Capacity", fluids[index].getCapacity());
                        if (fluids[index].getContents() != null) {
                            fluids[index].getContents().writeToNBT(nbt);
                        }
                        packetBuffer.writeCompoundTag(nbt);
                    }
                });
            }
        }
        if (mode == MODE.ITEM || (mode == MODE.PROXY && proxyMode[1] > 0)) {
            boolean syncFlag = false;
            IItemHandler itemHandler = this.getItemCapability();
            if(itemHandler != null) {
                int size = itemHandler.getSlots();
                if (this.slot < size) {
                    int maxStoredItems = itemHandler.getSlotLimit(this.slot);
                    if (maxStoredItems != maxItemCapability) {
                        maxItemCapability = maxStoredItems;
                        syncFlag = true;
                    }
                }
                List<Integer> toUpdate = new ArrayList<>();
                if (items.length != size) {
                    items = new ItemStack[size];
                    syncFlag = true;
                }
                for (int i = 0; i < size; i++) {
                    if (items[i] == null) {
                        items[i] = ItemStack.EMPTY;
                    }
                    ItemStack content = itemHandler.getStackInSlot(i);
                    if (!ItemStack.areItemStacksEqual(items[i], content)) {
                        syncFlag = true;
                        items[i] = content.copy();
                        toUpdate.add(i);
                    }
                }
                if (syncFlag) writeUpdateData(3, packetBuffer -> {
                    packetBuffer.writeVarInt(maxItemCapability);
                    packetBuffer.writeVarInt(items.length);
                    packetBuffer.writeVarInt(toUpdate.size());
                    for (Integer index : toUpdate) {
                        packetBuffer.writeVarInt(index);
                        packetBuffer.writeCompoundTag(fixItemStackSer(items[index]));
                    }
                });
            }
        }
        if (this.mode == MODE.ENERGY || (mode == MODE.PROXY && proxyMode[2] > 0)) {
            IEnergyContainer energyContainer = this.getEnergyCapability();
            if (energyContainer != null) {
                if (energyStored != energyContainer.getEnergyStored() || energyCapability != energyContainer.getEnergyCapacity()) {
                    energyStored = energyContainer.getEnergyStored();
                    energyCapability = energyContainer.getEnergyCapacity();
                    writeUpdateData(4, packetBuffer -> {
                        packetBuffer.writeLong(energyStored);
                        packetBuffer.writeLong(energyCapability);
                    });
                }
                if (this.coverHolder.getTimer() % 20 == 0) { //per second
                    writeUpdateData(5, packetBuffer -> {
                        packetBuffer.writeLong(energyInputPerDur);
                        packetBuffer.writeLong(energyOutputPerDur);
                        inputEnergyList.add(energyInputPerDur);
                        outputEnergyList.add(energyOutputPerDur);
                        if (inputEnergyList.size() > 13) {
                            inputEnergyList.remove(0);
                            outputEnergyList.remove(0);
                        }
                        energyInputPerDur = 0;
                        energyOutputPerDur = 0;
                    });
                }
            }
        }
        if (this.mode == MODE.MACHINE || (mode == MODE.PROXY && proxyMode[3] > 0)) {
            IWorkable workable = this.getMachineCapability();
            if (workable != null) {
                int progress = workable.getProgress();
                int maxProgress = workable.getMaxProgress();
                boolean isActive = workable.isActive();
                boolean isWorkingEnable = workable.isWorkingEnabled();
                if (isActive != this.isActive || isWorkingEnable != this.isWorkingEnable || this.progress != progress || this.maxProgress != maxProgress) {
                    this.progress = progress;
                    this.maxProgress = maxProgress;
                    this.isWorkingEnable = isWorkingEnable;
                    this.isActive = isActive;
                    writeUpdateData(6, packetBuffer -> {
                        packetBuffer.writeInt(progress);
                        packetBuffer.writeInt(maxProgress);
                        packetBuffer.writeBoolean(isActive);
                        packetBuffer.writeBoolean(isWorkingEnable);
                    });
                }
                if (this.coverHolder.getTimer() % 20 == 0) {
                    IEnergyContainer energyContainer = this.getEnergyCapability();
                    if (energyContainer != null) {
                        if (energyStored != energyContainer.getEnergyStored() || energyCapability != energyContainer.getEnergyCapacity()) {
                            energyStored = energyContainer.getEnergyStored();
                            energyCapability = energyContainer.getEnergyCapacity();
                            writeUpdateData(4, packetBuffer -> {
                                packetBuffer.writeLong(energyStored);
                                packetBuffer.writeLong(energyCapability);
                            });
                        }
                    }
                }
            }
        }
    }

    private void writeAllFluids(PacketBuffer packetBuffer) {
        packetBuffer.writeVarInt(fluids.length);
        packetBuffer.writeVarInt(fluids.length);
        for (int i = 0; i < fluids.length; i++) {
            packetBuffer.writeVarInt(i);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("Capacity", fluids[i].getCapacity());
            if (fluids[i].getContents() != null) {
                fluids[i].getContents().writeToNBT(nbt);
            }
            packetBuffer.writeCompoundTag(nbt);
        }
    }

    private void readFluids(PacketBuffer packetBuffer) {
        int size = packetBuffer.readVarInt();
        if (fluids == null || fluids.length != size) {
            fluids = new FluidTankProperties[size];
        }
        size = packetBuffer.readVarInt();
        try {
            for (int i = 0; i < size; i++) {
                int index = packetBuffer.readVarInt();
                NBTTagCompound nbt = packetBuffer.readCompoundTag();
                if (nbt != null) {
                    fluids[index] = new FluidTankProperties(FluidStack.loadFluidStackFromNBT(nbt), nbt.getInteger("Capacity"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAllItems(PacketBuffer packetBuffer) {
        packetBuffer.writeVarInt(maxItemCapability);
        packetBuffer.writeVarInt(items.length);
        packetBuffer.writeVarInt(items.length);
        for (int i = 0; i < items.length; i++) {
            packetBuffer.writeVarInt(i);
            packetBuffer.writeCompoundTag(fixItemStackSer(items[i]));
        }
    }

    private void readItems(PacketBuffer packetBuffer) {
        maxItemCapability = packetBuffer.readVarInt();
        int size = packetBuffer.readVarInt();
        if(items == null || items.length != size) {
            items = new ItemStack[size];
        }
        size = packetBuffer.readVarInt();
        try {
            for (int i = 0; i < size; i++) {
                int index = packetBuffer.readVarInt();
                NBTTagCompound nbt = packetBuffer.readCompoundTag();
                if (nbt != null) {
                    items[index] = new ItemStack(nbt);
                    items[index].setCount(nbt.getInteger("count"));
                } else {
                    items [index] = ItemStack.EMPTY;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NBTTagCompound fixItemStackSer(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.serializeNBT();
        nbt.setInteger("count", itemStack.getCount());
        return nbt;
    }

    public IFluidHandler getFluidCapability() {
        TileEntity te = getCoveredTE();
        IFluidHandler capability = te == null ? null : te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getCoveredFacing());
        if (capability == null && this.coverHolder instanceof MultiblockControllerBase) {
            List<IFluidTank> input = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.IMPORT_FLUIDS);
            List<IFluidTank> output = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.EXPORT_FLUIDS);
            List<IFluidTank> list = new ArrayList<>();
            if (input.size() > 0) {
                list.addAll(input);
            }
            if (output.size() > 0) {
                list.addAll(output);
            }
            capability = new FluidTankList(true, list);
        }
        return capability;
    }

    public IItemHandler getItemCapability() {
        TileEntity te = getCoveredTE();
        IItemHandler capability = te == null ? null : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getCoveredFacing());
        if (capability == null && this.coverHolder instanceof MultiblockControllerBase) {
            List<IItemHandlerModifiable> input = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.IMPORT_ITEMS);
            List<IItemHandlerModifiable> output = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.EXPORT_ITEMS);
            List<IItemHandlerModifiable> list = new ArrayList<>();
            if (input.size() > 0) {
                list.addAll(input);
            }
            if (output.size() > 0) {
                list.addAll(output);
            }
            capability = new ItemHandlerList(list);
        }
        return capability;
    }

    public IEnergyContainer getEnergyCapability() {
        TileEntity te = getCoveredTE();
        IEnergyContainer capability = te == null ? null : te.getCapability(GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER, getCoveredFacing());
        if (capability == null && this.coverHolder instanceof MultiblockControllerBase) {
            List<IEnergyContainer> input = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.INPUT_ENERGY);
            List<IEnergyContainer> output = ((MultiblockControllerBase) this.coverHolder).getAbilities(MultiblockAbility.OUTPUT_ENERGY);
            List<IEnergyContainer> list = new ArrayList<>();
            if (input.size() > 0) {
                list.addAll(input);
            }
            if (output.size() > 0) {
                list.addAll(output);
            }
            capability = new EnergyContainerList(list);
        } else if (capability == null) {
            IEnergyStorage fe = te.getCapability(CapabilityEnergy.ENERGY, getCoveredFacing());
            if(fe != null) {
                return new IEnergyContainer() {
                    public long acceptEnergyFromNetwork(EnumFacing enumFacing, long l, long l1) {
                        return 0;
                    }
                    public boolean inputsEnergy(EnumFacing enumFacing) {
                        return false;
                    }
                    public long changeEnergy(long l) {
                        return 0;
                    }
                    public long getEnergyStored() {
                        return fe.getEnergyStored() / 4;
                    }
                    public long getEnergyCapacity() {
                        return fe.getMaxEnergyStored() / 4;
                    }
                    public long getInputAmperage() {
                        return 0;
                    }
                    public long getInputVoltage() {
                        return 0;
                    }
                };
            }
        }
        return capability;
    }

    public IWorkable getMachineCapability() {
        TileEntity te = getCoveredTE();
        return te == null ? null : te.getCapability(GregtechTileCapabilities.CAPABILITY_WORKABLE, getCoveredFacing());
    }


    @Override
    public void readUpdateData(int id, PacketBuffer packetBuffer) {
        super.readUpdateData(id, packetBuffer);
        if (id == 1) { // set mode
            setMode(MODE.VALUES[packetBuffer.readByte()], packetBuffer.readInt(), EnumFacing.byIndex(packetBuffer.readByte()));
        } else if (id == 2) { // sync fluids
            readFluids(packetBuffer);
        } else if (id == 3) {
            readItems(packetBuffer);
        } else if (id == 4) {
            energyStored = packetBuffer.readLong();
            energyCapability = packetBuffer.readLong();
        } else if (id == 5) {
            energyInputPerDur = packetBuffer.readLong();
            energyOutputPerDur = packetBuffer.readLong();
            inputEnergyList.add(energyInputPerDur);
            outputEnergyList.add(energyOutputPerDur);
            if (inputEnergyList.size() > 13) {
                inputEnergyList.remove(0);
                outputEnergyList.remove(0);
            }
        } else if (id == 6) {
            this.progress = packetBuffer.readInt();
            this.maxProgress = packetBuffer.readInt();
            this.isActive = packetBuffer.readBoolean();
            boolean isWorkingEnable = packetBuffer.readBoolean();
            if (this.isWorkingEnable != isWorkingEnable && this.mode == MODE.MACHINE) {
                this.isWorkingEnable = isWorkingEnable;
                this.coverHolder.scheduleRenderUpdate();
            }
            this.isWorkingEnable = isWorkingEnable;
        }
    }

    @Override
    public boolean canPipePassThrough() {
        return true;
    }

    @Override
    public boolean canAttach() {
        return this.coverHolder instanceof PipeCoverableImplementation ||
                canCapabilityAttach();
    }

    public boolean canCapabilityAttach() {
        return  getFluidCapability() != null ||
                getItemCapability() != null ||
                getEnergyCapability() != null ||
                getMachineCapability() != null;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, T defaultValue) {
        if (this.mode == MODE.PROXY) {
            if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER && defaultValue == null) {
                return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(proxyCapability);
            }
        }
        return defaultValue;
    }

    @Override
    public void renderCoverPlate(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 plateBox, BlockRenderLayer layer) {
    }

    @Override
    public void renderCover(CCRenderState ccRenderState, Matrix4 translation, IVertexOperation[] ops, Cuboid6 cuboid6, BlockRenderLayer blockRenderLayer) {
        Rotation rotation = new Rotation(0, 0, 1, 0);
        if (this.attachedSide == EnumFacing.UP || this.attachedSide == EnumFacing.DOWN) {
            if (this.spin == EnumFacing.WEST) {
                translation.translate(0, 0, 1);
                rotation = new Rotation(Math.PI / 2, 0, 1, 0);
            } else if (this.spin == EnumFacing.EAST) {
                translation.translate(1, 0, 0);
                rotation = new Rotation(-Math.PI / 2, 0, 1, 0);
            } else if (this.spin == EnumFacing.SOUTH) {
                translation.translate(1, 0, 1);
                rotation = new Rotation(Math.PI, 0, 1, 0);
            }
            translation.apply(rotation);
        }
        if (mode == MODE.PROXY) {
            ClientHandler.COVER_INTERFACE_PROXY.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
        } else if (mode == MODE.FLUID) {
            ClientHandler.COVER_INTERFACE_FLUID.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
            ClientHandler.COVER_INTERFACE_FLUID_GLASS.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 3));
        } else if (mode == MODE.ITEM) {
            ClientHandler.COVER_INTERFACE_ITEM.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
        } else if (mode == MODE.ENERGY) {
            ClientHandler.COVER_INTERFACE_ENERGY.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
        } else if (mode == MODE.MACHINE) {
            if (isWorkingEnable) {
                ClientHandler.COVER_INTERFACE_MACHINE_ON.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
            } else {
                ClientHandler.COVER_INTERFACE_MACHINE_OFF.renderSided(this.attachedSide, cuboid6, ccRenderState, ArrayUtils.addAll(ops, rotation), RenderHelper.adjustTrans(translation, this.attachedSide, 1));
            }
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0 && this.mode != MODE.PROXY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderMetaTileEntityDynamic(double x, double y, double z, float partialTicks) {
        GlStateManager.pushMatrix();
        /* hack the lightmap */
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        RenderHelper.moveToFace(x, y, z, this.attachedSide);
        RenderHelper.rotateToFace(this.attachedSide, this.spin);

        if (!renderSneakingLookAt(this.coverHolder.getPos(), this.attachedSide, this.slot, partialTicks)) {
            renderMode(this.mode, this.slot, partialTicks);
        }

        /* restore the lightmap  */
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GL11.glPopAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean renderSneakingLookAt(BlockPos blockPos, EnumFacing side, int slot, float partialTicks) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null && player.isSneaking() && player.getHeldItemMainhand().isEmpty()) {
            RayTraceResult rayTraceResult = player.rayTrace(Minecraft.getMinecraft().playerController.getBlockReachDistance(), partialTicks);
            if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && rayTraceResult.sideHit == side && rayTraceResult.getBlockPos().equals(blockPos)) {
                RenderHelper.renderRect(-7f / 16, -7f / 16, 3f / 16, 3f / 16, 0.002f, 0XFF838583);
                RenderHelper.renderRect(4f / 16, -7f / 16, 3f / 16, 3f / 16, 0.002f, 0XFF838583);
                RenderHelper.renderText(-5.5f / 16, -5.5F / 16, 0, 1.0f / 70, 0XFFFFFFFF, "<", true);
                RenderHelper.renderText(5.7f / 16, -5.5F / 16, 0, 1.0f / 70, 0XFFFFFFFF, ">", true);
                RenderHelper.renderText(0, -5.5F / 16, 0, 1.0f / 120, 0XFFFFFFFF, "Slot: " + slot, true);
                TileEntity te = getCoveredTE();
                if (te != null) {
                    ItemStack itemStack;
                    if (te instanceof MetaTileEntityHolder) {
                        itemStack = ((MetaTileEntityHolder) te).getMetaTileEntity().getStackForm();
                    } else {
                        BlockPos pos = te.getPos();
                        itemStack = te.getBlockType().getPickBlock(te.getWorld().getBlockState(pos), new RayTraceResult(new Vec3d(0.5, 0.5, 0.5), getCoveredFacing(), pos), te.getWorld(), pos, Minecraft.getMinecraft().player);
                    }
                    String name = itemStack.getDisplayName();
                    RenderHelper.renderRect(-7f / 16, -4f / 16, 14f / 16, 1f / 16, 0.002f, 0XFF000000);
                    RenderHelper.renderText(0, -3.5F / 16, 0, 1.0f / 200, 0XFFFFFFFF, name, true);
                    RenderHelper.renderItemOverLay(-8f / 16, -5f / 16, 0.002f, 1f / 32, itemStack);
                }
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void renderMode(MODE mode, int slot, float partialTicks) {
        if (mode == MODE.FLUID && fluids.length > slot && slot >= 0 && fluids[slot] != null && fluids[slot].getContents() != null) {
            renderFluidMode(slot);
        } else if (mode == MODE.ITEM && items.length > slot && slot >= 0 && items[slot] != null) {
            renderItemMode(slot);
        } else if (mode == MODE.ENERGY) {
            renderEnergyMode();
        } else if (mode == MODE.MACHINE) {
            renderMachineMode(partialTicks);
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderMachineMode(float partialTicks) {
        int color = energyCapability > 10 * energyStored ? 0XFFFF2F39 : isWorkingEnable ? 0XFF00FF00 : 0XFFFF662E;
        if (isActive && maxProgress != 0) {
            float offset = ((this.coverHolder.getTimer() % 20 + partialTicks) * 0.875f / 20);
            float start = Math.max(-0.4375f, -0.875f + 2 * offset);
            float width = Math.min(0.4375f, -0.4375f + 2 * offset) - start;
            int startAlpha = 0X00;
            int endAlpha = 0XFF;
            if (offset < 0.4375f) {
                startAlpha = (int) (255 - 255 / 0.4375 * offset);
            } else if (start > 0.4375) {
                endAlpha = (int) (510 - 255 / 0.4375 * offset);
            }
            RenderHelper.renderRect(-7f / 16, -7f / 16, progress * 14f / (maxProgress * 16), 3f / 16, 0.002f, 0XFFFF5F44);
            RenderHelper.renderText(0, -5.5F / 16, 0, 1.0f / (isProxy() ? 110 : 70), 0XFFFFFFFF, readAmountOrCountOrEnergy(progress * 100 / maxProgress, MODE.MACHINE), true);
            RenderHelper.renderGradientRect(start, -4f / 16, width, 1f / 16, 0.002f, (color & 0X00FFFFFF) | (startAlpha << 24), (color & 0X00FFFFFF) | (endAlpha << 24), true);
        } else {
            RenderHelper.renderRect(-7f / 16, -4f / 16, 14f / 16, 1f / 16, 0.002f, color);
        }
        if (this.isProxy()) {
            if (isWorkingEnable) {
                RenderHelper.renderTextureArea(ClientHandler.COVER_INTERFACE_MACHINE_ON_PROXY, -7f / 16, 1f / 16, 14f / 16, 3f / 16, 0.002f);
            } else {
                RenderHelper.renderTextureArea(ClientHandler.COVER_INTERFACE_MACHINE_OFF_PROXY, -7f / 16, -1f / 16, 14f / 16, 5f / 16, 0.002f);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderEnergyMode() {
        if (inputEnergyList.isEmpty()) return;
        long max = Long.MIN_VALUE;
        for (long d : inputEnergyList) {
            max = Math.max(max, d);
        }
        for (long d : outputEnergyList) {
            max = Math.max(max, d);
        }

        RenderHelper.renderLineChart(inputEnergyList, max, -5.5f / 16, 5.5f / 16, 12f / 16, 6f / 16, 0.005f, 0XFF03FF00);
        RenderHelper.renderLineChart(outputEnergyList, max, -5.5f / 16, 5.5f / 16, 12f / 16, 6f / 16, 0.005f, 0XFFFF2F39);
        RenderHelper.renderText(-5.7f / 16, -2.3f / 16, 0, 1.0f / 270, 0XFF03FF00, "EU I: " + String.format(Locale.US, "%,d", energyInputPerDur/20).replace(',', ' ') + "EU/t", false);
        RenderHelper.renderText(-5.7f / 16, -1.6f / 16, 0, 1.0f / 270, 0XFFFF0000, "EU O: " + String.format(Locale.US, "%,d", energyOutputPerDur/20).replace(',', ' ') + "EU/t", false);
        RenderHelper.renderRect(-7f / 16, -7f / 16, energyStored * 14f / (energyCapability * 16), 3f / 16, 0.002f, 0XFFFFD817);
        RenderHelper.renderText(0, -5.5F / 16, 0, 1.0f / (isProxy() ? 110 : 70), 0XFFFFFFFF, readAmountOrCountOrEnergy(energyStored, MODE.ENERGY), true);
    }

    @SideOnly(Side.CLIENT)
    private void renderItemMode(int slot) {
        ItemStack itemStack = items[slot];
        if (!itemStack.isEmpty()) {
            RenderHelper.renderItemOverLay(-8f / 16, -5f / 16, 0, 1f / 32, itemStack);
            if (maxItemCapability != 0) {
                RenderHelper.renderRect(-7f / 16, -7f / 16, Math.max(itemStack.getCount() * 14f / (maxItemCapability * 16), 0.001f), 3f / 16, 0.002f, 0XFF25B9FF);
            } else {
                RenderHelper.renderRect(-7f / 16, -7f / 16, Math.max(itemStack.getCount() * 14f / (itemStack.getMaxStackSize() * 16), 0.001f), 3f / 16, 0.002f, 0XFF25B9FF);
            }
            RenderHelper.renderText(0, -5.5F / 16, 0, 1.0f / (isProxy() ? 110 : 70), 0XFFFFFFFF, readAmountOrCountOrEnergy(itemStack.getCount(), MODE.ITEM), true);

        }
    }

    @SideOnly(Side.CLIENT)
    private void renderFluidMode(int slot) {
        FluidStack fluidStack = fluids[slot].getContents();
        assert fluidStack != null;
        float height = 10f / 16 * Math.max(fluidStack.amount * 1.0f / fluids[slot].getCapacity(), 0.001f);
        RenderHelper.renderFluidOverLay(-7f / 16, 0.4375f - height, 14f / 16, height, 0.002f, fluidStack, 0.8f);
        int fluidColor = WidgetOreList.getFluidColor(fluidStack.getFluid());
        int textColor = ((fluidColor & 0xff) + ((fluidColor >> 8) & 0xff) + ((fluidColor >> 16) & 0xff)) / 3 > (255 / 2) ? 0X0 : 0XFFFFFFFF;
        RenderHelper.renderRect(-7f / 16, -7f / 16, 14f / 16, 3f / 16, 0.002f, fluidColor | (255 << 24));
        RenderHelper.renderText(0, -5.5F / 16, 0, 1.0f / (isProxy() ? 110 : 70), textColor, readAmountOrCountOrEnergy(fluidStack.amount, MODE.FLUID), true);
    }

    static String[][] units = {
            {"", "mB", "", "EU"},
            {"", "B", "K", "KEU"},
            {"", "KB", "M", "MEU"},
            {"", "MB", "G", "GEU"},
            {"", "GB", "T", "TEU"},
            {"", "TB", "P", "PEU"},
    };

    @SideOnly(Side.CLIENT)
    private String readAmountOrCountOrEnergy(long number, MODE mode) {
        int unit = mode == MODE.FLUID ? 1 : mode == MODE.ITEM ? 2 : mode == MODE.ENERGY ? 3 : 0;
        if (mode == MODE.MACHINE) {
            return number + "%";
        }

        if (number / 1000 == 0) {
            return number + units[0][unit];
        }
        int i = 1;

        while (number / 10000000 != 0 && i < units.length) {
            number = number / 1000;
            ++i;
        }

        return new DecimalFormat("#.#").format(number * 1.0f / 1000) + units[i][unit];
    }
}
