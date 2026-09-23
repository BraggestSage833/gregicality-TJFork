package gregicadditions.jei;

import com.google.common.collect.Lists;
import gregicadditions.channels.GAChannelRegistry;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.fusion.GACryostatCasing;
import gregicadditions.item.fusion.GADivertorCasing;
import gregicadditions.item.fusion.GAFusionCasing;
import gregicadditions.item.fusion.GAVacuumCasing;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.channels.ChannelRegistry;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
;

public class FusionReactor4Info extends MultiblockInfoPage {
    @Override
    public MultiblockControllerBase getController() {

        return GATileEntities.ADVANCED_FUSION_REACTOR;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder()
            .aisle("###############", "###############","#####ccCcc#####","#####ccCcc#####","###############","###############")
            .aisle("###############", "#######C#######","###ccvEvEvcc###","###ccvEvEvcc###","#######C#######","###############")
            .aisle("#######C#######", "##C##EEdEE##C##","##Cvv#####vvC##","##Cvv#####vvC##","##C##bbbbb##C##","#######C#######")
            .aisle("###C###C###C###", "###EEdddddEE###","#cv#########vc#","#cv#########vc#","###bbbbbbbbb###","###C###C###C###")
            .aisle("####C#####C####", "###Edd#C#ddE###","#cv###vvv###vc#","#cv###vvv###vc#","###bbb#C#bbb###","####C#####C####")
            .aisle("###############", "##EddC###CddE##","cv###v#C#v###vc","cv###v#C#v###vc","##bbbC###Cbbb##","###############")
            .aisle("######XXX######", "##dd##CCC##dd##","cv##v#CCC#v##vc","cv##v#CCC#v##vc","##bb##CCC##bb##","######XXX######")
            .aisle("##CC##XXX##CC##", "#CddC#CCC#CddC#","Cv##vCCCCCv##vC","Cv##vCCCCCv##vC","#CbbC#CCC#CbbC#","##CC##XXX##CC##")
            .aisle("######XXX######", "##dd##CCC##dd##","cv##v#CCC#v##vc","cv##v#CCC#v##vc","##bb##CCC##bb##","######XXX######")
            .aisle("###############", "##dddC###Cddd##","cv###v#C#v###vc","cv###v#C#v###vc","##bbbC###Cbbb##","###############")
            .aisle("####C#####C####", "###ddd#C#ddd###","#cv###vvv###vc#","#cv###vvv###vc#","###bbb#C#bbb###","####C#####C####")
            .aisle("###C###C###C###", "###dfddddddd###","#cv#########vc#","#cv#########vc#","###bbbbbbbbb###","###C###C###C###")
            .aisle("#######C#######", "##C##ffdFF##C##","##Cvv#####vvC##","##Cvv#####vvC##","##C##bbbbb##C##","#######C#######")
            .aisle("###############", "#######S#######","###ccvvvvvcc###","###ccvvvvvcc###","#######C#######","###############")
            .aisle("###############", "###############","#####ccCcc#####","#####ccCcc#####","###############","###############")
            .where('S', GATileEntities.ADVANCED_FUSION_REACTOR, EnumFacing.SOUTH)
            .where('#', Blocks.AIR.getDefaultState())

            .where('C', GAChannelRegistry.FUSION_COIL, GAMetaBlocks.FUSION_CASING.getState(GAFusionCasing.CasingType.values()[4])) //1-4
            .where('X', GAMetaBlocks.FUSION_CASING.getState(GAFusionCasing.CasingType.ADV_FUSION_CASING))
            .where('f', MetaTileEntities.FLUID_IMPORT_HATCH[8], EnumFacing.SOUTH)
            .where('F', MetaTileEntities.FLUID_EXPORT_HATCH[8], EnumFacing.SOUTH)
            .where('E', ChannelRegistry.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(9,false), EnumFacing.NORTH)

            .where('c', GAChannelRegistry.CRYOSTAT_CASING, GAMetaBlocks.CRYOSTAT_CASING.getState(GACryostatCasing.CasingType.values()[0])) // 1-4
            .where('v', GAChannelRegistry.VACUUM_CASING, GAMetaBlocks.VACUUM_CASING.getState(GAVacuumCasing.CasingType.values()[0])) // 1-4
            .where('b', GAMetaBlocks.FUSION_CASING.getState(GAFusionCasing.CasingType.FUSION_BLANKET))
            .where('d', GAChannelRegistry.DIVERTOR_CASING, GAMetaBlocks.DIVERTOR_CASING.getState(GADivertorCasing.CasingType.values()[0])) // 1-4
            .build();

    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.machine.multiblock.description")};
    }


    @Override
    public float getDefaultZoom() {
        return 0.5f;
    }
}
