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


public class ImplosionCompressorInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.IMPLOSION_COMPRESSOR;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return MultiblockShapeInfo.builder()
				.aisle("XXX", "XBX", "XXX")
				.aisle("MXX", "C#E", "XmX")
				.aisle("XXX", "XIX", "XXX")
				.where('C', GATileEntities.IMPLOSION_COMPRESSOR, EnumFacing.WEST)
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
				.where('E', PlaceholderType.ENERGY_INPUT_HATCH,
						GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
				.where('I', PlaceholderType.INPUT_BUS,
						MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.SOUTH)
				.where('B', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.implosion_compressor.description")};
	}

}
