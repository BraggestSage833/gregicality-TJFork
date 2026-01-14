package gregicadditions.jei.multi.override;

import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
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
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
				.aisle("EXX", "XXX", "XXX", "XXX", "XXX", "XXX")
				.aisle("SFX", "M#X", "X#X", "X#X", "X#X", "XmX")
				.aisle("IXX", "HXX", "HXX", "HXX", "HXX", "HXX")
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('S', GATileEntities.DISTILLATION_TOWER, EnumFacing.WEST);
		for (int tier = 0; tier < 15; tier++) {
			shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
					.where('I', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.DOWN)
					.where('H', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
					.build());
		}
		return shapeInfos;
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
