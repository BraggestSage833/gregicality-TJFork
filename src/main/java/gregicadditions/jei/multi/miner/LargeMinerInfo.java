package gregicadditions.jei.multi.miner;

import gregicadditions.GAConfig;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.miner.MetaTileEntityLargeMiner;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
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
    public MultiblockShapeInfo getMatchingShapes(int extent) {

        return MultiblockShapeInfo.builder()
            .aisle("F###F", "F###F", "PPPPP", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
            .aisle("#####", "#####", "PPPPP", "#MPO#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
            .aisle("#####", "#####", "PPPPP", "#SPE#", "##F##", "##F##", "##F##", "##F##", "##F##", "##F##")
            .aisle("#####", "#####", "PPPPP", "#IPP#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
            .aisle("F###F", "F###F", "PPPPP", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
            .where('S', getController(), EnumFacing.WEST)
            .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
            .where('P', largeMiner.getCasingState())
            .where('F', largeMiner.getFrameState())
            .where('E', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
            .where('O', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.EAST)
            .where('I', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
            .build();

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
