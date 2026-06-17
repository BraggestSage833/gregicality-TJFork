package gregicadditions.jei.multi.mega;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockWireCoil;
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

public class MegaDistillationTowerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.MEGA_DISTILLATION_TOWER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, LEFT, UP)
                .aisle("#XXX#", "XXXXX", "XXXXX", "OXXXH", "#FSM#");
        for (int i = 0; i < 11; i++) {
            builder.aisle("#XXX#", "XCpCX", "XpPpX", "XCpCX", "#XEX#");
        }
        return builder.aisle("#XXX#", "XXXXX", "XXXXX", "XXXXX", "#XEX#")
                .where('S', GATileEntities.MEGA_DISTILLATION_TOWER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
                .where('C',MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.NICHROME))
                .where('p', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
                .where('H', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
                .where('E', PlaceholderType.OUTPUT_HATCH,MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
                .where('O', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
                .where('F', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
                .where('P', PlaceholderType.FRAMEWORK,GAMetaBlocks.getFramework(0))
                .build();

    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.mega_distillation_tower.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.4f;
    }
}
