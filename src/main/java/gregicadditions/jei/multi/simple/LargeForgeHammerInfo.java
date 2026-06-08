package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeForgeHammer;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeForgeHammerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_FORGE_HAMMER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("SXO", "M#E", "XpI", "XXF")

                .where('S', GATileEntities.LARGE_FORGE_HAMMER, EnumFacing.WEST)
                .where('X', TileEntityLargeForgeHammer.casingState)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)

                .where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.SOUTH)

                .where('I', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.SOUTH)

                .where('F', PlaceholderType.INPUT_HATCH,
                        MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.SOUTH)

                .where('O', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.SOUTH)

                .where('p', GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[0]))
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.large_forge_hammer.description")};
    }
}
