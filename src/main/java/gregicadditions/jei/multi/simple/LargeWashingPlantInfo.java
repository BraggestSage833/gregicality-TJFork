package gregicadditions.jei.multi.simple;

import com.google.common.collect.Lists;
import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.CasingUtils;
import gregicadditions.machines.multi.simple.TileEntityLargeWashingPlant;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
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

public class LargeWashingPlantInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_WASHING_PLANT;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("XXXXX", "XXXXX", "XXXXX")
				.aisle("XXXXX", "XP#PX", "X###X")
				.aisle("XXXXX", "XP#PX", "X###X")
				.aisle("XXXXX", "XP#PX", "X###X")
				.aisle("XXXXX", "XP#PX", "X###X")
				.aisle("XXXXX", "XP#PX", "X###X")
				.aisle("IOMEX", "XHSiX", "XXXXX")
				.where('S', GATileEntities.LARGE_WASHING_PLANT, EnumFacing.WEST)
				.where('X', TileEntityLargeWashingPlant.casingState)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('#', Blocks.WATER.getDefaultState())
				.where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE));
		for (int tier = 0; tier < 15; tier++) {
			shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
					.where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.build());
		}
		return Lists.newArrayList(shapeInfos);
	}

	private static final ITextComponent componentCasingTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));

	@Override
	protected void generateBlockTooltips() {
		super.generateBlockTooltips();

		ITextComponent casingTooltip = new TextComponentTranslation("gregtech.multiblock.preview.limit", 25).setStyle(new Style().setColor(TextFormatting.RED));

		ItemStack defaultCasingStack = METAL_CASING_1.getItemVariant(MetalCasing1.CasingType.GRISIUM);
		ItemStack casingStack = CasingUtils.getConfigCasingItemStack(GAConfig.multis.largeWashingPlant.casingMaterial, defaultCasingStack);

		this.addBlockTooltip(casingStack, casingTooltip);

		for (MotorCasing.CasingType casingType : MotorCasing.CasingType.values()) {
			this.addBlockTooltip(GAMetaBlocks.MOTOR_CASING.getItemVariant(casingType), componentCasingTooltip);
		}
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_washing_plant.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.7f;
	}
}
