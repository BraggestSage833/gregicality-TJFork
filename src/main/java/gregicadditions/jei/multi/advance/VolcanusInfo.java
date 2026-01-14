package gregicadditions.jei.multi.advance;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class VolcanusInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.VOLCANUS;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
         MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("IFX", "CCC", "CCC", "XXX")
                .aisle("SXE", "C#C", "C#C", "MHX")
                .aisle("ODX", "CCC", "CCC", "XXX")
                .where('X', GAMetaBlocks.METAL_CASING_1.getState(MetalCasing1.CasingType.HASTELLOY_N))
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('H', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
                .where('S', GATileEntities.VOLCANUS, EnumFacing.WEST);
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('D', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.SOUTH)
                    .where('C', GAMetaBlocks.getCoils(tier))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.volcanus.description")};
    }
}