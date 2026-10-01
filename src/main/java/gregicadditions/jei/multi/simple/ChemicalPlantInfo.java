package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.channels.GAChannelRegistry;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.channels.ChannelRegistry;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class ChemicalPlantInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.CHEMICAL_PLANT;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes(int extent) {
		return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("X###X", "EXXXX", "X###X", "XXXXX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("X###X", "XPPPX", "XMMMX", "XPPPX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("H###X", "SioIO", "X###X", "XXXXX", "X###X")
				.where('S', GATileEntities.CHEMICAL_PLANT, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT))
				.where('P', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE))
				.where('E', ChannelRegistry.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
				.where('I', ChannelRegistry.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
				.where('O', ChannelRegistry.OUTPUT_BUS, MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
				.where('i', ChannelRegistry.INPUT_HATCH, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
				.where('o', ChannelRegistry.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
				.where('M', GAChannelRegistry.MOTOR, GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[0]))
				.where('C', ChannelRegistry.COIL)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.chemical_plant.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.8f;
	}
}
