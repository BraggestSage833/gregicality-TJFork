package gregicadditions.jei.multi.advance;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
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
import static gregtech.api.unification.material.Materials.Naquadria;


public class LargeNaquadahReactorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_NAQUADAH_REACTOR;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#CMC#", "#CSC#", "#CCC#", "##C##", "##C##", "#CCC#", "#CGC#", "#CCC#")
                .aisle("CCCCC", "CPAPC", "CgAgC", "#PAP#", "#PAP#", "CgAgC", "CPAPC", "#CCC#")
                .aisle("CCCCf", "GAFAG", "CAFAC", "CAFAC", "CAFAC", "CAFAC", "GAFAG", "#CCC#")
                .aisle("CCCCC", "CPAPC", "CgAgC", "#PAP#", "#PAP#", "CgAgC", "CPAPC", "#CCC#")
                .aisle("#CEC#", "#CGC#", "#CCC#", "##C##", "##C##", "#CCC#", "#CGC#", "#CCC#")
                .where('S', GATileEntities.LARGE_NAQUADAH_REACTOR, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[2], EnumFacing.WEST)
                .where('C', GAMetaBlocks.METAL_CASING_2.getState(MetalCasing2.CasingType.NAQUADRIA))
                .where('Y', MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.FUSION_COIL))
                .where('F', MetaBlocks.FRAMES.get(Naquadria).getDefaultState())
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
                .where('g', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.TUNGSTENSTEEL_GEARBOX_CASING))
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
                .where('T', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.TIERED_HULL_UV));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, true), EnumFacing.EAST)
                    .where('f', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.SOUTH)
                    .build());
        }
        return shapeInfos;

    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_naquadah_reactor.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}
