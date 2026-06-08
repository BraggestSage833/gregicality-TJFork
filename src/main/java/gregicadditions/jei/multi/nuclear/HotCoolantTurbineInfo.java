package gregicadditions.jei.multi.nuclear;

import gregicadditions.GAConfig;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.impl.MetaTileEntityRotorHolderForNuclearCoolant;
import gregicadditions.machines.multi.nuclear.MetaTileEntityHotCoolantTurbine;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.BlockInfo;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.items.MetaItems;
import gregtech.common.items.behaviors.TurbineRotorBehavior;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class HotCoolantTurbineInfo extends MultiblockInfoPage {

    public final MetaTileEntityHotCoolantTurbine turbine;

    public HotCoolantTurbineInfo(MetaTileEntityHotCoolantTurbine turbine) {
        this.turbine = turbine;
    }

    @Override
    public MultiblockControllerBase getController() {
        return turbine;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        MetaTileEntityHolder holder = new MetaTileEntityHolder();
        holder.setMetaTileEntity(GATileEntities.ROTOR_HOLDER[2]);
        holder.getMetaTileEntity().setFrontFacing(EnumFacing.WEST);

        ItemStack rotorStack = MetaItems.TURBINE_ROTOR.getStackForm();
        TurbineRotorBehavior.getInstanceFor(rotorStack)
                .setPartMaterial(rotorStack, Materials.Darmstadtium);

        ((MetaTileEntityRotorHolderForNuclearCoolant) holder.getMetaTileEntity())
                .getRotorInventory()
                .setStackInSlot(0, rotorStack);

        return MultiblockShapeInfo.builder()
                .aisle("CCCC", "CIOC", "CCCC")
                .aisle("CCCC", "R##D", "CCCC")
                .aisle("CCCC", "CSMC", "CCCC")
                .where('S', turbine, EnumFacing.SOUTH)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)
                .where('C', turbine.turbineType.casingState)

                // rotor holder (BlockInfo mukana)
                .where('R', new BlockInfo(
                        MetaBlocks.MACHINE.getDefaultState(),
                        holder,
                        null
                ))

                .where('D', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, true), EnumFacing.EAST)
                .where('I', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.NORTH)
                .where('O', PlaceholderType.OUTPUT_HATCH,MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)

                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.machine.hot_coolant_turbine.description")};
    }

}