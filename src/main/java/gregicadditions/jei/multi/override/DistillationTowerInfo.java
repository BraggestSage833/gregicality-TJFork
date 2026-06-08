package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;


public class DistillationTowerInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.DISTILLATION_TOWER;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return MultiblockShapeInfo.builder()
				.aisle("EXX", "XXX", "XXX", "XXX", "XXX", "XXX")
				.aisle("SFX", "M#X", "X#X", "X#X", "X#X", "XmX")
				.aisle("IXX", "HXX", "HXX", "HXX", "HXX", "HXX")
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('S', GATileEntities.DISTILLATION_TOWER, EnumFacing.WEST)
				.where('E', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
				.where('I', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, 0)], EnumFacing.WEST)
				.where('F', PlaceholderType.INPUT_HATCH, MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, 0)], EnumFacing.DOWN)
				.where('H', PlaceholderType.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, 0)], EnumFacing.WEST)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.distillation_tower.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.7f;
	}
}
