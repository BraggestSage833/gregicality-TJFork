package gregicadditions.recipes.chain;

import static gregicadditions.GAMaterials.*;
import static gregicadditions.recipes.GARecipeMaps.CHEMICAL_DEHYDRATOR_RECIPES;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregicadditions.recipes.GARecipeMaps.LARGE_CHEMICAL_RECIPES;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import gregtech.api.unification.OreDictUnifier;


public class SeleniumChain {
    public static void init() {

        /*
         * Unknown compounds key:
         * ElectricallyImpureCopper: Cu
         * CopperRefiningSolution: CuH2SO4
         * AnodicSlime: TeSe
         * SelenateTellurateMix: TeSe(H2Na2CO3)2
         * SelenateSolution: SeH2CO3
         */

        // CuFeS2 + SiO2 + 5O -> Cu(EIC) + FeSiO3 + 2SO2
        BLAST_RECIPES.recipeBuilder().duration(240).EUt(120).blastFurnaceTemp(1500)
                .input(crushedCentrifuged, Chalcopyrite)
                .input(dust, SiliconDioxide, 3)
                .fluidInputs(Oxygen.getFluid(5000))
                .output(dust, ElectricallyImpureCopper)
                .output(dust, Ferrosilite, 5)
                .fluidOutputs(SulfurDioxide.getFluid(2000))
                .buildAndRegister();

        // Cu + H2SO4 -> CuH2SO4
        CHEMICAL_RECIPES.recipeBuilder().duration(200).EUt(120)
                .input(dust, ElectricallyImpureCopper)
                .fluidInputs(SulfuricAcid.getFluid(1000))
                .fluidOutputs(CopperRefiningSolution.getFluid(1000))
                .buildAndRegister();

        // 2Cu + CuH2SO4 -> H2SO4 + 3Cu + TeSe(75%)
        ELECTROLYZER_RECIPES.recipeBuilder().duration(450).EUt(120)
                .input(plate, ElectricallyImpureCopper, 2)
                .fluidInputs(CopperRefiningSolution.getFluid(1000))
                .fluidOutputs(SulfuricAcid.getFluid(1000))
                .output(ingot, Copper, 3)
                .chancedOutput(AnodicSlime.getItemStack(), 7500, 0)
                .buildAndRegister();

        // TeSe + 2Na2CO3 + 4O -> TeO2SeO2(Na2CO3)2
        BLAST_RECIPES.recipeBuilder().duration(320).EUt(120).blastFurnaceTemp(2100)
                .inputs(AnodicSlime.getItemStack())
                .input(dust, SodaAsh, 12)
                .fluidInputs(Oxygen.getFluid(4000))
                .chancedOutput(OreDictUnifier.get(dust, PreciousMetal, 1), 5555, 0)
                .fluidOutputs(SeleniteTelluriteMix.getFluid(1000))
                .buildAndRegister();

        // TeO2SeO2(Na2CO3)2 + H2SO4 -> TeO2 + Na2SO4 + Na2SeO3 + CO2 + H2O
        LARGE_CHEMICAL_RECIPES.recipeBuilder().duration(270).EUt(1920)
                .fluidInputs(SeleniteTelluriteMix.getFluid(1000))
                .fluidInputs(SulfuricAcid.getFluid(1000))
                .outputs(TelluriumOxide.getItemStack(3))
                .fluidOutputs(Water.getFluid(1000))
                .fluidOutputs(SulfurTrioxide.getFluid(1000))
                .fluidOutputs(CarbonDioxide.getFluid(1000))
                .fluidOutputs(SeleniteSolution.getFluid(1000))
                .buildAndRegister();

        // Na2SeO3 + 2HCl -> 2NaCl + SeO2 + H2O
        CHEMICAL_DEHYDRATOR_RECIPES.recipeBuilder().duration(240).EUt(480)
                .fluidInputs(SeleniteSolution.getFluid(1000))
                .fluidInputs(HydrochloricAcid.getFluid(2000))
                .output(dust, Salt, 4)
                .outputs(SeleniumOxide.getItemStack(3))
                .buildAndRegister();

        // SeO2 + 2SO2 -> Se + 2SO3
        CHEMICAL_RECIPES.recipeBuilder().duration(260).EUt(120)
                .inputs(SeleniumOxide.getItemStack(3))
                .fluidInputs(SulfurDioxide.getFluid(2000))
                .output(dust, Selenium)
                .fluidOutputs(SulfurTrioxide.getFluid(2000))
                .buildAndRegister();

        // TeO2 + 2SO2 -> Te + 2SO3
        CHEMICAL_RECIPES.recipeBuilder().duration(260).EUt(120)
                .inputs(TelluriumOxide.getItemStack(3))
                .fluidInputs(SulfurDioxide.getFluid(2000))
                .output(dust, Tellurium)
                .fluidOutputs(SulfurTrioxide.getFluid(2000))
                .buildAndRegister();
    }
}
