package com.cocoraid;


import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;

public class UpsideDownBiome {


    public UpsideDownBiome() {

    }



    public Biome getBiome() {
        Biome.BiomeBuilder biomeBuilder = new Biome.BiomeBuilder();

        BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder();
        builder.ambientAdditionsSound(
                new AmbientAdditionsSettings(Holder.direct(SoundEvents.ALLAY_AMBIENT_WITH_ITEM), 0)
        );
        builder.ambientParticle(new AmbientParticleSettings(ParticleTypes.FALLING_SPORE_BLOSSOM, 0.01F));
        builder.fogColor(0);
        builder.waterColor(0);
        builder.foliageColorOverride(0);
        builder.grassColorOverride(0);
        builder.skyColor(0);
        builder.waterFogColor(0);

        biomeBuilder.specialEffects(builder.build());
        biomeBuilder.downfall(0F);
        biomeBuilder.temperature(0F);
        biomeBuilder.mobSpawnSettings(MobSpawnSettings.EMPTY);
        biomeBuilder.generationSettings(BiomeGenerationSettings.EMPTY);
        biomeBuilder.temperatureAdjustment(Biome.TemperatureModifier.NONE);

        Biome nmsBiome = biomeBuilder.build();
        return nmsBiome;

    }


}