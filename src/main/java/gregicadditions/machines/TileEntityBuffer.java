package gregicadditions.machines;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregicadditions.client.ClientHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiAbilityProvider;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.render.SimpleOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.common.gui.widget.GhostCircuitWidget;
import gregtech.common.metatileentities.electric.multiblockpart.MetaTileEntityMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class TileEntityBuffer extends MetaTileEntityMultiblockPart implements ITieredMetaTileEntity, IMultiAbilityProvider {

    private final int tier;
    protected FluidTankList fluids;
    private ItemStackHandler inventory;
    private ItemStackHandler circuitInventory;
    private IItemHandlerModifiable combinedInventory;
   // private static final double[] rotations = new double[]{180.0, 0.0, -90.0, 90.0};


    public TileEntityBuffer(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId,tier);
        this.tier = tier;
        initializeInventory();
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        FluidTank[] tanks = new FluidTank[this.tier];
        for (int i = 0; i < this.tier; i++) {
            tanks[i] = new FluidTank(8000 * (1 << getTier()));
        }
        this.fluids = new FluidTankList(false, tanks);
        this.fluidInventory = fluids;
        //this.importFluids = new FluidTankList(false, fluids);
        //this.exportFluids = new FluidTankList(false, fluids);
        //this.fluidInventory = new FluidHandlerProxy(fluids, fluids);
        this.circuitInventory = new ItemStackHandler(1);
        this.inventory = new ItemStackHandler(tier * tier);
        this.combinedInventory = new ItemHandlerList(Arrays.asList(this.circuitInventory, this.inventory));
        this.itemInventory = inventory;
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new TileEntityBuffer(metaTileEntityId, tier);
    }

    @Override
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(ClientHandler.VOLTAGE_CASINGS[this.tier].getParticleSprite(), this.getPaintingColor());
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BACKGROUND,
                176, Math.max(166, 18 + 18 * tier + 94));//176, 166
        for (int i = 0; i < this.fluids.getTanks(); i++) {
            builder.widget(new TankWidget(this.fluids.getTankAt(i), 176 - 8 - 18, 18 + 18 * i, 18, 18)
                    .setAlwaysShowFull(true)
                    .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                    .setContainerClicking(true, true));
        }
        for (int y = 0; y < tier; y++) {
            for (int x = 0; x < tier; x++) {
                int index = y * tier + x;
                builder.slot(inventory, index, 8 + x * 18, 18 + y * 18, GuiTextures.SLOT);
            }
        }
        return builder.label(6, 6, getMetaFullName())
                .widget(new GhostCircuitWidget(this.circuitInventory, 7 + (this.tier * 18), 54 + (18 * Math.max(0, this.tier - 3)))
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.INT_CIRCUIT_OVERLAY))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 8, 18 + 18 * tier + 12)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (shouldRenderOverlay()) {
            SimpleOverlayRenderer renderer = Textures.PIPE_IN_OVERLAY;
            renderer.renderSided(getFrontFacing(), renderState, translation, pipeline);
            renderState.reset();
            SimpleOverlayRenderer renderer2 = Textures.BUFFER_INPUT_OVERLAY;
            renderer2.renderSided(getFrontFacing(), renderState, translation, pipeline);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("Inventory", inventory.serializeNBT());
        data.setTag("GhostCircuit", circuitInventory.serializeNBT());
        data.setTag("FluidInventory", fluids.serializeNBT());
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.deserializeNBT(data.getCompoundTag("Inventory"));
        this.circuitInventory.deserializeNBT(data.getCompoundTag("GhostCircuit"));
        this.fluids.deserializeNBT(data.getCompoundTag("FluidInventory"));
    }

    @Override
    protected boolean shouldSerializeInventories() {
        return false;
    }
    @Override
    public MultiblockAbility<?>[] getAbilities() {
        return new MultiblockAbility<?>[]{
                MultiblockAbility.IMPORT_ITEMS,
                MultiblockAbility.IMPORT_FLUIDS
        };
    }

    @Override
    public void registerAbilityFor(MultiblockAbility<?> ability, List<Object> list) {
        if (ability == MultiblockAbility.IMPORT_ITEMS) {
            list.add(this.combinedInventory);
        }

        if (ability == MultiblockAbility.IMPORT_FLUIDS) {
            list.addAll(fluids.getFluidTanks());
        }

    }
    @Override
    public void onRemoval() {
        super.onRemoval();
        if (!getWorld().isRemote) {
            for (int i = 0; i < itemInventory.getSlots(); i++) {
                ItemStack stack = itemInventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Block.spawnAsEntity(getWorld(), getPos(), stack.copy());
                    //itemInventory.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.universal.tooltip.item_storage_capacity", (tier * tier)));
        tooltip.add(I18n.format("gregtech.universal.tooltip.fluid_storage_capacity", (8000 * (1 << getTier()))));
        tooltip.add(I18n.format("gtadditions.machine.multi_fluid_hatch_universal.tooltip.2", (int) fluids.getTanks()));
        tooltip.add(I18n.format("gregtech.universal.enabled"));
        tooltip.add(I18n.format("gregtech.universal.no_import"));



    }

}