package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.BlockTurbineCasing;
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


public class LargeCombustionEngineInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_COMBUSTION_ENGINE[0];
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes(int extent) {
		return GAMultiblockShapeInfo.builder(FRONT, DOWN, RIGHT)
				.aisle("AAA", "ACA", "AAA")
				.aisle("HHH", "MGH", "HHH")
				.aisle("HHH", "FGH", "HHH")
				.aisle("HHH", "HEH", "HHH")
				.where('H', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TITANIUM_STABLE))
				.where('G', MetaBlocks.TURBINE_CASING.getState(BlockTurbineCasing.TurbineCasingType.TITANIUM_GEARBOX))
				.where('A', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ENGINE_INTAKE_CASING))
				.where('C', GATileEntities.LARGE_COMBUSTION_ENGINE[0], EnumFacing.WEST)
				.where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.NORTH)
				.where('E', PlaceholderType.ENERGY_OUTPUT_HATCH, GATileEntities.getEnergyHatch(0, true), EnumFacing.EAST)
				.where('F', PlaceholderType.INPUT_HATCH, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gregtech.multiblock.diesel_engine.description")};
	}
}
