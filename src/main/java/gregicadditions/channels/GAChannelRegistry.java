package gregicadditions.channels;


import gregicadditions.item.CellCasing;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.components.*;
import gregicadditions.item.fusion.GACryostatCasing;
import gregicadditions.item.fusion.GADivertorCasing;
import gregicadditions.item.fusion.GAFusionCasing;
import gregicadditions.item.fusion.GAVacuumCasing;
import gregicadditions.item.metal.NuclearCasing;
import gregicadditions.machines.GATileEntities;
import gregtech.api.util.BlockInfo;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.channels.ChannelRegistry;
import gregtech.integration.jei.multiblock.channel.Channel;

import gregtech.integration.jei.multiblock.channel.PlaceholderType;

import java.util.List;


public final class GAChannelRegistry {
    public static final Channel COMPONENT_CHANNEL = Channel.createDriver("component");
    public static final Channel FUSION_PARTS_CHANNEL = Channel.create("fusion parts");
    public static final Channel CELL_CHANNEL = Channel.create("cell");

    public static final PlaceholderType MOTOR = PlaceholderType.create("motor");
    public static final PlaceholderType CONVEYOR = PlaceholderType.create("conveyor");
    public static final PlaceholderType EMITTER = PlaceholderType.create("emitter");
    public static final PlaceholderType FIELD_GEN = PlaceholderType.create("field generator");
    public static final PlaceholderType PISTON = PlaceholderType.create("piston");
    public static final PlaceholderType PUMP = PlaceholderType.create("pump");
    public static final PlaceholderType ROBOT_ARM = PlaceholderType.create("robot arm");
    public static final PlaceholderType SENSOR = PlaceholderType.create("sensor");

    public static final PlaceholderType CELL = PlaceholderType.create("cell");
    public static final PlaceholderType NUCLEAR_CASING = PlaceholderType.create("nuclear casing");
    public static final PlaceholderType GLASS = PlaceholderType.create("glass");
    public static final PlaceholderType MUFFLER = PlaceholderType.create("muffler");
    public static final PlaceholderType FRAMEWORK = PlaceholderType.create("framework");

    public static final PlaceholderType FUSION_COIL = PlaceholderType.create("fusion coil");
    public static final PlaceholderType CRYOSTAT_CASING = PlaceholderType.create("cryostat casing");
    public static final PlaceholderType VACUUM_CASING = PlaceholderType.create("vacuum casing");
    public static final PlaceholderType DIVERTOR_CASING = PlaceholderType.create("divertor casing");


