package gregicadditions.jei.multi.mega;

import gregicadditions.GAConfig;
import gregicadditions.channels.GAChannelRegistry;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.*;
import gregtech.common.channels.ChannelRegistry;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class MegaVacuumFreezerInfo extends MultiblockInfoPage {
    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.MEGA_VACUUM_FREEZER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {

        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#HXMIO#", "#XXSfE#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#")
                .aisle("XXXXXXX", "XP#F#PX", "XP###PX", "XPPPPPX", "XP###PX", "XP#F#PX", "XXXXXXX");
        for (int i = 0; i < 3; i++) {
            builder.aisle("XXXXXXX", "X#####X", "X#####X", "XPGGGPX", "X#####X", "X#####X", "XXXXXXX");
        }
        return builder.aisle("XXXXXXX", "XP#F#PX", "XP###PX", "XPPPPPX", "XP###PX", "XP#F#PX", "XXXXXXX")
                .aisle("#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#")
                .where('S', GATileEntities.MEGA_VACUUM_FREEZER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.ALUMINIUM_FROSTPROOF))
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))

                .where('H', ChannelRegistry.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
                .where('I', ChannelRegistry.INPUT_BUS,MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('E', ChannelRegistry.OUTPUT_HATCH,MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
                .where('O', ChannelRegistry.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
                .where('f', ChannelRegistry.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
                .where('F', GAChannelRegistry.FRAMEWORK,GAMetaBlocks.getFramework(0))
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.mega_vacuum_freezer.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.4f;
    }
}
