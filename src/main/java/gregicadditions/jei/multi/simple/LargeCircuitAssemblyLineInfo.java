package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
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
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
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
    public MultiblockShapeInfo getMatchingShapes() {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("COC", "RTR", "GYG")
                .aisle("CIC", "RTR", "GAG")
                .aisle("CIC", "RTR", "GAG")
                .aisle("CIC", "RTR", "GAG")
                .aisle("CIC", "RTR", "GAG")
                .aisle("CIC", "RTR", "GAG")
                .aisle("FIM", "RTR", "GSG")

                .where('S', GATileEntities.LARGE_CIRCUIT_ASSEMBLY_LINE, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)

                .where('I', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.DOWN)

                .where('C', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
                .where('A', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ASSEMBLER_CASING))
                .where('R', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.REINFORCED_GLASS))

                .where('Y', PlaceholderType.ENERGY_INPUT_HATCH,
                        GATileEntities.getEnergyHatch(0, false), EnumFacing.NORTH)

                .where('F', PlaceholderType.INPUT_HATCH,
                        MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.NORTH)

                .where('O', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.DOWN)

                .where('T', GAMetaBlocks.getFramework(5))
                .build();
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