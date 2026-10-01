package gregicadditions.item.components;

import com.google.common.collect.ImmutableMap;
import gregicadditions.GAConfig;
import gregicadditions.blocks.GAMetalCasing;
import gregicadditions.client.model.IReTexturedModel;
import gregicadditions.client.model.ReTexturedModel;
import gregicadditions.client.model.ReTexturedModelLoader;
import gregicadditions.machines.multi.CasingLinks;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.VariantBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;


public abstract class ReTexturedCasing<T extends Enum<T> & IStringSerializable> extends VariantBlock<T> implements IReTexturedModel {

    private final static ResourceLocation FRAME_MODEL = new ResourceLocation("gtadditions", "block/casing/frame");
    private final static ResourceLocation GLASS_MODEL = new ResourceLocation("gtadditions", "block/casing/glass");
    private final ResourceLocation CORE_MODEL;
    private ControllerProperty CONTROLLER;


    public ReTexturedCasing(ResourceLocation core) {
        super(Material.IRON);
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("wrench", 2);
        CORE_MODEL = core;
    }

    @Override
    public void register(IBlockState state, ResourceLocation path) {
        if (GAConfig.client.AdvancedCasingModel) {
            ReTexturedModelLoader.register(this, path, new ReTexturedModel(getModels(state)));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ResourceLocation[] getModels(IBlockState blockState) {
        return new ResourceLocation[]{CORE_MODEL, FRAME_MODEL, GLASS_MODEL};
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ImmutableMap<String, String> reTextured(IBlockState blockState, EnumFacing enumFacing, ResourceLocation model) {
        if (model == FRAME_MODEL && blockState instanceof IExtendedBlockState && enumFacing == null) {
            MultiblockControllerBase controller = ((IExtendedBlockState) blockState).getValue(CONTROLLER);
            if (controller == null) return null;
            ICubeRenderer texture = controller.getBaseTexture(null);
            if (texture == null) return null;
            return new ImmutableMap.Builder<String, String>()
                    .put("1", controller.getBaseTexture(null).getParticleSprite().getIconName())
                    .build();
        }
        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public List<BakedQuad> reBakedQuad(IBlockState blockState, EnumFacing side, ResourceLocation model, List<BakedQuad> base) {
        if (!(blockState instanceof IExtendedBlockState) || FRAME_MODEL != model) {
            return base;
        }

        MultiblockControllerBase controller = ((IExtendedBlockState) blockState).getValue(CONTROLLER);
        if (controller == null) {
            return base;
        }

        ICubeRenderer texture = controller.getBaseTexture(null);
        if (!(texture instanceof GAMetalCasing)) {
            return base;
        }

        GAMetalCasing casing = (GAMetalCasing) texture;
        int color = casing.blockState.getBaseState().getValue(casing.variantProperty).materialRGB;
        int colorCasing = 0xFF000000 | ((color & 0x00FF0000) >> 16) | (color & 0x0000FF00) | ((color & 0x000000FF) << 16);

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            int[] data = quad.getVertexData().clone();
            int stride = quad.getFormat().getIntegerSize();
            for (int v = 0; v < 4; v++) {
                data[v * stride + 3] = colorCasing;
            }
            out.add(new BakedQuad(data, quad.getTintIndex(), quad.getFace(), quad.getSprite(),
                    quad.shouldApplyDiffuseLighting(), quad.getFormat()));
        }
        return out;
    }

    @Override
    public boolean shouldRenderInLayer(IBlockState blockState, EnumFacing side, ResourceLocation model, BlockRenderLayer layer) {
        if (blockState == null) return true;
        if (GLASS_MODEL == model && layer == BlockRenderLayer.TRANSLUCENT) {
            return true;
        } else if (GLASS_MODEL != model && layer == BlockRenderLayer.CUTOUT_MIPPED) {
            return true;
        }
        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation variant, ResourceLocation modelRes, IModel model) {
        if (modelRes == CORE_MODEL && variant instanceof ModelResourceLocation) {
            String[] tierS = ((ModelResourceLocation) variant).getVariant().split("_");
            if (tierS.length > 0) {
                return model.retexture(new ImmutableMap.Builder<String, String>()
                        .put("0", "gtadditions:blocks/casing/" + tierS[tierS.length - 1])
                        .build());
            }
        }
        return model;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState && FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            state = ((IExtendedBlockState) state).withProperty(CONTROLLER, findController(world, pos));
        }
        return super.getExtendedState(state, world, pos);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        Class<T> enumClass = GTUtility.getActualTypeParameter(this.getClass(), ReTexturedCasing.class, 0);
        this.VARIANT = PropertyEnum.create("variant", enumClass);
        this.VALUES = enumClass.getEnumConstants();
        CONTROLLER = new ControllerProperty();
        return new BlockStateContainer.Builder(this).add(this.VARIANT).add(CONTROLLER).build();
    }

    @SideOnly(Side.CLIENT)
    protected MultiblockControllerBase findController(IBlockAccess access, BlockPos pos) {
        if (access == null || pos == null) {
            return null;
        }

        MultiblockControllerBase preview = CasingLinks.getPreview(access);
        if (preview != null)  {
            return preview; // for JEI
        }

        if (access instanceof World && access != Minecraft.getMinecraft().world) {
            return null; // ??? some goofy world
        }

        return CasingLinks.get(pos); // real world
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        CasingLinks.clear();
    }


    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    /** @deprecated */
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    private static class ControllerProperty implements IUnlistedProperty<MultiblockControllerBase>{
        @Override
        public String getName() {
            return "controller";
        }

        @Override
        public boolean isValid(MultiblockControllerBase controller) {
            return true;
        }

        @Override
        public Class<MultiblockControllerBase> getType() {
            return MultiblockControllerBase.class;
        }

        @Override
        public String valueToString(MultiblockControllerBase controller) {
            return controller == null ? "null" : controller.toString();
        }
    }

}
