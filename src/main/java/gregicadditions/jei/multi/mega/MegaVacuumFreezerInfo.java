package gregicadditions.jei.multi.mega;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.*;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#HXMIO#", "#XXSfE#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#")
                .aisle("XXXXXXX", "XP#F#PX", "XP###PX", "XPPPPPX", "XP###PX", "XP#F#PX", "XXXXXXX");
        for (int i = 0; i < 3; i++) {
            builder.aisle("XXXXXXX", "X#####X", "X#####X", "XPGGGPX", "X#####X", "X#####X", "XXXXXXX");
        }
        builder.aisle("XXXXXXX", "XP#F#PX", "XP###PX", "XPPPPPX", "XP###PX", "XP#F#PX", "XXXXXXX")
                .aisle("#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#", "#XXXXX#")
                .where('S', GATileEntities.MEGA_VACUUM_FREEZER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.ALUMINIUM_FROSTPROOF))
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('H', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('E', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('f', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', GAMetaBlocks.getFramework(tier))
                    .build());
        }
        return shapeInfos;
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
