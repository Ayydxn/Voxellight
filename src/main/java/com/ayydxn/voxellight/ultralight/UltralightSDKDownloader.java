package com.ayydxn.voxellight.ultralight;

import com.ayydxn.voxellight.VoxellightClientMod;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UltralightSDKDownloader
{
    public static void downloadAndExtract(Path targetDirectory) throws IOException
    {
        UltralightPlatform platform = UltralightPlatform.getCurrentPlatform();

        VoxellightClientMod.LOGGER.info("Downloading latest Ultralight SDK for {}...", platform.getName());

        // Create the target directory if it doesn't already exist.
        Files.createDirectories(targetDirectory);

        // Download and extract the Ultralight library files
        Path tempSDKFile = Path.of(System.getProperty("java.io.tmpdir") + "/ultralight-sdk-" + platform.getShortName() + "-" + UUID.randomUUID() + ".7z");

        UltralightSDKDownloader.downloadFile(platform.getSDKDownloadURL(), tempSDKFile);
        UltralightSDKDownloader.extractBinaries(tempSDKFile, targetDirectory, platform);

        // No longer need the temp SDK file so get rid of it
        FileUtils.forceDelete(tempSDKFile.toFile());
    }

    private static void downloadFile(String urlString, Path destination) throws IOException
    {
        URL url = URI.create(urlString).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Voxellight-UltralightDownloader");
        connection.setInstanceFollowRedirects(true);

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK)
            throw new IOException("Failed to download file: HTTP " + responseCode);

        long fileSize = connection.getContentLengthLong();

        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
             OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(destination)))
        {

            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int read;
            long lastProgressUpdateTime = System.nanoTime();

            while ((read = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, read);
                downloaded += read;

                long timeNow = System.nanoTime();

                if (fileSize > 0 && TimeUnit.NANOSECONDS.toMillis(timeNow - lastProgressUpdateTime) >= 225L)
                {
                    int progress = (int) ((downloaded * 100) / fileSize);
                    VoxellightClientMod.LOGGER.info("\r- Download Progress: {}%", progress);

                    lastProgressUpdateTime = timeNow;
                }
            }

            outputStream.flush();

            VoxellightClientMod.LOGGER.info("Download complete!");
        }
    }

    private static void extractBinaries(Path ultralightSDKPath, Path targetDirectory, UltralightPlatform platform) throws IOException
    {
        VoxellightClientMod.LOGGER.info("Extracting Ultralight binaries for {}...", platform.getName());

        boolean wasExtractionSuccessful = true;

        try (SevenZFile sdkFile = SevenZFile.builder()
                .setFile(ultralightSDKPath.toFile())
                .get())
        {
            SevenZArchiveEntry entry;
            while ((entry = sdkFile.getNextEntry()) != null)
            {
                if (entry.isDirectory())
                    continue;

                if (entry.getName().endsWith(platform.getLibraryFileExtension()))
                {
                    String libraryFilename = StringUtils.substringAfterLast(entry.getName(), "/");
                    Path libraryOutputPath = targetDirectory.resolve(libraryFilename);

                    try (OutputStream outputStream = Files.newOutputStream(libraryOutputPath))
                    {
                        byte[] buffer = new byte[8192];
                        int len;

                        while ((len = sdkFile.read(buffer)) != -1)
                            outputStream.write(buffer, 0, len);
                    }
                }
            }
        }
        catch (Exception exception)
        {
            wasExtractionSuccessful = false;

            VoxellightClientMod.LOGGER.error("Failed to extract Ultralight binaries!", exception);
        }

        if (wasExtractionSuccessful)
            VoxellightClientMod.LOGGER.info("Successfully extracted Ultralight binaries to the game directory!");
    }
}
