package gregicadditions.jei.multi.nuclear;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.metal.NuclearCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.nuclear.MetaTileEntityNuclearReactor;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class NuclearReactorInfo extends MultiblockInfoPage {

    public final MetaTileEntityNuclearReactor reactor;

    public NuclearReactorInfo(MetaTileEntityNuclearReactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public MultiblockControllerBase getController() {
        return reactor;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("MEY", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "YYY")
                .aisle("YYY", "XRX", "XRX", "XRX", "XRX", "XRX", "XRX", "XRX", "YYY")
                .aisle("ISO", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "ZXZ", "FYG")
                .where('S', reactor, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.EAST)
                .where('Y', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CLADDED_REACTOR_CASING))
                .where('Z', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CLADDED_REACTOR_CASING))
                .where('X', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.BOROSILICATE_GLASS))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('I', PlaceholderType.INPUT_BUS,MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('O', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
                .where('F', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
                .where('G', PlaceholderType.OUTPUT_HATCH ,MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
                .where('R', PlaceholderType.NUCLEAR_CASING ,GAMetaBlocks.NUCLEAR_CASING.getState(NuclearCasing.CasingType.values()[Math.min(11, 0)]))
                .build();

    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.reactor.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.4f;
    }

}
