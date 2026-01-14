package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
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

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;


public class LargeCircuitAssemblyLineInfo extends MultiblockInfoPage {

    private static final ITextComponent defaultText = new TextComponentTranslation("gregtech.multiblock.preview.any_hatch").setStyle(new Style().setColor(TextFormatting.GREEN));

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_CIRCUIT_ASSEMBLY_LINE;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT);
        builder.aisle("COC", "RTR", "GYG");
        for (int num = 0; num < 5; num++) {
            builder.aisle("CIC", "RTR", "GAG");
        }
        builder.aisle("FIM", "RTR", "GSG")
                .where('S', GATileEntities.LARGE_CIRCUIT_ASSEMBLY_LINE, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)
                .where('I', MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.DOWN)
                .where('C', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
                .where('A', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ASSEMBLER_CASING))
                .where('R', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.REINFORCED_GLASS));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('Y', GATileEntities.getEnergyHatch(tier, false), EnumFacing.NORTH)
                    .where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.DOWN)
                    .where('T', GAMetaBlocks.getFramework(Math.max(5, tier)))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.large_circuit_assembly.description")};
    }

    @Override
    protected void generateBlockTooltips() {

        ItemStack itemStack = MetaTileEntities.ITEM_IMPORT_BUS[0].getStackForm();

        ITextComponent tooltip = new TextComponentTranslation("gregtech.multiblock.preview.only", itemStack.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED));
        for(int i = 0; i < GTValues.V.length; ++i) {
            this.addBlockTooltip(MetaTileEntities.ITEM_EXPORT_BUS[i].getStackForm(), defaultText);
            this.addBlockTooltip(MetaTileEntities.ITEM_IMPORT_BUS[i].getStackForm(), tooltip);
            this.addBlockTooltip(MetaTileEntities.FLUID_EXPORT_HATCH[i].getStackForm(), defaultText);
            this.addBlockTooltip(MetaTileEntities.FLUID_IMPORT_HATCH[i].getStackForm(), defaultText);
        }
    }


    @Override
    public float getDefaultZoom() {
        return 0.9f;
    }

}