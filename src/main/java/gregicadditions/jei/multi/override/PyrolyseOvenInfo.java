package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMachineCasing;
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

public class PyrolyseOvenInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.PYROLYSE_OVEN;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("XMX", "ISF", "XXX")
                .aisle("CCC", "C#C", "CCC")
                .aisle("CCC", "C#C", "CCC")
                .aisle("XXX", "BEH", "XXX")
                .where('S', GATileEntities.PYROLYSE_OVEN, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.MACHINE_CASING.getState(BlockMachineCasing.MachineCasingType.ULV))

                .where('E', PlaceholderType.ENERGY_INPUT_HATCH,
                        GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)

                .where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('F', PlaceholderType.INPUT_HATCH, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

                .where('B', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.EAST)

                .where('H', PlaceholderType.OUTPUT_HATCH,
                        MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.EAST)

                .where('C', PlaceholderType.COIL)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.pyrolyze_oven.description")};
    }

}
