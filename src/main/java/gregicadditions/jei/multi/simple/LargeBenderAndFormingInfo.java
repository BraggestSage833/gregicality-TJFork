package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeBenderAndForming;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeBenderAndFormingInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_BENDER_AND_FORMING;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return MultiblockShapeInfo.builder()
                .aisle("XXXX", "XXXX", "XXIX")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")
                .aisle("iXXX", "XPMX", "XXIO")

                .aisle("EXXX", "XSHX", "XXIX")
                .where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
                .where('S', getController(), EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', TileEntityLargeBenderAndForming.casingState)

                .where('i', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)

                .where('O', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.SOUTH)

                .where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[0]))
                .where('P', GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[0]))
                .where('I', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TITANIUM_PIPE))
                .build();
    }

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_bender_and_forming.description")};
	}

    private static final ITextComponent componentTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();

        for (MotorCasing.CasingType casingType : MotorCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.MOTOR_CASING.getItemVariant(casingType), componentTooltip);
        }

        for (PistonCasing.CasingType casingType : PistonCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.PISTON_CASING.getItemVariant(casingType), componentTooltip);
        }
    }
}
