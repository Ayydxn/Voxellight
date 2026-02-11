package com.ayydxn.voxellight;

import com.ayydxn.voxellight.ultralight.UltralightConstants;
import com.ayydxn.voxellight.ultralight.UltralightPlatform;
import com.ayydxn.voxellight.ultralight.UltralightSDKDownloader;
import com.ayydxn.voxellight.utils.VoxellightConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.lwjgl.ultralight.Ultralight.ulVersionString;

public class VoxelleightPreLaunchEntrypoint implements PreLaunchEntrypoint
{
    @Override
    public void onPreLaunch()
    {
        VoxellightClientMod.LOGGER.info("Bootstrapping Voxellight...");

        // Putting them in here is easier as we then don't have to deal with setting library paths and by extension avoid errors when the libraries get loaded.
        Path ultralightLibrariesDirectory = FabricLoader.getInstance().getGameDir();

        try
        {
            if (!this.areUltralightLibrariesPresent(ultralightLibrariesDirectory))
                UltralightSDKDownloader.downloadAndExtract(ultralightLibrariesDirectory);
        }
        catch (IOException exception)
        {
            VoxellightClientMod.LOGGER.error("Failed to download the Ultralight libraries", exception);
        }

        VoxellightClientMod.LOGGER.info("Ultralight Version: {}", ulVersionString());

        // Ultralight can't load cacert.pem and icudt67l.dat from the mod's resources folder.
        // So, we have to copy them to somewhere on disk and then point Ultralight to that.
        try
        {
            Files.createDirectories(VoxellightConstants.ULTRALIGHT_RESOURCES_DIRECTORY);
        }
        catch (IOException exception)
        {
            VoxellightClientMod.LOGGER.error("Failed to create Ultralight resources folder!", exception);
        }

        String[] targetResourceFiles = {
                "cacert.pem",
                "icudt67l.dat"
        };

        for (String targetResourceFile : targetResourceFiles)
        {
            String resourceFilePath = String.format("/assets/%s/ultralight/%s", VoxellightClientMod.MOD_ID, targetResourceFile);
            Path destinationFile = VoxellightConstants.ULTRALIGHT_RESOURCES_DIRECTORY.resolve(targetResourceFile);

            try (InputStream inputStream = VoxelleightPreLaunchEntrypoint.class.getResourceAsStream(resourceFilePath))
            {
                if (inputStream == null)
                    throw new FileNotFoundException(String.format("Ultralight resource file '%s' was not found!",  resourceFilePath));

                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException exception)
            {
                VoxellightClientMod.LOGGER.error("Failed to copy Ultralight resource file!", exception);
            }
        }
    }

    private boolean areUltralightLibrariesPresent(Path librariesDirectory)
    {
        if (!Files.exists(librariesDirectory) || !Files.isDirectory(librariesDirectory))
            return false;

        UltralightPlatform platform = UltralightPlatform.getCurrentPlatform();
        List<String> ultralightLibraryFiles = UltralightConstants.PLATFORM_TO_LIBRARY_FILES.get(platform);

        if (ultralightLibraryFiles == null)
        {
            throw new IllegalStateException(String.format("Ultralight libraries for the current platform (%s) are not available! It probably isn't supported!",
                    Util.getOperatingSystem().getName()));
        }

        return ultralightLibraryFiles.stream()
                .map(librariesDirectory::resolve)
                .allMatch(path -> Files.exists(path) && Files.isRegularFile(path));
    }
}
