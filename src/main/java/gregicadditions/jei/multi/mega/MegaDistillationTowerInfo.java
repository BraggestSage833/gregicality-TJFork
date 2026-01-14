package gregicadditions.jei.multi.mega;

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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, LEFT, UP)
                .aisle("#XXX#", "XXXXX", "XXXXX", "OXXXH", "#FSM#");
        for (int i = 0; i < 11; i++) {
            builder.aisle("#XXX#", "XCpCX", "XpPpX", "XCpCX", "#XEX#");
        }
        builder.aisle("#XXX#", "XXXXX", "XXXXX", "XXXXX", "#XEX#")
                .where('S', GATileEntities.MEGA_DISTILLATION_TOWER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
                .where('C', MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.NICHROME))
                .where('p', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('H', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('E', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('P', GAMetaBlocks.getFramework(tier))
                    .build());
        }
        return shapeInfos;
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
