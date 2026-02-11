package com.ayydxn.voxellight;

import com.ayydxn.voxellight.utils.VoxellightConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.ultralight.AppCore.ulCreateApp;
import static org.lwjgl.ultralight.AppCore.ulDestroyApp;
import static org.lwjgl.ultralight.Ultralight.*;

@Environment(EnvType.CLIENT)
public class VoxellightClientMod implements ClientModInitializer
{
    private static VoxellightClientMod INSTANCE;

    public static final Logger LOGGER = (Logger) LogManager.getLogger("Voxellight");
    public static final String MOD_ID = "voxellight";

    private long ulConfig;
    private long ulApp;
    private long ulRenderer;

    @Override
    public void onInitializeClient()
    {
        INSTANCE = this;

        LOGGER.info("Initializing Voxellight... (Version: {})", FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString());

        long resourcePathPrefix = ulCreateString(VoxellightConstants.ULTRALIGHT_RESOURCES_DIRECTORY.toFile() + "/");

        this.ulConfig = ulCreateConfig();
        ulConfigSetResourcePathPrefix(this.ulConfig, resourcePathPrefix);

        this.ulApp = ulCreateApp(MemoryUtil.NULL, this.ulConfig);
        this.ulRenderer = ulCreateRenderer(this.ulConfig);

        ulDestroyString(resourcePathPrefix);

        ClientLifecycleEvents.CLIENT_STOPPING.register(this::onClientStopping);
    }

    public static VoxellightClientMod getInstance()
    {
        if (INSTANCE == null)
            throw new IllegalStateException("Tried to access an instance of Voxellight when one wasn't available!");

        return INSTANCE;
    }

    private void onClientStopping(MinecraftClient client)
    {
        VoxellightClientMod.LOGGER.info("Destroying Ultralight resources...");

        if (this.ulRenderer != MemoryUtil.NULL)
            ulDestroyRenderer(this.ulRenderer);

        if (this.ulApp != MemoryUtil.NULL)
            ulDestroyApp(this.ulApp);

        if (this.ulConfig != MemoryUtil.NULL)
            ulDestroyConfig(this.ulConfig);
    }
}
