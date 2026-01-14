package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.CasingUtils;
import gregicadditions.machines.multi.simple.TileEntityLargeWiremill;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockTurbineCasing;
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

import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;
import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeWiremillInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_WIREMILL;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		for (int tier = 0; tier < 15; tier++) {
			GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
					.aisle("XXXXX", "XXXXX", "XXXXX");
			for (int j = -2; j < Math.min(4, tier); j++) {
				builder.aisle("IXXXX", "XMGMX", "OXXXX");
			}
			shapeInfos.add(builder.aisle("XHX##", "XSX##", "EXX##")
					.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.NORTH)
					.where('S', GATileEntities.LARGE_WIREMILL, EnumFacing.WEST)
					.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
					.where('X', TileEntityLargeWiremill.casingState)
					.where('G', MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_GEARBOX))
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
					.where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.build());
		}
		return shapeInfos;
	}

	private static final ITextComponent componentCasingTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));

	@Override
	protected void generateBlockTooltips() {
		super.generateBlockTooltips();

		ITextComponent casingTooltip = new TextComponentTranslation("gregtech.multiblock.preview.limit", 8).setStyle(new Style().setColor(TextFormatting.RED));

		ItemStack defaultCasingStack = METAL_CASING_1.getItemVariant(MetalCasing1.CasingType.MARAGING_STEEL_250);
		ItemStack casingStack = CasingUtils.getConfigCasingItemStack(GAConfig.multis.largeWiremill.casingMaterial, defaultCasingStack);

		this.addBlockTooltip(casingStack, casingTooltip);

		for (MotorCasing.CasingType casingType : MotorCasing.CasingType.values()) {
			this.addBlockTooltip(GAMetaBlocks.MOTOR_CASING.getItemVariant(casingType), componentCasingTooltip);
		}
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_wiremill.description")};
	}
}
