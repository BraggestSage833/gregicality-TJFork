package gregicadditions.jei.multi.miner;

import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.miner.MetaTileEntityLargeMiner;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;


public class LargeMinerInfo extends MultiblockInfoPage {

    private final MetaTileEntityLargeMiner largeMiner;

    public LargeMinerInfo(MetaTileEntityLargeMiner largeMiner) {
        this.largeMiner = largeMiner;
    }

    @Override
    public MultiblockControllerBase getController() {
        return largeMiner;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("F###F", "F###F", "PPPPP", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                .aisle("#####", "#####", "PPPPP", "#MPO#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                .aisle("#####", "#####", "PPPPP", "#SPE#", "##F##", "##F##", "##F##", "##F##", "##F##", "##F##")
                .aisle("#####", "#####", "PPPPP", "#IPP#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                .aisle("F###F", "F###F", "PPPPP", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                .where('S', getController(), EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('P', largeMiner.getCasingState())
                .where('F', largeMiner.getFrameState());
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.EAST)
                    .where('I', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.machine.miner.multi.description", largeMiner.type.chunk, largeMiner.type.chunk, largeMiner.type.fortuneString)};
    }

    @Override
    public float getDefaultZoom() {
        return 0.5f;
    }
}
