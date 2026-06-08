package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeCanningMachine;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeCanningMachineInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_CANNING_MACHINE;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("XHX", "XEX", "XXX")
                .aisle("IXi", "XpX", "OXo")
                .aisle("IXi", "XpX", "OXo")
                .aisle("IXi", "XpX", "OXo")
                .aisle("IXi", "XpX", "OXo")
                .aisle("IXi", "XpX", "OXo")
                .aisle("IXi", "XpX", "OXo")

                .aisle("PPP", "PSP", "PPP")

                .where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('S', GATileEntities.LARGE_CANNING_MACHINE, EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.EAST)
                .where('X', TileEntityLargeCanningMachine.casingState)

                .where('I', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)

                .where('O', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)

                .where('i', PlaceholderType.INPUT_HATCH,
                        MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.SOUTH)

                .where('o', PlaceholderType.OUTPUT_HATCH,
                        MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.SOUTH)

                .where('p', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[0]))
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE))
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_canning_machine.description")};
    }
}
