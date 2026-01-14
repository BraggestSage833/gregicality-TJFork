package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeBrewery;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeBreweryInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_BREWERY;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        ArrayList<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#IHi#", "#XSX#", "#XGX#", "#OXo#", "#####")
                .aisle("XXXXX", "X###X", "X###X", "X###X", "XXXXX")
                .aisle("XXMXX", "X#p#X", "P#p#P", "X#p#X", "XXmXX")
                .aisle("XXXXX", "X###X", "X###X", "X###X", "XXXXX")
                .aisle("#XEX#", "#XGX#", "#XGX#", "#XXX#", "#####")
                .where('S', GATileEntities.LARGE_BREWERY, EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', TileEntityLargeBrewery.casingState)
                .where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
                .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.BOROSILICATE_GLASS))
                .where('p', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('P', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_brewery.description")};
    }
}
