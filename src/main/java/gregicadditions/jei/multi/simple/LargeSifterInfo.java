package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeSifter;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMultiblockCasing;
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

public class LargeSifterInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_SIFTER;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
				.aisle("XXXXX", "PXXXP", "XXXXX")
				.aisle("IXXXX", "X###X", "XGGGX")
				.aisle("MXXXX", "S###X", "EGGGX")
				.aisle("OXXXX", "X###X", "XGGGX")
				.aisle("XXXXX", "PXXXP", "XXXXX")
				.where('S', GATileEntities.LARGE_SIFTER, EnumFacing.WEST)
				.where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', TileEntityLargeSifter.casingState);
		for (int tier = 0; tier < 15; tier++) {
			shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('P', GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.build());
		}
		return shapeInfos;
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_sifter.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.9f;
	}

	@Override
	protected void generateBlockTooltips() {
		super.generateBlockTooltips();

		for (PistonCasing.CasingType casingType : PistonCasing.CasingType.values()) {
			this.addBlockTooltip(GAMetaBlocks.PISTON_CASING.getItemVariant(casingType), pistonTooltip);
		}
	}

	private static final ITextComponent pistonTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));
}
