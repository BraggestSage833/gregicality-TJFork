package gregicadditions.jei.multi.override;

import gregicadditions.item.GAMetaBlocks;
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


public class ElectricBlastFurnaceInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.ELECTRIC_BLAST_FURNACE;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
				.aisle("IFX", "CCC", "CCC", "XXX")
				.aisle("SXE", "C#C", "C#C", "XHX")
				.aisle("ODM", "CCC", "CCC", "XXX")
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.INVAR_HEATPROOF))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)
				.where('H', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('S', GATileEntities.ELECTRIC_BLAST_FURNACE, EnumFacing.WEST);
		for (int tier = 0; tier < 15; tier++) {
			shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('F', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
					.where('D', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.SOUTH)
					.where('C', GAMetaBlocks.getCoils(tier))
					.build());
		}
		return shapeInfos;
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.electric_blast_furnace.description")};
	}
}
