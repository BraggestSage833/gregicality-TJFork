package gregicadditions;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GTSounds {

    public static SoundEvent BLACK_HOLE_AMBIENT;

    public static void registerSounds() {
        BLACK_HOLE_AMBIENT = register("black_hole_ambient");
    }

    private static SoundEvent register(String name) {
        ResourceLocation loc = new ResourceLocation(Gregicality.MODID,name);
        SoundEvent sound = new SoundEvent(loc).setRegistryName(loc);
        ForgeRegistries.SOUND_EVENTS.register(sound);
        return sound;
    }
}
