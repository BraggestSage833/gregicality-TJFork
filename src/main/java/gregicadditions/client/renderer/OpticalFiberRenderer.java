package gregicadditions.client.renderer;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.BlockRenderer.BlockFace;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.block.BlockRenderingRegistry;
import codechicken.lib.render.block.ICCBlockRenderer;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.IconTransformation;
import gregicadditions.Gregicality;
import gregicadditions.pipelike.opticalfiber.BlockOpticalFiber;
import gregicadditions.pipelike.opticalfiber.ItemBlockOpticalFiber;
import gregicadditions.pipelike.opticalfiber.OpticalFiberProperties;
import gregicadditions.pipelike.opticalfiber.OpticalFiberSize;
import gregicadditions.utils.GALog;
import gregtech.api.pipenet.block.BlockPipe;
import gregtech.api.pipenet.block.IPipeType;
import gregtech.api.pipenet.tile.IPipeTile;
import gregtech.api.unification.material.type.Material;
import gregtech.common.render.PipeRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;


import javax.annotation.Nullable;

public class OpticalFiberRenderer extends PipeRenderer {
    public static OpticalFiberRenderer INSTANCE = new OpticalFiberRenderer();
    private TextureAtlasSprite wireTexture;

    private OpticalFiberRenderer() {
        super("ga_optical_fiber", new ModelResourceLocation(new ResourceLocation(Gregicality.MODID, "cable"), "normal"));
    }


    public void registerIcon(TextureMap map) {
        GALog.logger.info("Registering cable textures.");
        ResourceLocation wireLocation = new ResourceLocation(Gregicality.MODID, "blocks/cable/wire");
        this.wireTexture = map.registerSprite(wireLocation);

    }

    @Override
    public void buildRenderer(PipeRenderContext context, BlockPipe<?,?,?> blockPipe, IPipeTile<?,?> pipeTile,
                              IPipeType<?> pipeType, @Nullable Material material) {
        context.addOpenFaceRender(new IconTransformation(this.wireTexture))
                .addSideRender(new IconTransformation(this.wireTexture));
    }


    @Override
    public TextureAtlasSprite getParticleTexture(IPipeType<?> pipeType, @org.jetbrains.annotations.Nullable Material material) {
        return this.wireTexture;
    }

}
