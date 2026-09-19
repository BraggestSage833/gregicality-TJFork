package gregicadditions.channels;


import gregicadditions.item.CellCasing;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.*;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import gregtech.integration.jei.multiblock.channel.ChannelDescription;
import gregtech.integration.jei.multiblock.channel.StructureChannels;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public final class GAChannelRegistry {
    public static void init() {
        registerIndicators(
                StructureChannels.MOTOR,
                MotorCasing.CasingType.values(),
                type -> GAMetaBlocks.MOTOR_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.CONVEYOR,
                ConveyorCasing.CasingType.values(),
                type -> GAMetaBlocks.CONVEYOR_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.EMITTER,
                EmitterCasing.CasingType.values(),
                type -> GAMetaBlocks.EMITTER_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.FIELD_GEN,
                FieldGenCasing.CasingType.values(),
                type -> GAMetaBlocks.FIELD_GEN_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.PISTON,
                PistonCasing.CasingType.values(),
                type -> GAMetaBlocks.PISTON_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.PUMP,
                PumpCasing.CasingType.values(),
                type -> GAMetaBlocks.PUMP_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.ROBOT_ARM,
                RobotArmCasing.CasingType.values(),
                type -> GAMetaBlocks.ROBOT_ARM_CASING.getItemVariant(type)
        );
        registerIndicators(
                StructureChannels.SENSOR,
                SensorCasing.CasingType.values(),
                type -> GAMetaBlocks.SENSOR_CASING.getItemVariant(type)
        );

        registerIndicators(
                StructureChannels.CELL,
                CellCasing.CellType.values(),
                type -> GAMetaBlocks.CELL_CASING.getItemVariant(type)
        );
        
    }

    public static void addToChannels() {
        int counter = 10;
        for (GAHeatingCoil.CoilType type : GAHeatingCoil.CoilType.values()) {
            StructureChannels.COIL.registerIndicator(GAMetaBlocks.HEATING_COIL.getItemVariant(type), counter++);
        }

        counter = 0;
        var a = ChannelDescription.get("coil").getItems();
        for (var te : a.entrySet()) {
            System.out.println(te.getKey().toString() + counter);
            counter++;
        }
    }

    private static <E extends Enum<E>> void registerIndicators(
            StructureChannels channel,
            E[] values,
            Function<E, ItemStack> itemProvider) {

        int counter = 1;

        for (E type : values) {
            channel.registerIndicator(itemProvider.apply(type), counter++);
        }

    }
}