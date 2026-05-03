package gregicadditions.item.render;

import gregicadditions.Gregicality;
import gregicadditions.machines.renderTileEntities.RenderingTileEntityBlackhole;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class blockBlackhole extends Block{

    private static final AxisAlignedBB TINY_AABB =
            new AxisAlignedBB(0.45, 0.45, 0.45, 0.55, 0.55, 0.55);




    public blockBlackhole() {
        super(Material.IRON);
        setRegistryName(Gregicality.MODID, "black_hole");
        setTranslationKey(Gregicality.MODID + ".black_hole");
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new RenderingTileEntityBlackhole();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return Block.NULL_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return player.isCreative();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return playerIsCreative(world) ? 0.0F : -1.0F;
    }

    private boolean playerIsCreative(World world) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        return player != null && player.isCreative();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return 6000000.0F;
    }
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos,
                                            Vec3d start, Vec3d end) {

        EntityPlayer player = world.getClosestPlayer(start.x, start.y, start.z, 1.0, false);

        // creative only for debug
        if (player != null && player.isCreative()) {
            return this.rayTrace(pos, start, end, TINY_AABB);
        }
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        return TINY_AABB.offset(pos);
    }
}
