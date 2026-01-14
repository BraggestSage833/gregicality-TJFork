package gregicadditions.jei;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.GATransparentCasing;
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

public class AssemblyLineInfo extends MultiblockInfoPage {

	private static final ITextComponent defaultText = new TextComponentTranslation("gregtech.multiblock.preview.any_hatch").setStyle(new Style().setColor(TextFormatting.GREEN));

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.ASSEMBLY_LINE;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		for (int tier = 0; tier < 15; tier++) {
			GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT);
			builder.aisle("FIM", "RTR", "GSG", "#Q#");
			for (int num = -3; num < Math.min(12, tier); num++) {
				if (num == 1 || num == 6 || num == 11) builder.aisle("FIC", "RTR", "GAG", "#Y#");
				else builder.aisle("CIC", "RTR", "GAG", "#C#");
			}
			shapeInfos.add(builder.aisle("COC", "RTR", "GAG", "#Y#")
					.where('S', GATileEntities.ASSEMBLY_LINE, EnumFacing.WEST)
					.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
					.where('C', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
					.where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.SOUTH)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.DOWN)
					.where('Y', GATileEntities.getEnergyHatch(tier, false), EnumFacing.UP)
					.where('Q', GATileEntities.QBIT_INPUT_HATCH[0], EnumFacing.UP)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.DOWN)
					.where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
					.where('A', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ASSEMBLER_CASING))
					.where('R', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.REINFORCED_GLASS))
					.where('T', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.ASSEMBLY_LINE_CASING))
					.build());
		}
		return shapeInfos;
	}

	@Override
	public String[] getDescription() {
		return new String[] { I18n.format("gregtech.multiblock.assembly_line.description") };
	}

	@Override
	protected void generateBlockTooltips() {

		ItemStack itemStack = MetaTileEntities.ITEM_IMPORT_BUS[0].getStackForm();

		ITextComponent tooltip = new TextComponentTranslation("gregtech.multiblock.preview.only", itemStack.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED));

		ITextComponent outputTooltip = new TextComponentTranslation(
				"gregtech.multiblock.preview.only_location",
				new TextComponentTranslation("gtadditions.multiblock.preview.location_end"))
				.setStyle(new Style().setColor(TextFormatting.RED));

		for(int i = 0; i < GTValues.V.length; ++i) {
			this.addBlockTooltip(MetaTileEntities.ITEM_EXPORT_BUS[i].getStackForm(), defaultText);
			this.addBlockTooltip(MetaTileEntities.ITEM_EXPORT_BUS[i].getStackForm(), outputTooltip);
			this.addBlockTooltip(MetaTileEntities.ITEM_IMPORT_BUS[i].getStackForm(), tooltip);
			this.addBlockTooltip(MetaTileEntities.FLUID_EXPORT_HATCH[i].getStackForm(), defaultText);
			this.addBlockTooltip(MetaTileEntities.FLUID_IMPORT_HATCH[i].getStackForm(), defaultText);
		}
	}
}