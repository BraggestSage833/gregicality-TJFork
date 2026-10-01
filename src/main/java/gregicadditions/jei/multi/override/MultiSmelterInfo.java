package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.channels.ChannelRegistry;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;


public class MultiSmelterInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.MULTI_FURNACE;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes(int extent) {
		return MultiblockShapeInfo.builder()
				.aisle("IXX", "CCC", "XXX")
				.aisle("SXE", "C#C", "MXX")
				.aisle("OXX", "CCC", "XXX")
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.INVAR_HEATPROOF))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('S', GATileEntities.MULTI_FURNACE, EnumFacing.WEST)

				.where('E', ChannelRegistry.ENERGY_INPUT_HATCH,
						GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)

				.where('I', ChannelRegistry.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

				.where('O', ChannelRegistry.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)

				.where('C', ChannelRegistry.COIL)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.multi_smelter.description")};
	}

}
