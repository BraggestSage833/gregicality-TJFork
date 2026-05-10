package gregicadditions.recipes.chain;


import gregicadditions.GAEnums;
import gregicadditions.GAValues;
import gregicadditions.item.GAMetaItem;
import gregicadditions.item.GAMetaItems;
import gregicadditions.machines.GATileEntities;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.type.IngotMaterial;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.common.items.MetaItems;
import li.cil.oc.common.component.GpuTextBuffer;
import net.minecraft.item.ItemStack;
import scala.tools.cmd.Meta;

import static gregicadditions.GAMaterials.*;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.MarkerMaterials.Tier.Elite;
import static gregtech.api.unification.material.Materials.*;
import static gregicadditions.recipes.GARecipeMaps.*;
import static gregtech.api.unification.ore.OrePrefix.*;

public class Pcb_PolySyn {

    public static void init() {
        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 5 * 16)
                .fluidInputs(Hydrogen.getFluid(8000 * 16))
                .notConsumable(new IntCircuitIngredient(1))
                .fluidOutputs(Rubber.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.IV]) - (GAValues.V[GAValues.HV]))
                .buildAndRegister();


        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .fluidInputs(Hydrogen.getFluid(4000 * 16))
                .notConsumable(new IntCircuitIngredient(2))
                .fluidOutputs(Plastic.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.ZPM]) - (GAValues.V[GAValues.IV]))
                .buildAndRegister();


        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .fluidInputs(Hydrogen.getFluid(3000 * 16))
                .fluidInputs(Chlorine.getFluid(1000 * 16))
                .notConsumable(new IntCircuitIngredient(3))
                .fluidOutputs(PolyvinylChloride.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UV]) - (GAValues.V[GAValues.IV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .fluidInputs(Fluorine.getFluid(4000 * 16))
                .notConsumable(new IntCircuitIngredient(4))
                .fluidOutputs(Polytetrafluoroethylene.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UV]) - (GAValues.V[GAValues.IV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .fluidInputs(Hydrogen.getFluid(4000 * 16))
                .fluidInputs(Oxygen.getFluid(1000 * 16))
                .notConsumable(new IntCircuitIngredient(5))
                .fluidOutputs(Epoxid.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UV]) - (GAValues.V[GAValues.IV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 6 * 16)
                .input(dust, Sulfur, 16)
                .fluidInputs(Hydrogen.getFluid(4000 * 16))
                .notConsumable(new IntCircuitIngredient(6))
                .fluidOutputs(PolyphenyleneSulfide.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UV]) - (GAValues.V[GAValues.IV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .input(dust, Silicon, 16)
                .fluidInputs(Hydrogen.getFluid(6000 * 16))
                .fluidInputs(Oxygen.getFluid(1000 * 16))
                .notConsumable(new IntCircuitIngredient(7))
                .fluidOutputs(SiliconeRubber.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UHV]) - (GAValues.V[GAValues.ZPM]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 20 * 16)
                .fluidInputs(Hydrogen.getFluid(26000 * 16))
                .notConsumable(new IntCircuitIngredient(8))
                .fluidOutputs(StyreneButadieneRubber.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UHV]) - (GAValues.V[GAValues.ZPM]))
                .buildAndRegister();


        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 2 * 16)
                .fluidInputs(Hydrogen.getFluid(8000 * 16))
                .notConsumable(new IntCircuitIngredient(9))
                .fluidOutputs(Polystyrene.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UHV]) - (GAValues.V[GAValues.ZPM]))
                .buildAndRegister();


        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 20 * 16)
                .fluidInputs(Hydrogen.getFluid(12000 * 16))
                .fluidInputs(Nitrogen.getFluid(4000 * 16))
                .notConsumable(new IntCircuitIngredient(10))
                .fluidOutputs(Polybenzimidazole.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UEV]) - (GAValues.V[GAValues.UV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 20 * 16)
                .fluidInputs(Hydrogen.getFluid(12000 * 16))
                .fluidInputs(Oxygen.getFluid(3000 * 16))
                .notConsumable(new IntCircuitIngredient(11))
                .fluidOutputs(Polyetheretherketone.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UEV]) - (GAValues.V[GAValues.UV]))
                .buildAndRegister();


        POLYMERS_SYN.recipeBuilder()
                .input(dust, Carbon, 14 * 16)
                .fluidInputs(Hydrogen.getFluid(6000 * 16))
                .fluidInputs(Nitrogen.getFluid(2000 * 16))
                .fluidInputs(Oxygen.getFluid(2000 * 16))
                .notConsumable(new IntCircuitIngredient(12))
                .fluidOutputs(Zylon.getFluid(1000 * 16))
                .duration(200)
                .EUt((GAValues.V[GAValues.UMV]) - (GAValues.V[GAValues.UV]))
                .buildAndRegister();

        POLYMERS_SYN.recipeBuilder()
                .input(dust, Palladium, 16)
                .input(dust, Iron, 16)
                .input(dust, Carbon, 153 * 16)
                .fluidInputs(Hydrogen.getFluid(36000 * 16))
                .fluidInputs(Nitrogen.getFluid(1000 * 16))
                .fluidInputs(Oxygen.getFluid(2000 * 16))
                .notConsumable(new IntCircuitIngredient(13))
                .fluidOutputs(FullerenePolymerMatrix.getFluid(1000 * 16))
                .duration(200 * 4)
                .EUt((GAValues.V[GAValues.UXV]) - (GAValues.V[GAValues.UV]))
                .buildAndRegister();


        ASSEMBLY_LINE_RECIPES.recipeBuilder()
                .inputs(GATileEntities.CHEMICAL_PLANT.getStackForm(4))
                .inputs(MetaItems.EMITTER_ZPM.getStackForm(64))
                .inputs(MetaItems.EMITTER_ZPM.getStackForm(64))
                .inputs(MetaItems.HIGH_POWER_INTEGRATED_CIRCUIT.getStackForm(64))
                .inputs(MetaItems.HIGH_POWER_INTEGRATED_CIRCUIT.getStackForm(64))
                .input(circuit, MarkerMaterials.Tier.Infinite)
                .input(circuit, MarkerMaterials.Tier.Superconductor)
                .input(circuit, MarkerMaterials.Tier.Ultimate)
                .input(circuit, Elite)
                .input(circuit, MarkerMaterials.Tier.Extreme)
                .fluidInputs(Lubricant.getFluid(5000))
                .fluidInputs(Polybenzimidazole.getFluid(8000))
                .fluidInputs(SolderingAlloy.getFluid(12000))
                .outputs(GATileEntities.POLY_SYN.getStackForm())
                .duration(600 * 4)
                .EUt((GAValues.V[GAValues.UV]) - (GAValues.V[GAValues.EV]))
                .buildAndRegister();

        IngotMaterial[] polymer = {Plastic, PolyvinylChloride, PolyvinylChloride, Epoxid, ReinforcedEpoxyResin, PolyphenyleneSulfide, SiliconeRubber, StyreneButadieneRubber, Polystyrene, Polybenzimidazole, Polyimide, Polyetheretherketone, Zylon, FullerenePolymerMatrix};

        PCB_FACTORY.recipeBuilder()
                .input(dust, Wood, 8)
                .input(foil, Bronze, (8 * 4))
                .fluidInputs(IronChloride.getFluid(500))
                .fluidInputs(Glue.getFluid(500))
                .notConsumable(new IntCircuitIngredient(1))
                .outputs(GAMetaItems.GOOD_PHENOLIC_BOARD.getStackForm(8))
                .duration(200)
                .EUt((GAValues.V[GAValues.LV]) - (GAValues.V[GAValues.ULV]))
                .buildAndRegister();

        PCB_FACTORY.recipeBuilder()
                .input(dust, Wood, 8)
                .input(foil, Bronze, (8 * 4))
                .fluidInputs(IronChloride.getFluid(500))
                .fluidInputs(Glue.getFluid(500))
                .notConsumable(new IntCircuitIngredient(1))
                .outputs(GAMetaItems.GOOD_PHENOLIC_BOARD.getStackForm(8))
                .duration(200)
                .EUt((GAValues.V[GAValues.LV]) - (GAValues.V[GAValues.ULV]))
                .buildAndRegister();
