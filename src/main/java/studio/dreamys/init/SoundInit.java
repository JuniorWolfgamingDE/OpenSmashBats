package studio.dreamys.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundInit {

    public static SoundEvent WOODEN_SMASH_BAT;
    public static SoundEvent METAL_SMASH_BAT;
    public static SoundEvent DIAMOND_SMASH_BAT;

    public static void registerSounds() {
        WOODEN_SMASH_BAT = registerSound("wooden_smash_bat");
        METAL_SMASH_BAT = registerSound("metal_smash_bat");
        DIAMOND_SMASH_BAT = registerSound("diamond_smash_bat");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation("opensmashbats", name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}