package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.item.components.FieldGenCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.item.components.SensorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.uumatter.TileEntityLargeReplicator;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeReplicatorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_REPLICATOR;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#####XXEXX#####", "#####eXEXe#####", "#######X#######")
                .aisle("###XXXXXXXXX###", "###XXCCCCCXX###", "#####XPXPX#####")
                .aisle("##XXXXXEXXXXX##", "##XCCCXSHCCCX##", "###XXF#s#FXX###")
                .aisle("#XXXXX###XXXXX#", "#XCCXX###XXCCX#", "##XF#######FX##")
                .aisle("#XXX#######XXX#", "#XCX#######XCX#", "##X#########X##")
                .aisle("iXXX#######XXXI", "oCCX#######XCCO", "#XF#########FX#")
                .aisle("iXX#########XXI", "oCX#########XCO", "#P###########P#")
                .aisle("EXE#########EXE", "ECE#########ECE", "XXs#########sXX")
                .aisle("iXX#########XXI", "oCX#########XCO", "#P###########P#")
                .aisle("iXXX#######XXXI", "oCCX#######XCCO", "#XF#########FX#")
                .aisle("#XXX#######XXX#", "#XCX#######XCX#", "##X#########X##")
                .aisle("#XXXXX###XXXXX#", "#XCCXX###XXCCX#", "##XF#######FX##")
                .aisle("##XXXXXEXXXXX##", "##XCCCXEXCCCX##", "###XXF#s#FXX###")
                .aisle("###XXXXXXXXX###", "###XXCCCCCXX###", "#####XPXPX#####")
                .aisle("#####XXEXX#####", "#####XXEXX#####", "#######X#######")
                .where('S', GATileEntities.LARGE_REPLICATOR, EnumFacing.EAST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.EAST)
                .where('X', TileEntityLargeReplicator.casingState)
                .where('C', MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.SUPERCONDUCTOR));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('e', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.SOUTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.SOUTH)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('F', GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('P', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('s', GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('E', GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public float getDefaultZoom() {
        return 0.4f;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_replicator.description")};
    }

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();

        for (FieldGenCasing.CasingType casingType : FieldGenCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.FIELD_GEN_CASING.getItemVariant(casingType), tieredCasingTooltip);
        }

        for (PumpCasing.CasingType casingType : PumpCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.PUMP_CASING.getItemVariant(casingType), tieredCasingTooltip);
        }

        for (SensorCasing.CasingType casingType : SensorCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.SENSOR_CASING.getItemVariant(casingType), tieredCasingTooltip);
        }

        for (EmitterCasing.CasingType casingType : EmitterCasing.CasingType.values()) {
            this.addBlockTooltip(GAMetaBlocks.EMITTER_CASING.getItemVariant(casingType), tieredCasingTooltip);
        }
    }

    private static final ITextComponent tieredCasingTooltip = new TextComponentTranslation("gregtech.multiblock.universal.component_casing.tooltip").setStyle(new Style().setColor(TextFormatting.RED));
}
