package gregicadditions.jei.multi;

import gregicadditions.GAMaterials;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.item.components.FieldGenCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.item.components.SensorCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class CosmicRayDetectorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.COSMIC_RAY_DETECTOR;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "###############", "###############", "######xxx######", "###############")
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "###############", "######xxx######", "####xx###xx####", "###############")
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "#######x#######", "####xxx#xxx####", "###x#######x###", "###############")
                .aisle("######XMX######", "######eSf######", "######XXX######", "###############", "###############", "#######X#######", "#####xxxxx#####", "###xx#####xx###", "##x#########x##", "###############")
                .aisle("#####XXXXX#####", "#####X###X#####", "#####X###X#####", "######XXX######", "######XXX######", "#####XXXXX#####", "####xxxxxxx####", "##xx#######xx##", "#x###########x#", "###############")
                .aisle("####XXXXXXX####", "####X#####X####", "####X#####X####", "#####X###X#####", "#####X###X#####", "####XXxxxXX####", "###xxx###xxx###", "##x#########x##", "#x###########x#", "###############")
                .aisle("###XXXXXXXXX###", "###X###E###X###", "###X#######X###", "####X#####X####", "####X##F##X####", "####XxxxxxX####", "###xx#####xx###", "#xx#########xx#", "x#############x", "###############")
                .aisle("###XXXXXXXXX###", "###X##EcE##X###", "###X###c###X###", "####X##c##X####", "####X#FcF#X####", "###XXxxExxXX###", "##xxx##C##xxx##", "#x#####C#####x#", "x######C######x", "#######s#######")
                .aisle("###XXXXXXXXX###", "###X###E###X###", "###X#######X###", "####X#####X####", "####X##F##X####", "####XxxxxxX####", "###xx#####xx###", "#xx#########xx#", "x#############x", "###############")
                .aisle("####XXXXXXX####", "####X#####X####", "####X#####X####", "#####X###X#####", "#####X###X#####", "####XXxxxXX####", "###xxx###xxx###", "##x#########x##", "#x###########x#", "###############")
                .aisle("#####XXXXX#####", "#####X###X#####", "#####X###X#####", "######XXX######", "######XXX######", "#####XXXXX#####", "####xxxxxxx####", "##xx#######xx##", "#x###########x#", "###############")
                .aisle("######XXX######", "######XXX######", "######XXX######", "###############", "###############", "#######X#######", "#####xxxxx#####", "###xx#####xx###", "##x#########x##", "###############")
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "#######x#######", "####xxx#xxx####", "###x#######x###", "###############")
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "###############", "######xxx######", "####xx###xx####", "###############")
                .aisle("###############", "###############", "###############", "###############", "###############", "###############", "###############", "###############", "######xxx######", "###############")
                .where('S', GATileEntities.COSMIC_RAY_DETECTOR, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', GAMetaBlocks.METAL_CASING_2.getState(MetalCasing2.CasingType.QUANTUM))
                .where('x', GAMetaBlocks.METAL_CASING_2.getState(MetalCasing2.CasingType.TRITANIUM))
                .where('C', MetaBlocks.FRAMES.get(GAMaterials.BlackTitanium).getDefaultState())
                .where('c', MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.SUPERCONDUCTOR));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('e', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('f', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('E', GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('s', GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('P', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.cosmic_ray_detector.description")};
    }


    @Override
    public float getDefaultZoom() {
        return 0.3f;
    }
}
