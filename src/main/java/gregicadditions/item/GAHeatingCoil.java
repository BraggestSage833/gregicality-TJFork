package gregicadditions.item;

import gregtech.common.blocks.VariantBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class GAHeatingCoil extends VariantBlock<GAHeatingCoil.CoilType> {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public GAHeatingCoil() {
        super(Material.IRON);
        setTranslationKey("ga_heating_coil");
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("wrench", 2);
        setDefaultState(getState(CoilType.TITAN_STEEL_COIL));
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 15 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        super.createBlockState();
        return new BlockStateContainer(this, VARIANT, ACTIVE);
    }

    @Override
    public IBlockState getState(CoilType variant) {
        return super.getState(variant).withProperty(ACTIVE, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta % 7).withProperty(ACTIVE, meta / 7 >= 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return Math.min(15, super.getMetaFromState(state) + (state.getValue(ACTIVE) ? 7 : 0));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.damageDropped(state) - (state.getValue(ACTIVE) ? 7 : 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> lines, ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, worldIn, lines, tooltipFlag);
        GAHeatingCoil.CoilType coilType = getState(getStateFromMeta(itemStack.getMetadata()));
        lines.add(I18n.format("tile.wire_coil.tooltip_ebf"));
        lines.add(I18n.format("tile.wire_coil.tooltip_heat", coilType.coilTemperature));
        lines.add("");
        lines.add(I18n.format("tile.wire_coil.tooltip_smelter"));
        lines.add(I18n.format("tile.wire_coil.tooltip_level", coilType.level));
        lines.add(I18n.format("tile.wire_coil.tooltip_discount", coilType.energyDiscount));
    }
    

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    public enum CoilType implements IStringSerializable {

        TITAN_STEEL_COIL("titan_steel_coil", 9600, 32, 8, null),
        PIKYONIUM_COIL("pikyonium_coil", 10700, 32, 8, null),
        BLACK_TITANIUM_COIL("black_titanium_coil", 11200, 64, 16, null),
        NEUTRONIUM_COIL("neutronium_coil", 12600, 64, 16, null),
        COSMIC_NEUTRONIUM_COIL("cosmic_neutronium_coil", 14200, 128, 32, null),
        INFINITY_COIL("infinity_coil", 28400, 128, 48, null),
        ETERNITY_COIL("eternity_coil", 56800, 512, 64, null);


        private final String name;
        private final int coilTemperature;
        private final int level;
        private final int energyDiscount;
        private final Material material;

        CoilType(String name, int coilTemperature, int level, int energyDiscount, Material material) {
            this.name = name;
            this.coilTemperature = coilTemperature;
            this.level = level;
            this.energyDiscount = energyDiscount;
            this.material = material;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getCoilTemperature() {
            return coilTemperature;
        }

        public int getLevel() {
            return level;
        }

        public int getEnergyDiscount() {
            return energyDiscount;
        }

    }
}
