package com.ayydxn.voxellight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@Environment(EnvType.CLIENT)
public class VoxellightClientMod implements ClientModInitializer
{
    private static VoxellightClientMod INSTANCE;

    public static final Logger LOGGER = (Logger) LogManager.getLogger("Voxellight");
    public static final String MOD_ID = "voxellight";

    @Override
    public void onInitializeClient()
    {
        INSTANCE = this;

        LOGGER.info("Initializing Voxellight... (Version: {})", FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString());
    }

    public static VoxellightClientMod getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of Voxellight when one wasn't available!");

        return INSTANCE;
    }
}
