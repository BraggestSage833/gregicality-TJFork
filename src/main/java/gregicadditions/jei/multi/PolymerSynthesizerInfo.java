package gregicadditions.jei.multi;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class PolymerSynthesizerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.POLY_SYN;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder()
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")
                .aisle("U  F  U", " CoSIi ", "       ", "       ", "       ", " CCmCC ", "U  F  U")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("UFFTFFU", " C H C ", "  PHP  ", "   H   ", "  PHP  ", " C H C ", "UFFTFFU")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("U  F  U", " CCqCC ", "       ", "       ", "       ", " CCCCC ", "U  F  U")
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")
                .where('S', getController(), EnumFacing.NORTH)
				.where('C' , (GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)))
				.where('U' ,(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)))
				.where('F' ,(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
				.where('P' , (GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
                .where('m' , GATileEntities.MAINTENANCE_HATCH[1], EnumFacing.NORTH)
                .where(' ', Blocks.AIR.getDefaultState());
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder
                    .where('q', GATileEntities.getEnergyHatch(tier, false), EnumFacing.SOUTH)
                    .where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('H', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('T', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.polymer.synth.description")};
    }



    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}

