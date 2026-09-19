package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeCentrifuge;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeCentrifugeInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_CENTRIFUGE;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes(int extent) {
		return GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
				.aisle("#OXo#", "XXSXX", "#IHi#")
				.aisle("XXXXX", "X###X", "XXXXX")
				.aisle("XXMXX", "G#P#G", "XXmXX")
				.aisle("XXXXX", "X###X", "XXXXX")
				.aisle("#XEX#", "XXGXX", "#XXX#")

				.where('S', GATileEntities.LARGE_CENTRIFUGE, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', TileEntityLargeCentrifuge.casingState)
				.where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
				.where('P', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TITANIUM_PIPE))
				.where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))

				.where('E', PlaceholderType.ENERGY_INPUT_HATCH,
						GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)

				.where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

				.where('O', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)

				.where('i', PlaceholderType.INPUT_HATCH, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

				.where('o', PlaceholderType.OUTPUT_HATCH,
						MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)

				.where('M', PlaceholderType.MOTOR, GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[0]))
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_centrifuge.description")};
	}
}
