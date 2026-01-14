package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing2;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.components.ConveyorCasing;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockTurbineCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeEngraverInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_LASER_ENGRAVER;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        for (int tier = 0; tier < 15; tier++) {
            GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                    .aisle("XXX", "XXX","XXX","#T#");
            for (int j = -3; j < Math.min(3, tier); j++) {
                builder.aisle("IXX", "GCG","XEX","#T#");
            }
            shapeInfos.add(builder.aisle("eHX", "XSX","iOo","#T#")
                    .where('e', GATileEntities.getEnergyHatch(tier, false), EnumFacing.NORTH)
                    .where('S', GATileEntities.LARGE_LASER_ENGRAVER, EnumFacing.WEST)
                    .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                    .where('X', GAMetaBlocks.MUTLIBLOCK_CASING2.getState(GAMultiblockCasing2.CasingType.LASER_ENGRAVER))
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('E', GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.IRIDIUM_GLASS))
                    .where('T', MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_GEARBOX))
                    .where('C', GAMetaBlocks.CONVEYOR_CASING.getState(ConveyorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_laser_engraver.description")};
    }

    private static final ITextComponent componentCasingTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();

        ITextComponent casingTooltip = new TextComponentTranslation("gregtech.multiblock.preview.limit", 18).setStyle(new Style().setColor(TextFormatting.RED));

        this.addBlockTooltip(GAMetaBlocks.MUTLIBLOCK_CASING2.getItemVariant(GAMultiblockCasing2.CasingType.LASER_ENGRAVER), casingTooltip);

        for (EmitterCasing.CasingType casingType : EmitterCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.EMITTER_CASING.getItemVariant(casingType), componentCasingTooltip);
        }
        for (ConveyorCasing.CasingType casingType : ConveyorCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.CONVEYOR_CASING.getItemVariant(casingType), componentCasingTooltip);
        }
    }
}
