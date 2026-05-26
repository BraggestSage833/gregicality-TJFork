package gregicadditions.sound;


import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.common.sound.GTSoundEvents;
import net.minecraft.init.SoundEvents;

// Haven't really found the issue yet, but somehow Gregicality makes every GregTech core mod's RecipeMap member variable sound null. This simply sets them back.
// If found we can just yeet this away. :D
public class RecipeMapSoundFixer {
    public static void apply() {
        RecipeMaps.COMPRESSOR_RECIPES.setSound(GTSoundEvents.COMPRESSOR);
        RecipeMaps.EXTRACTOR_RECIPES.setSound(GTSoundEvents.CENTRIFUGE);
        RecipeMaps.MACERATOR_RECIPES.setSound(GTSoundEvents.MACERATOR);
        RecipeMaps.ORE_WASHER_RECIPES.setSound(GTSoundEvents.BATH);
        RecipeMaps.THERMAL_CENTRIFUGE_RECIPES.setSound(GTSoundEvents.CENTRIFUGE);
        RecipeMaps.FURNACE_RECIPES.setSound(GTSoundEvents.FURNACE);
        RecipeMaps.MICROWAVE_RECIPES.setSound(GTSoundEvents.FURNACE);
        RecipeMaps.ASSEMBLER_RECIPES.setSound(GTSoundEvents.ASSEMBLER);
        RecipeMaps.FORMING_PRESS_RECIPES.setSound(GTSoundEvents.FORGE_HAMMER);
        RecipeMaps.FLUID_CANNER_RECIPES.setSound(GTSoundEvents.BATH);
        RecipeMaps.PLASMA_ARC_FURNACE_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.ARC_FURNACE_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.SIFTER_RECIPES.setSound(SoundEvents.BLOCK_SAND_PLACE);
        RecipeMaps.LASER_ENGRAVER_RECIPES.setSound(GTSoundEvents.ELECTROLYZER);
        RecipeMaps.MIXER_RECIPES.setSound(GTSoundEvents.MIXER);
        RecipeMaps.AUTOCLAVE_RECIPES.setSound(GTSoundEvents.FURNACE);
        RecipeMaps.ELECTROMAGNETIC_SEPARATOR_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.POLARIZER_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.CHEMICAL_BATH_RECIPES.setSound(GTSoundEvents.BATH);
        RecipeMaps.BREWING_RECIPES.setSound(GTSoundEvents.BATH);
        RecipeMaps.FLUID_HEATER_RECIPES.setSound(GTSoundEvents.BOILER);
        RecipeMaps.DISTILLERY_RECIPES.setSound(GTSoundEvents.CHEMICAL_REACTOR);
        RecipeMaps.FERMENTING_RECIPES.setSound(GTSoundEvents.CHEMICAL_REACTOR);
        RecipeMaps.FLUID_SOLIDFICATION_RECIPES.setSound(GTSoundEvents.COOLING);
        RecipeMaps.FLUID_EXTRACTION_RECIPES.setSound(GTSoundEvents.CENTRIFUGE);
        RecipeMaps.FUSION_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.CENTRIFUGE_RECIPES.setSound(GTSoundEvents.CENTRIFUGE);
        RecipeMaps.ELECTROLYZER_RECIPES.setSound(GTSoundEvents.ELECTROLYZER);
        RecipeMaps.BLAST_RECIPES.setSound(GTSoundEvents.FIRE);
        RecipeMaps.IMPLOSION_RECIPES.setSound(SoundEvents.ENTITY_GENERIC_EXPLODE);
        RecipeMaps.VACUUM_RECIPES.setSound(GTSoundEvents.COOLING);
        RecipeMaps.CHEMICAL_RECIPES.setSound(GTSoundEvents.CHEMICAL_REACTOR);
        RecipeMaps.DISTILLATION_RECIPES.setSound(GTSoundEvents.CHEMICAL_REACTOR);
        RecipeMaps.CRACKING_RECIPES.setSound(GTSoundEvents.FIRE);
        RecipeMaps.PYROLYSE_RECIPES.setSound(GTSoundEvents.FIRE);
        RecipeMaps.WIREMILL_RECIPES.setSound(GTSoundEvents.MOTOR);
        RecipeMaps.BENDER_RECIPES.setSound(GTSoundEvents.MOTOR);
        RecipeMaps.ALLOY_SMELTER_RECIPES.setSound(GTSoundEvents.FURNACE);
        RecipeMaps.CANNER_RECIPES.setSound(GTSoundEvents.BATH);
        RecipeMaps.LATHE_RECIPES.setSound(GTSoundEvents.CUT);
        RecipeMaps.CUTTER_RECIPES.setSound(GTSoundEvents.CUT);
        RecipeMaps.EXTRUDER_RECIPES.setSound(GTSoundEvents.ARC);
        RecipeMaps.FORGE_HAMMER_RECIPES.setSound(GTSoundEvents.FORGE_HAMMER);
        RecipeMaps.PACKER_RECIPES.setSound(GTSoundEvents.ASSEMBLER);
        RecipeMaps.UNPACKER_RECIPES.setSound(GTSoundEvents.ASSEMBLER);
        RecipeMaps.AMPLIFIERS.setSound(GTSoundEvents.ARC);
        RecipeMaps.DIESEL_GENERATOR_FUELS.setSound(GTSoundEvents.COMBUSTION);
        RecipeMaps.PLASMA_GENERATOR_FUELS.setSound(GTSoundEvents.ARC);
        RecipeMaps.SEMI_FLUID_GENERATOR_FUELS.setSound(GTSoundEvents.COMBUSTION);
        RecipeMaps.GAS_TURBINE_FUELS.setSound(GTSoundEvents.TURBINE);
        RecipeMaps.STEAM_TURBINE_FUELS.setSound(GTSoundEvents.COMBUSTION);


    }
}
