package gregicadditions.machines.multi.miner;

import gregicadditions.client.ClientHandler;
import gregicadditions.item.metal.MetalCasing2;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.Material;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;


public class MetaTileEntityLargeMiner4 extends MetaTileEntityLargeMiner {

    public MetaTileEntityLargeMiner4(ResourceLocation metaTileEntityId, Miner.Type type) {
        super(metaTileEntityId, type);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityLargeMiner4(metaTileEntityId, getType());
    }

    @Override
    public IBlockState getCasingState() {
        return METAL_CASING_2.getState(MetalCasing2.CasingType.TRITANIUM);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return ClientHandler.TRITANIUM_CASING;
    }

    @Override
    public IBlockState getFrameState() {

        Material material = Materials.Tritanium;
        return MetaBlocks.FRAMES.get(material).getDefaultState();
    }
}