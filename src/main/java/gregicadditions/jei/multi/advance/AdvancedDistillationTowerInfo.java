package gregicadditions.jei.multi.advance;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.advance.MetaTileEntityAdvancedDistillationTower;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class AdvancedDistillationTowerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.ADVANCED_DISTILLATION_TOWER;
    }


    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return MultiblockShapeInfo.builder()
                .aisle("CXX", "XXX", "XXX", "XXX", "XXX", "XXX")
                .aisle("SFX", "MPX", "XPX", "XPX", "XPX", "XXX")
                .aisle("IEX", "HXX", "HXX", "HXX", "HXX", "HXX")
                .where('#', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.TIERED_HULL_IV))
                .where('X', MetaTileEntityAdvancedDistillationTower.casingState)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('S', GATileEntities.ADVANCED_DISTILLATION_TOWER, EnumFacing.WEST)
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TITANIUM_PIPE))

                .where('E', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.SOUTH)
                .where('C', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('I', PlaceholderType.OUTPUT_BUS, MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
                .where('F', PlaceholderType.INPUT_HATCH, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.DOWN)
                .where('H', PlaceholderType.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
                .build();
    }



    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.advanced_distillation_tower.description1")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.7f;
    }
}
