package gregicadditions.jei.multi.advance;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAReactorCasing;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.unification.material.Materials.Naquadria;

public class HyperReactor3Info extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.HYPER_REACTOR_III;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
       return GAMultiblockShapeInfo.builder()
                .aisle("###########","###########","###########","###########","###########", "###########", "###########", "###########", "####CCC####", "###CGGGC###", "###CGGGC###", "###CGGGC###", "####CCC####", "###########", "###########", "###########")
                .aisle("###########","###########","###########","###########","###########", "###########", "###########", "###CCCCC###", "##CC###CC##", "##C#####C##", "##C#####C##", "##C#####C##", "##CC###CC##", "###CCCCC###", "###########", "###########")
                .aisle("##F#####F##","##F#####F##","##F#####F##","##F#####F##","##F#####F##", "##F#####F##", "##FCCCCCF##", "##C#####C##", "#C#######C#", "#C#######C#", "#C#######C#", "#C#######C#", "#C#######C#", "##C#####C##", "###CCCCC###", "###########")
                .aisle("###F###F###","###F###F###","###F###F###","###F###F###","###F###F###", "###FCCCF###", "##CC###CC##", "#C#######C#", "#C#######C#", "C#########C", "C####H####C", "C#########C", "#C#######C#", "#C#######C#", "##CC###CC##", "####CCC####")
                .aisle("###########","###########","###########","###########","###########", "###CCCCC###", "##C#####C##", "#C#######C#", "C#########C", "C####H####C", "C###HHH###C", "C####H####C", "C#########C", "#C#######C#", "##C#####C##", "###CCCCC###")
                .aisle("###########","###########","###########","###########","###########", "###CCCCC###", "##C#####C##", "#C#######C#", "C####H####C", "M###HHH###C", "S##HHHHH##E", "C###HHH###C", "C####H####C", "#C#######C#", "##C#####C##", "###CCCCC###")
                .aisle("###########","###########","###########","###########","###########", "###CCCCC###", "##C#####C##", "#C#######C#", "C#########C", "C####H####C", "f###HHH###C", "C####H####C", "C#########C", "#C#######C#", "##C#####C##", "###CCCCC###")
                .aisle("###F###F###","###F###F###","###F###F###","###F###F###","###F###F###", "###FCCCF###", "##CC###CC##", "#C#######C#", "#C#######C#", "C#########C", "C####H####C", "C#########C", "#C#######C#", "#C#######C#", "##CC###CC##", "####CCC####")
                .aisle("##F#####F##","##F#####F##","##F#####F##","##F#####F##","##F#####F##", "##F#####F##", "##FCCCCCF##", "##C#####C##", "#C#######C#", "#C#######C#", "#C#######C#", "#C#######C#", "#C#######C#", "##C#####C##", "###CCCCC###", "###########")
                .aisle("###########","###########","###########","###########","###########", "###########", "###########", "###CCCCC###", "##CC###CC##", "##C#####C##", "##C#####C##", "##C#####C##", "##CC###CC##", "###CCCCC###", "###########", "###########")
                .aisle("###########","###########","###########","###########","###########", "###########", "###########", "###########", "####CCC####", "###CGGGC###", "###CGGGC###", "###CGGGC###", "####CCC####", "###########", "###########", "###########")
                .where('S', GATileEntities.HYPER_REACTOR_III, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[2], EnumFacing.WEST)
                .where('C', GAMetaBlocks.REACTOR_CASING.getState(GAReactorCasing.CasingType.HYPER_CASING_2))
                .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.OSMIRIDIUM_GLASS))
                .where('H', GAMetaBlocks.REACTOR_CASING.getState(GAReactorCasing.CasingType.HYPER_CORE_3))
                .where('F', MetaBlocks.FRAMES.get(Naquadria).getDefaultState())
                .where('E', PlaceholderType.ENERGY_OUTPUT_HATCH,GATileEntities.getEnergyHatch(0, true), EnumFacing.EAST)
                .where('f', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.hyper_reactor.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.3f;
    }
}