    public static void init() {
        MOTOR.registerVariant(COMPONENT_CHANNEL, MotorCasing.CasingType.values(),
                type -> GAMetaBlocks.MOTOR_CASING.getItemVariant(type),
                type -> GAMetaBlocks.MOTOR_CASING.getState(type)
        );
        CONVEYOR.registerVariant(COMPONENT_CHANNEL, ConveyorCasing.CasingType.values(),
                type -> GAMetaBlocks.CONVEYOR_CASING.getItemVariant(type),
                type -> GAMetaBlocks.CONVEYOR_CASING.getState(type)
        );

        EMITTER.registerVariant(COMPONENT_CHANNEL, EmitterCasing.CasingType.values(),
                type -> GAMetaBlocks.EMITTER_CASING.getItemVariant(type),
                type -> GAMetaBlocks.EMITTER_CASING.getState(type)
        );

        FIELD_GEN.registerVariant(COMPONENT_CHANNEL, FieldGenCasing.CasingType.values(),
                type -> GAMetaBlocks.FIELD_GEN_CASING.getItemVariant(type),
                type -> GAMetaBlocks.FIELD_GEN_CASING.getState(type)
        );

        PISTON.registerVariant(COMPONENT_CHANNEL, PistonCasing.CasingType.values(),
                type -> GAMetaBlocks.PISTON_CASING.getItemVariant(type),
                type -> GAMetaBlocks.PISTON_CASING.getState(type)
        );

        PUMP.registerVariant(COMPONENT_CHANNEL, PumpCasing.CasingType.values(),
                type -> GAMetaBlocks.PUMP_CASING.getItemVariant(type),
                type -> GAMetaBlocks.PUMP_CASING.getState(type)
        );

        ROBOT_ARM.registerVariant(COMPONENT_CHANNEL, RobotArmCasing.CasingType.values(),
                type -> GAMetaBlocks.ROBOT_ARM_CASING.getItemVariant(type),
                type -> GAMetaBlocks.ROBOT_ARM_CASING.getState(type)
        );

        SENSOR.registerVariant(COMPONENT_CHANNEL, SensorCasing.CasingType.values(),
                type -> GAMetaBlocks.SENSOR_CASING.getItemVariant(type),
                type -> GAMetaBlocks.SENSOR_CASING.getState(type)
        );

        CRYOSTAT_CASING.registerVariant(FUSION_PARTS_CHANNEL, GACryostatCasing.CasingType.values(),
                type -> GAMetaBlocks.CRYOSTAT_CASING.getItemVariant(type),
                type -> GAMetaBlocks.CRYOSTAT_CASING.getState(type)
        );
        VACUUM_CASING.registerVariant(FUSION_PARTS_CHANNEL, GAVacuumCasing.CasingType.values(),
                type -> GAMetaBlocks.VACUUM_CASING.getItemVariant(type),
                type -> GAMetaBlocks.VACUUM_CASING.getState(type));

        DIVERTOR_CASING.registerVariant(FUSION_PARTS_CHANNEL, GADivertorCasing.CasingType.values(),
                type -> GAMetaBlocks.DIVERTOR_CASING.getItemVariant(type),
                type -> GAMetaBlocks.DIVERTOR_CASING.getState(type)
        );

        FUSION_COIL.registerVariant(FUSION_PARTS_CHANNEL,
                new GAFusionCasing.CasingType[]{
                        GAFusionCasing.CasingType.ADV_FUSION_COIL_1,
                        GAFusionCasing.CasingType.ADV_FUSION_COIL_2,
                        GAFusionCasing.CasingType.ADV_FUSION_COIL_3,
                        GAFusionCasing.CasingType.ADV_FUSION_COIL_4,
                        GAFusionCasing.CasingType.ADV_FUSION_COIL_5,
                },
                type -> GAMetaBlocks.FUSION_CASING.getItemVariant(type),
                type -> GAMetaBlocks.FUSION_CASING.getState(type)
        );


        int counter = Channel.COIL.getIndicatorMaxValue() + 1;
        for (GAHeatingCoil.CoilType type : GAHeatingCoil.CoilType.values()) {
            Channel.COIL.registerIndicator(GAMetaBlocks.HEATING_COIL.getItemVariant(type), counter++);
        }


        CELL.registerVariant(CELL_CHANNEL, CellCasing.CellType.values(),
                type -> GAMetaBlocks.CELL_CASING.getItemVariant(type),
                type -> GAMetaBlocks.CELL_CASING.getState(type));


        CELL.registerResolver(context -> new BlockInfo(GAMetaBlocks.CELL_CASING.getState(
                CellCasing.CellType.values()[PlaceholderType.clampIndex(
                        context.getTier(Channel.VOLTAGE), 3, CellCasing.CellType.values().length)])), Channel.VOLTAGE
        );

        NUCLEAR_CASING.registerResolver(context -> new BlockInfo(GAMetaBlocks.NUCLEAR_CASING.getState(
                NuclearCasing.CasingType.values()[PlaceholderType.clampIndex(
                        context.getTier(Channel.VOLTAGE), 0, NuclearCasing.CasingType.values().length)])), Channel.VOLTAGE
        );

        GLASS.registerResolver(context -> new BlockInfo(GAMetaBlocks.TRANSPARENT_CASING.getState(
                GATransparentCasing.CasingType.values()[PlaceholderType.clampIndex(
                        context.getTier(Channel.VOLTAGE), 0, GATransparentCasing.CasingType.values().length)])), Channel.VOLTAGE
        );

        MUFFLER.registerResolver(context -> PlaceholderType.mteHolder(
                GATileEntities.MUFFLER_HATCH[PlaceholderType.clampIndex(
                        context.getTier(Channel.VOLTAGE), 1, GATileEntities.MUFFLER_HATCH.length)], context.facing), Channel.VOLTAGE
        );

        FRAMEWORK.registerResolver(context -> new BlockInfo(GAMetaBlocks.getFramework(context.getTier(Channel.VOLTAGE))), Channel.VOLTAGE);


        // ---- Overrides of GT-core's own ChannelRegistry entries
        ChannelRegistry.COIL.registerResolver(context -> {
            List<BlockWireCoil.CoilType> gtCoils = ChannelRegistry.PREVIEW_COILS;
            GAHeatingCoil.CoilType[] gaCoils = GAHeatingCoil.CoilType.values();


            int index = PlaceholderType.clampIndex(context.getTier(Channel.COIL), 1, gtCoils.size() + gaCoils.length);

            if (index < gtCoils.size()) {
                return new BlockInfo(MetaBlocks.WIRE_COIL.getState(gtCoils.get(index)));
            }
            return new BlockInfo(GAMetaBlocks.HEATING_COIL.getState(gaCoils[index - gtCoils.size()]));
        }, Channel.COIL);

        ChannelRegistry.ENERGY_INPUT_HATCH.registerResolver(context -> PlaceholderType.mteHolder(
                GATileEntities.getEnergyHatch(context.getTier(Channel.VOLTAGE), false), context.facing), Channel.VOLTAGE
        );

        ChannelRegistry.ENERGY_OUTPUT_HATCH.registerResolver(context -> PlaceholderType.mteHolder(
                GATileEntities.getEnergyHatch(context.getTier(Channel.VOLTAGE), true), context.facing), Channel.VOLTAGE
        );
    }

}