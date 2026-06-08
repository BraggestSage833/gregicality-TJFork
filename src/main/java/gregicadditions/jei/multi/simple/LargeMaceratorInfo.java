package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeMacerator;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeMaceratorInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_MACERATOR;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("XXX", "XXX", "XXX", "XXX", "XXX", "XXX")
				.aisle("XXX", "XMX", "X#X", "XPX", "X#X", "XXX")
				.aisle("XSX", "XHX", "XEX", "XIX", "XOX", "XXX")

				.where('S', GATileEntities.LARGE_MACERATOR, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', TileEntityLargeMacerator.casingState)

				.where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)

				.where('I', PlaceholderType.INPUT_BUS,
						MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

				.where('O', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)

				.where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[0]))
				.where('P', GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[0]))
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_macerator.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.7f;
	}
}
