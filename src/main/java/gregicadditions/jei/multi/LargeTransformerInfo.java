package gregicadditions.jei.multi;

import gregicadditions.GAConfig;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeTransformerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_TRANSFORMER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("ISO")
                .where('S', GATileEntities.LARGE_TRANSFORMER, EnumFacing.WEST)
                .where('O', PlaceholderType.ENERGY_OUTPUT_HATCH, GATileEntities.getEnergyHatch(0, true), EnumFacing.SOUTH)
                .where('I', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.NORTH)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_transformer.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 1.4f;
    }
}