//Start of PCB factory recipes
        //tier 1
        for (int i = 0; i < polymer.length; i++) {

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Copper, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.GOOD_PHENOLIC_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.LV]) - (GAValues.V[GAValues.ULV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Copper, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.GOOD_PHENOLIC_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.LV]) - (GAValues.V[GAValues.ULV]))
                    .buildAndRegister();
            //tier 2
        }
        for (int i = 1; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, AnnealedCopper, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.GOOD_PLASTIC_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.MV]) - (GAValues.V[GAValues.ULV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, AnnealedCopper, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.GOOD_PLASTIC_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.MV]) - (GAValues.V[GAValues.ULV]))
                    .buildAndRegister();
        }
        //tier 3
        for (int i = 2; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Electrum, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.ADVANCED_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.HV]) - (GAValues.V[GAValues.LV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Electrum, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.ADVANCED_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.HV]) - (GAValues.V[GAValues.LV]))
                    .buildAndRegister();
        }
        //tier 4
        for (int i = 3; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Palladium, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.EXTREME_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.EV]) - (GAValues.V[GAValues.LV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Palladium, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.EXTREME_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.EV]) - (GAValues.V[GAValues.LV]))
                    .buildAndRegister();
        }
        //tier 5
        for (int i = 4; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Platinum, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.ELITE_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.IV]) - (GAValues.V[GAValues.MV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Platinum, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.ELITE_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.IV]) - (GAValues.V[GAValues.MV]))
                    .buildAndRegister();
        }
        //tier 6
        for (int i = 5; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Iridium, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .outputs(GAMetaItems.KAPTON_CIRCUIT_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.LuV]) - (GAValues.V[GAValues.HV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Iridium, (8 * Math.max(4, 4 * i)))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .outputs(GAMetaItems.KAPTON_CIRCUIT_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.LuV]) - (GAValues.V[GAValues.HV]))
                    .buildAndRegister();
        }
        //tier 7

        for (int i = 6; i < polymer.length; i++) {
            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Osmium, (8 * Math.max(4, 4 * i)))
                    .input(foil, NiobiumTitanium, (8 * Math.max(4, 4 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 1))
                    .inputs(GAMetaItems.PETRI_DISH.getStackForm(Math.max(4, 4 * i)))
                    .inputs(MetaItems.ELECTRIC_PUMP_LV.getStackForm(Math.max(4, 4 * i)))
                    .inputs(MetaItems.SENSOR_LV.getStackForm(Math.max(4, 4 * i)))
                    .input(circuit, MarkerMaterials.Tier.Good, Math.max(4, 4 * i))
                    .fluidInputs(IronChloride.getFluid(Math.max(250, 250 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SterileGrowthMedium.getFluid(Math.max(250, 250 * i)))
                    .outputs(GAMetaItems.MASTER_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.UEV]) - (GAValues.V[GAValues.UV]))
                    .buildAndRegister();

            PCB_FACTORY.recipeBuilder()
                    .input(plate, polymer[i], Math.max(4, 4 * i))
                    .input(foil, Osmium, (8 * Math.max(4, 4 * i)))
                    .input(foil, NiobiumTitanium, (8 * Math.max(4, 4 * i)))
                    .notConsumable(new IntCircuitIngredient(i + 2))
                    .inputs(GAMetaItems.PETRI_DISH.getStackForm(Math.max(4, 4 * i)))
                    .inputs(MetaItems.ELECTRIC_PUMP_LV.getStackForm(Math.max(4, 4 * i)))
                    .inputs(MetaItems.SENSOR_LV.getStackForm(Math.max(4, 4 * i)))
                    .input(circuit, MarkerMaterials.Tier.Good, Math.max(4, 4 * i))
                    .fluidInputs(SodiumPersulfate.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SulfuricAcid.getFluid(Math.max(500, 500 * i)))
                    .fluidInputs(SterileGrowthMedium.getFluid(Math.max(250, 250 * i)))
                    .outputs(GAMetaItems.MASTER_BOARD.getStackForm(Math.max(8, 8 * i)))
                    .duration(200)
                    .EUt((GAValues.V[GAValues.UEV]) - (GAValues.V[GAValues.UV]))
                    .buildAndRegister();
        }
    }

}

