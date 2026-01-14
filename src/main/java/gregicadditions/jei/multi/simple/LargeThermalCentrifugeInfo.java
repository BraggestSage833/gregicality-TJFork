package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.CasingUtils;
import gregicadditions.machines.multi.simple.TileEntityLargeThermalCentrifuge;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;

public class LargeThermalCentrifugeInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_THERMAL_CENTRIFUGE;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("#XXX#", "#XGX#", "#XXX#", "#####")
                .aisle("XXXXX", "XCCCX", "I###X", "#XXX#")
                .aisle("HXMXX", "SCPCG", "E#P#X", "#XmX#")
                .aisle("XXXXX", "XCCCX", "O###X", "#XXX#")
                .aisle("#XXX#", "#XGX#", "#XXX#", "#####")
                .where('S', GATileEntities.LARGE_THERMAL_CENTRIFUGE, EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', TileEntityLargeThermalCentrifuge.casingState)
                .where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
                .where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TITANIUM_PIPE))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('C', GAMetaBlocks.getCoils(tier))
                    .build());
        }
        return shapeInfos;
    }

    private static final ITextComponent componentCasingTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();

        ITextComponent casingTooltip1 = new TextComponentTranslation("gregtech.multiblock.preview.limit", 12).setStyle(new Style().setColor(TextFormatting.RED));
        ITextComponent casingTooltip2 = new TextComponentTranslation("gregtech.multiblock.preview.limit", 12).setStyle(new Style().setColor(TextFormatting.RED));

        ItemStack defaultCasingStack = METAL_CASING_2.getItemVariant(MetalCasing2.CasingType.RED_STEEL);
        ItemStack casingStack = CasingUtils.getConfigCasingItemStack(GAConfig.multis.largeThermalCentrifuge.casingMaterial, defaultCasingStack);

        this.addBlockTooltip(casingStack, casingTooltip1);

        for (MotorCasing.CasingType casingType : MotorCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.MOTOR_CASING.getItemVariant(casingType), componentCasingTooltip);
        }
        this.addBlockTooltip(MetaBlocks.MUTLIBLOCK_CASING.getItemVariant(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING), casingTooltip2);
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_thermal_centrifuge.description")};
    }
}
