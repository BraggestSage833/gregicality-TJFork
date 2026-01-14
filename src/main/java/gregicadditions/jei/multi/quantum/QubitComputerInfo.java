package gregicadditions.jei.multi.quantum;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAQuantumCasing;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("CC", "IC", "CC", "CC")
                .aisle("OC", "SC", "CC", "CC")
                .aisle("EC", "MC", "CC", "CC")
                .aisle("CC", "CC", "CC", "CC")
                .where('S', GATileEntities.QUBIT_COMPUTER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('C', GAMetaBlocks.QUANTUM_CASING.getState(GAQuantumCasing.CasingType.COMPUTER));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', GATileEntities.QBIT_OUTPUT_HATCH[0], EnumFacing.WEST)
                    .build());
        }
        return shapeInfos;
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
