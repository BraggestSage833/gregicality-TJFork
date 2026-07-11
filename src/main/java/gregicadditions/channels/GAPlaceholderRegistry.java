package gregicadditions.channels;

import gregicadditions.item.CellCasing;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.components.*;
import gregicadditions.item.metal.NuclearCasing;
import gregicadditions.machines.GATileEntities;
import gregtech.api.util.BlockInfo;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.channel.PlaceholderBlockRegistry;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;


public class GAPlaceholderRegistry {

    private static int componentBlockClamp(int value) {
        return Math.max(0, value - 1);
    }

    public static void init () {
        PlaceholderBlockRegistry.register(PlaceholderType.COIL, (context) -> new BlockInfo(GAMetaBlocks.getCoils(context.coilTier)));
        PlaceholderBlockRegistry.register(PlaceholderType.INPUT_HATCH, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(MetaTileEntities.FLUID_IMPORT_HATCH[PlaceholderBlockRegistry.IOMTEClamper(context.voltageTier)], context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.OUTPUT_HATCH, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(MetaTileEntities.FLUID_EXPORT_HATCH[PlaceholderBlockRegistry.IOMTEClamper(context.voltageTier)], context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.INPUT_BUS, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(MetaTileEntities.ITEM_IMPORT_BUS[PlaceholderBlockRegistry.IOMTEClamper(context.voltageTier)], context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.OUTPUT_BUS, (context) -> PlaceholderBlockRegistry.MTEHolderBuilder(MetaTileEntities.ITEM_EXPORT_BUS[PlaceholderBlockRegistry.IOMTEClamper(context.voltageTier)], context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.ENERGY_INPUT_HATCH, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(GATileEntities.getEnergyHatch(context.voltageTier, false), context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.ENERGY_OUTPUT_HATCH, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(GATileEntities.getEnergyHatch(context.voltageTier, true), context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.MOTOR, (context) ->  new BlockInfo(GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.CONVEYOR, (context) ->  new BlockInfo(GAMetaBlocks.CONVEYOR_CASING.getState(ConveyorCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.EMITTER, (context) ->  new BlockInfo(GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.FIELD_GEN, (context) ->  new BlockInfo(GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.PISTON, (context) ->  new BlockInfo(GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.PUMP, (context) ->  new BlockInfo(GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.ROBOT_ARM, (context) ->  new BlockInfo(GAMetaBlocks.ROBOT_ARM_CASING.getState(RobotArmCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.SENSOR, (context) ->  new BlockInfo(GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[componentBlockClamp(context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.CELL, (context) ->  new BlockInfo(GAMetaBlocks.CELL_CASING.getState(CellCasing.CellType.values()[ Math.min(Math.max(context.voltageTier - 3, 0), 12)])));
        PlaceholderBlockRegistry.register(PlaceholderType.MUFFLER, (context) ->  PlaceholderBlockRegistry.MTEHolderBuilder(GATileEntities.MUFFLER_HATCH[Math.min(7, Math.max(0, context.voltageTier - 1))], context.facing));
        PlaceholderBlockRegistry.register(PlaceholderType.FRAMEWORK, (context) ->  new BlockInfo(GAMetaBlocks.getFramework(context.voltageTier)));
        PlaceholderBlockRegistry.register(PlaceholderType.NUCLEAR_CASING, (context) -> new BlockInfo(GAMetaBlocks.NUCLEAR_CASING.getState(NuclearCasing.CasingType.values()[Math.min(11, context.voltageTier)])));
        PlaceholderBlockRegistry.register(PlaceholderType.GLASS, (context) -> new BlockInfo(GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.values()[Math.min(6, context.voltageTier)])));
    }
}
