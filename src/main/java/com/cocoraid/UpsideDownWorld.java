package com.cocoraid;

import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class UpsideDownWorld extends JavaPlugin {

    @Override
    public void onEnable() {

        ResourceKey.create(Registries.BIOME, new ResourceLocation("test"));
    }

    private void replaceAllBiomesAsync(World world, Player player) {
        int amount = world.getLoadedChunks().length;
        player.sendMessage("Replacing " + amount + " chunks");
        Holder<Biome> upsideDownBiome = Holder.direct(new UpsideDownBiome().getBiome());

        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        nmsWorld.getChunkSource().getGenerator().getBiomeSource().
        new BukkitRunnable() {
            int currentChunk = 0;

            @Override
            public void run() {
                List<ChunkAccess> chunks = new ArrayList<>();

                if (currentChunk >= amount) {
                    player.sendMessage("Done replacing all chunks");
                    return;
                }
                LevelChunk nmsChunk = ((CraftChunk) world.getLoadedChunks()[currentChunk]).getHandle();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            nmsChunk.setBiome(x, y, z, upsideDownBiome);
                        }
                    }
                }
                chunks.add(nmsChunk);
                currentChunk++;

                nmsWorld.getChunkSource().chunkMap.resendBiomesForChunks(chunks);
                player.sendMessage("Replaced " + currentChunk + " chunks");
                //display percentage
                player.sendMessage("Replaced " + (currentChunk * 100 / amount) + "% of chunks");
            }
        }.runTaskTimer(this, 0, 10);
    }


    public void replaceBiome(Player player, int radius) {
        Holder<Biome> upsideDownBiome = Holder.direct(new UpsideDownBiome().getBiome());
        World world = player.getWorld();
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        int x = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();
        int amount = 0;
        for (int i = x - radius; i < x + radius; i++) {
            for (int j = z - radius; j < z + radius; j++) {
                LevelChunk nmsChunk = nmsWorld.getChunk(i, j);
                for (int k = 0; k < 16; k++) {
                    for (int l = 0; l < 16; l++) {
                        for (int m = 0; m < 256; m++) {
                            nmsChunk.setBiome(k, m, l, upsideDownBiome);
                        }
                    }
                }
                amount++;
            }
        }
        player.sendMessage("Replaced " + amount + " chunks");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("replacebiome")) {
                replaceBiome(player, 1);
            }
        }
        return true;
    }


}