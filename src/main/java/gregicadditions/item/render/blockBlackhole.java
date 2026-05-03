package gregicadditions.item.render;

import gregicadditions.Gregicality;
import gregicadditions.client.renderer.RenderingTileEntityBlackhole;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class blockBlackhole extends Block{

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);



    public blockBlackhole() {
        super(Material.IRON);
        setRegistryName(Gregicality.MODID, "black_hole");
        setTranslationKey(Gregicality.MODID + ".black_hole");
        setHardness(5.0F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 2);
        setLightOpacity(0);
        setCreativeTab(net.minecraft.creativetab.CreativeTabs.BUILDING_BLOCKS);;
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




}
