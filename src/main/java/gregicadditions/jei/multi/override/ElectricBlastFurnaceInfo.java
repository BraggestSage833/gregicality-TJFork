package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
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


public class ElectricBlastFurnaceInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.ELECTRIC_BLAST_FURNACE;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return MultiblockShapeInfo.builder()
				.aisle("IFX", "CCC", "CCC", "XXX")
				.aisle("SXE", "C#C", "C#C", "XHX")
				.aisle("ODM", "CCC", "CCC", "XXX")
				.where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.INVAR_HEATPROOF))
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)
				.where('H', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('S', GATileEntities.ELECTRIC_BLAST_FURNACE, EnumFacing.WEST)
				.where('E', PlaceholderType.ENERGY_INPUT_HATCH,
						GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
				.where('I', PlaceholderType.INPUT_BUS,
						MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
				.where('O', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
				.where('F', PlaceholderType.INPUT_HATCH,
						MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.NORTH)
				.where('D', PlaceholderType.OUTPUT_HATCH,
						MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.SOUTH)
				.where('C', PlaceholderType.COIL)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.electric_blast_furnace.description")};
	}
}
