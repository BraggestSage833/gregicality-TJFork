package gregicadditions;


import gregicadditions.utils.GALog;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.util.GTUtility;
import gregtech.common.ConfigHolder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GAUtility {

    public static byte getTierByVoltage(long voltage) {
        byte tier = 0;

        do {
            ++tier;
            if (tier >= GAValues.V.length) {
                return (byte)Math.min(GAValues.V.length - 1, tier);
            }

            if (voltage == GAValues.V[tier]) {
                return tier;
            }
        } while(voltage >= GAValues.V[tier]);

        return (byte)Math.max(0, tier - 1);
    }

    public static void doOvervoltageExplosion(MetaTileEntity metaTileEntity, long voltage) {
        BlockPos pos = metaTileEntity.getPos();
        metaTileEntity.getWorld().setBlockToAir(pos);
        if (!metaTileEntity.getWorld().isRemote) {
            double posX = (double)pos.getX() + 0.5D;
            double posY = (double)pos.getY() + 0.5D;
            double posZ = (double)pos.getZ() + 0.5D;
            ((WorldServer)metaTileEntity.getWorld()).spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 10, 0.2D, 0.2D, 0.2D, 0.0D);
            metaTileEntity.getWorld().createExplosion(null, posX, posY, posZ, getTierByVoltage(voltage), ConfigHolder.doExplosions);
        }

    }

    public static int setBetweenInclusive(int value, int start, int end) {
        if (value < start)
            return start;
        return Math.min(value, end);
    }

    /**
     * Safely cast a Long to an Int without overflow.
     *
     * @param v The Long value to cast to an Int.
     * @return v, casted to Int, or Integer.MAX_VALUE if it would overflow.
     */
    public static int safeCastLongToInt(long v) {
        return v > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) v;
    }

    public static void checkRecipeConflicts() {
        for (RecipeMap recipeMap : RecipeMap.getRecipeMaps()) {
            for (Object recipe : recipeMap.getRecipeList()) {
                if (recipe instanceof Recipe) {
                    Recipe r = (Recipe) recipe;
                    List<FluidStack> fluidInputs = r.getFluidInputs();
                    List<CountableIngredient> inputs = r.getInputs();
                    List<ItemStack> itemStacks = new ArrayList<>();
                    for (CountableIngredient ci : inputs) {
                        if (ci.getIngredient().getMatchingStacks().length != 0) {
                            itemStacks.add(ci.getIngredient().getMatchingStacks()[0]);
                        }
                    }
                    Recipe result = recipeMap.findRecipe(Integer.MAX_VALUE, itemStacks, fluidInputs, Integer.MAX_VALUE);
                    if (result != null && result != r) {
                        List<String> outputs = result.getOutputs().stream().map(ItemStack::getDisplayName).collect(Collectors.toList());
                        List<String> outputs2 = r.getOutputs().stream().map(ItemStack::getDisplayName).collect(Collectors.toList());
                        List<String> fluidOutputs = result.getFluidOutputs().stream().map(FluidStack::getUnlocalizedName).collect(Collectors.toList());
                        List<String> fluidOutputs2 = r.getFluidOutputs().stream().map(FluidStack::getUnlocalizedName).collect(Collectors.toList());
                        List<String> inputNames = itemStacks.stream().map(ItemStack::getDisplayName).collect(Collectors.toList());
                        List<String> fluidInputNames = r.getFluidInputs().stream().map(FluidStack::getUnlocalizedName).collect(Collectors.toList());
                        GALog.logger.info("Possibly conflicting recipe in " + recipeMap.toString() + " " + fluidInputNames + " " + inputNames + " " + outputs2 + " " + fluidOutputs2 + ", conflicting recipe result " + outputs + " " + fluidOutputs);
                    }
                }
            }
        }
    }

    public static void applyHammerDrops(Random random, IBlockState blockState, List<ItemStack> drops, int fortuneLevel, EntityPlayer player, long voltage) {
        ItemStack itemStack = new ItemStack(blockState.getBlock(), 1, blockState.getBlock().getMetaFromState(blockState));
        Recipe recipe = RecipeMaps.MACERATOR_RECIPES.findRecipe(Long.MAX_VALUE, Collections.singletonList(itemStack), Collections.emptyList(), 0);
        if (recipe != null && !recipe.getOutputs().isEmpty()) {
            drops.clear();
            for (ItemStack outputStack : recipe.getResultItemOutputs(Integer.MAX_VALUE, random, GAUtility.getTierByVoltage(voltage))) {
                outputStack = outputStack.copy();
                    if (!(player instanceof FakePlayer) && !outputStack.isItemEqual(OreDictUnifier.get(OrePrefix.dust,Materials.Stone))) {
                    int growAmount = Math.round(outputStack.getCount() * random.nextFloat());
                    if (fortuneLevel > 0) {
                        int i = Math.max(0, random.nextInt(fortuneLevel + 2) - 1);
                        growAmount += outputStack.getCount() * i;
                    }
                    outputStack.grow(growAmount);
                }
                drops.add(outputStack);
            }
        }
    }
    public static final String[] TIER_COLOR = new String[] {
            TextFormatting.RED.toString(), // ULV, 0
            TextFormatting.GRAY.toString(), // LV, 1
            TextFormatting.GOLD.toString(), // MV, 2
            TextFormatting.YELLOW.toString(), // HV, 3
            TextFormatting.DARK_GRAY.toString(), // EV, 4
            TextFormatting.GREEN.toString(), // IV, 5
            TextFormatting.LIGHT_PURPLE.toString(), // LuV, 6
            TextFormatting.AQUA.toString(), // ZPM, 7
            TextFormatting.DARK_GREEN.toString(), // UV, 8
            TextFormatting.DARK_RED.toString(), // UHV, 9
            TextFormatting.DARK_PURPLE.toString(), // UEV, 10
            TextFormatting.DARK_BLUE.toString() + TextFormatting.BOLD, // UIV, 11
            TextFormatting.RED.toString() + TextFormatting.BOLD + TextFormatting.UNDERLINE, // UMV, 12
            TextFormatting.DARK_RED.toString() + TextFormatting.BOLD + TextFormatting.UNDERLINE, // UXV, 13
            TextFormatting.WHITE.toString() + TextFormatting.BOLD + TextFormatting.UNDERLINE, // MAX, 14
    };
}
