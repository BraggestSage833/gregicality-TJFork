package gregicadditions.recipes.categories;


import gregicadditions.machines.GATileEntities;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import static gregicadditions.GAMaterials.*;
import static gregicadditions.item.GAMetaItems.DEGENERATE_RHENIUM_DUST;
import static gregicadditions.recipes.GARecipeMaps.BLAST_ALLOY_RECIPES;
import static gregicadditions.recipes.GARecipeMaps.LARGE_MIXER_RECIPES;
import static gregtech.api.GTValues.L;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.items.MetaItems.PLANT_BALL;
import static gregtech.common.metatileentities.MetaTileEntities.*;
import static gregicadditions.machines.GATileEntities.*;

public class MiscRecipes {

    public static void init() {

        // Manual Potin Dust
        ModHandler.addShapelessRecipe("ga_potin_dust", OreDictUnifier.get(dust, Potin, 5),
                new UnificationEntry(dust, Lead),
                new UnificationEntry(dust, Lead),
                new UnificationEntry(dust, Bronze),
                new UnificationEntry(dust, Bronze),
                new UnificationEntry(dust, Tin));

        // Staballoy Dust
        MIXER_RECIPES.recipeBuilder().duration(50).EUt(16)
                .input(dust, UraniumRadioactive.getMaterial(), 9)
                .input(dust, Titanium)
                .output(dust, Staballoy, 10)
                .buildAndRegister();

        // Quantum Dust
        LARGE_MIXER_RECIPES.recipeBuilder().duration(10500).EUt(30)
                .input(dust, Stellite, 15)
                .input(dust, Jasper, 5)
                .input(dust, Gallium, 5)
                .input(dust, Americium241.getMaterial(), 5)
                .input(dust, Palladium, 5)
                .input(dust, Bismuth, 5)
                .input(dust, Germanium, 5)
                .inputs(SiliconCarbide.getItemStack(5))
                .output(dust, Quantum, 50)
                .buildAndRegister();

        // Eglin Steel
        MIXER_RECIPES.recipeBuilder().EUt(32).duration(100)
                .input(dust, Iron, 4)
                .input(dust, Kanthal)
                .input(dust, Invar, 5)
                .output(dust, EglinSteelBase, 10)
                .buildAndRegister();

        LARGE_MIXER_RECIPES.recipeBuilder().EUt(128).duration(100)
                .input(dust, Iron, 4)
                .input(dust, Kanthal)
                .input(dust, Invar, 5)
                .input(dust, Sulfur)
                .input(dust, Silicon)
                .input(dust, Carbon)
                .notConsumable(new IntCircuitIngredient(6))
                .output(dust, EglinSteel, 13)
                .buildAndRegister();

        // Incoloy MA956
        LARGE_MIXER_RECIPES.recipeBuilder()
                .notConsumable(new IntCircuitIngredient(4))
                .input(dust, Iron, 16)
                .input(dust, Aluminium, 3)
                .input(dust, Chrome, 5)
                .input(dust, Yttrium, 1)
                .output(dust, IncoloyMA956, 25)
                .EUt(500)
                .duration(100)
                .buildAndRegister();

        // Pyrolyse Oven Charcoal from Sugar
        PYROLYSE_RECIPES.recipeBuilder().duration(640).EUt(64)
                .inputs(new ItemStack(Items.SUGAR, 23))
                .circuitMeta(1)
                .output(dust, Charcoal, 12)
                .fluidOutputs(Water.getFluid(1500))
                .buildAndRegister();

        // Pyrolyse Oven Fermented Biomass
        PYROLYSE_RECIPES.recipeBuilder().duration(200).EUt(10)
                .inputs(PLANT_BALL.getStackForm())
                .circuitMeta(2)
                .fluidInputs(Water.getFluid(1500))
                .chancedOutput(PLANT_BALL.getStackForm(), 1000, 100)
                .fluidOutputs(FermentedBiomass.getFluid(1500))
                .buildAndRegister();

        // Ender Pearl
        MIXER_RECIPES.recipeBuilder().EUt(300).duration(880)
                .input(dust, Beryllium)
                .input(dust, Potassium, 4)
                .fluidInputs(Nitrogen.getFluid(5000))
                .output(dust, EnderPearl, 10)
                .buildAndRegister();

        // Silicon Carbide
        BLAST_RECIPES.recipeBuilder().EUt(120).duration(3000).blastFurnaceTemp(2500)
                .input(dust, Silicon)
                .input(dust, Carbon)
                .notConsumable(new IntCircuitIngredient(2))
                .notConsumable(Argon.getFluid(0))
                .outputs(SiliconCarbide.getItemStack(2))
                .buildAndRegister();

        // Diamond Implosion Recipe
        IMPLOSION_RECIPES.recipeBuilder().EUt(30).duration(20).explosivesAmount(48)
                .input(dust, Diamond, 4)
                .output(gem, Diamond, 3)
                .output(dustTiny, DarkAsh, 2)
                .buildAndRegister();

        // Redstone and Glowstone Fluid Extraction
        FLUID_EXTRACTION_RECIPES.recipeBuilder().duration(80).EUt(32)
                .input(dust, Redstone)
                .fluidOutputs(Redstone.getFluid(L))
                .buildAndRegister();

        FLUID_EXTRACTION_RECIPES.recipeBuilder().duration(80).EUt(32)
                .input(dust, Glowstone)
                .fluidOutputs(Glowstone.getFluid(L))
                .buildAndRegister();

        // Mixing Dust Mixer Recipes
        // Soldering Alloy
        MIXER_RECIPES.recipeBuilder().duration(10 * 50).EUt(16)
                .input(dust, Tin, 9)
                .input(dust, Antimony)
                .output(dust, SolderingAlloy, 10)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();

        // Red Alloy
        MIXER_RECIPES.recipeBuilder().duration(50).EUt(16)
                .input(dust, Redstone, 3)
                .input(dust, Copper)
                .output(dust, RedAlloy)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();

        // Magnalium
        MIXER_RECIPES.recipeBuilder().duration(3 * 50).EUt(16)
                .input(dust, Aluminium, 2)
                .input(dust, Magnesium)
                .output(dust, Magnalium,3)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();

        // Tin Alloy
        MIXER_RECIPES.recipeBuilder().duration(2 * 50).EUt(16)
                .input(dust, Tin)
                .input(dust, Iron)
                .output(dust, TinAlloy, 2)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();

        MIXER_RECIPES.recipeBuilder().duration(2 * 50).EUt(16)
                .input(dust, Tin)
                .input(dust, WroughtIron)
                .output(dust, TinAlloy, 2)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();

        // Battery Alloy
        MIXER_RECIPES.recipeBuilder().duration(5 * 50).EUt(16)
                .input(dust, Lead, 4)
                .input(dust, Antimony)
                .output(dust, BatteryAlloy, 5)
                .notConsumable(new IntCircuitIngredient(2))
                .buildAndRegister();
     
        //Uranium recipe for EV Super conductor Base
        MIXER_RECIPES.recipeBuilder().duration(820).EUt(30)
            .input(dust, Platinum, 3)
            .input(dust, UraniumRadioactive.getMaterial())
            .output(dust, EVSuperconductorBase, 4)
            .buildAndRegister();

        ModHandler.addShapelessRecipe("drum_nbt_wood", GATileEntities.WOODEN_DRUM.getStackForm(), GATileEntities.WOODEN_DRUM.getStackForm());
        ModHandler.addShapelessRecipe("drum_nbt_bronze", GATileEntities.BRONZE_DRUM.getStackForm(), GATileEntities.BRONZE_DRUM.getStackForm());
        ModHandler.addShapelessRecipe("drum_nbt_steel", GATileEntities.STEEL_DRUM.getStackForm(), GATileEntities.STEEL_DRUM.getStackForm());
        ModHandler.addShapelessRecipe("drum_nbt_stainless_steel", GATileEntities.STAINLESS_STEEL_DRUM.getStackForm(), GATileEntities.STAINLESS_STEEL_DRUM.getStackForm());
        ModHandler.addShapelessRecipe("drum_nbt_titanium", GATileEntities.TITANIUM_DRUM.getStackForm(), GATileEntities.TITANIUM_DRUM.getStackForm());
        ModHandler.addShapelessRecipe("drum_nbt_Tungsten_steel", GATileEntities.TUNGSTENSTEEL_DRUM.getStackForm(), GATileEntities.TUNGSTENSTEEL_DRUM.getStackForm());






            for (int i = 0; i < FLUID_EXPORT_HATCH.length; i++) {
                if (FLUID_IMPORT_HATCH[i] != null && FLUID_EXPORT_HATCH[i] != null) {

                    ModHandler.addShapedRecipe("fluid_hatch_output_to_input_" + FLUID_IMPORT_HATCH[i].getTier(), FLUID_IMPORT_HATCH[i].getStackForm(),
                            "d", "B", 'B', FLUID_EXPORT_HATCH[i].getStackForm());
                    ModHandler.addShapedRecipe("fluid_hatch_input_to_output_" + FLUID_EXPORT_HATCH[i].getTier(), FLUID_EXPORT_HATCH[i].getStackForm(),
                            "d", "B", 'B', FLUID_IMPORT_HATCH[i].getStackForm());
                }
            }
            for (int i = 0; i < ITEM_IMPORT_BUS.length; i++) {
                if (ITEM_IMPORT_BUS[i] != null && ITEM_EXPORT_BUS[i] != null) {

                    ModHandler.addShapedRecipe("item_bus_output_to_input_" + ITEM_IMPORT_BUS[i].getTier(), ITEM_IMPORT_BUS[i].getStackForm(),
                            "d", "B", 'B', ITEM_EXPORT_BUS[i].getStackForm());
                    ModHandler.addShapedRecipe("item_bus_input_to_output_" + ITEM_EXPORT_BUS[i].getTier(), ITEM_EXPORT_BUS[i].getStackForm(),
                            "d", "B", 'B', ITEM_IMPORT_BUS[i].getStackForm());
                }
            }
        if (INPUT_HATCH_MULTI != null && OUTPUT_HATCH_MULTI != null) {

            ModHandler.addShapedRecipe("multi_fluid_hatch_output_to_input_" + INPUT_HATCH_MULTI.get(1).getTier(), INPUT_HATCH_MULTI.get(1).getStackForm(),
                    "d", "B", 'B', OUTPUT_HATCH_MULTI.get(1).getStackForm());
            ModHandler.addShapedRecipe("multi_fluid_hatch_input_to_output_" + OUTPUT_HATCH_MULTI.get(1).getTier(), OUTPUT_HATCH_MULTI.get(1).getStackForm(),
                    "d", "B", 'B', INPUT_HATCH_MULTI.get(1).getStackForm());
            ModHandler.addShapedRecipe("multi_fluid_hatch_output_to_input_" + INPUT_HATCH_MULTI.get(0).getTier(), INPUT_HATCH_MULTI.get(0).getStackForm(),
                    "d", "B", 'B', OUTPUT_HATCH_MULTI.get(0).getStackForm());
            ModHandler.addShapedRecipe("multi_fluid_hatch_input_to_output_" + OUTPUT_HATCH_MULTI.get(0).getTier(), OUTPUT_HATCH_MULTI.get(0).getStackForm(),
                    "d", "B", 'B', INPUT_HATCH_MULTI.get(0).getStackForm());
        }




        if (STEAM_OUTPUT_BUS != null && STEAM_INPUT_BUS != null) {
                //Steam
                ModHandler.addShapedRecipe("steam_bus_output_to_input_" + STEAM_OUTPUT_BUS.getTier(), STEAM_OUTPUT_BUS.getStackForm(),
                        "d", "B", 'B', STEAM_INPUT_BUS.getStackForm());
                ModHandler.addShapedRecipe("steam_bus_input_to_output_" + STEAM_INPUT_BUS.getTier(), STEAM_INPUT_BUS.getStackForm(),
                        "d", "B", 'B', STEAM_OUTPUT_BUS.getStackForm());

        }
    }
}
