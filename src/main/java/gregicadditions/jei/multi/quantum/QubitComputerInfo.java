package gregicadditions.jei.multi.quantum;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAQuantumCasing;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class QubitComputerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.QUBIT_COMPUTER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return MultiblockShapeInfo.builder()
                .aisle("CC", "IC", "CC", "CC")
                .aisle("OC", "SC", "CC", "CC")
                .aisle("EC", "MC", "CC", "CC")
                .aisle("CC", "CC", "CC", "CC")
                .where('S', GATileEntities.QUBIT_COMPUTER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('C', GAMetaBlocks.QUANTUM_CASING.getState(GAQuantumCasing.CasingType.COMPUTER))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
                .where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('O', GATileEntities.QBIT_OUTPUT_HATCH[0], EnumFacing.WEST)

                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.qubit_computer.description")};
    }


    @Override
    public float getDefaultZoom() {
        return 1.1f;
    }
}
