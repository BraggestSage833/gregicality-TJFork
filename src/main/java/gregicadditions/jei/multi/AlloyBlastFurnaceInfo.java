package gregicadditions.jei.multi;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;

public class AlloyBlastFurnaceInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.ALLOY_BLAST_FURNACE;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("#EXX#", "#ccc#", "#ccc#", "#XXX#")
                .aisle("MXXXX", "cCCCc", "cCCCc", "XXXXX")
                .aisle("SXXXi", "cCACc", "cCACc", "XXmXX")
                .aisle("IXXXX", "cCCCc", "cCCCc", "XXXXX")
                .aisle("#OXX#", "#ccc#", "#ccc#", "#XXX#")
                .where('S', GATileEntities.ALLOY_BLAST_FURNACE, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('c', METAL_CASING_2.getState(MetalCasing2.CasingType.STABALLOY))
                .where('X', METAL_CASING_1.getState(MetalCasing1.CasingType.ZIRCONIUM_CARBIDE))
                .where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP);
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder .where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('O', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.EAST)
                    .where('C', GAMetaBlocks.getCoils(tier))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.alloy_blast_furnace.description")};
    }

    @Override
    protected void generateBlockTooltips() {
        super.generateBlockTooltips();
        this.addBlockTooltip(METAL_CASING_1.getItemVariant(MetalCasing1.CasingType.ZIRCONIUM_CARBIDE), new TextComponentTranslation("gregtech.multiblock.preview.limit", 14));
    }
}
