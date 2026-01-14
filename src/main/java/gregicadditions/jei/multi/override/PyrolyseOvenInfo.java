package gregicadditions.jei.multi.override;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMachineCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("XMX", "ISF", "XXX")
                .aisle("CCC", "C#C", "CCC")
                .aisle("CCC", "C#C", "CCC")
                .aisle("XXX", "BEH", "XXX")
                .where('S', GATileEntities.PYROLYSE_OVEN, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.MACHINE_CASING.getState(BlockMachineCasing.MachineCasingType.ULV));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('B', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.EAST)
                    .where('H', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.EAST)
                    .where('C', GAMetaBlocks.getCoils(tier))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.pyrolyze_oven.description")};
    }

}
