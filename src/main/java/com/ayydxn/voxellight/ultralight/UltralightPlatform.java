package com.ayydxn.voxellight.ultralight;

import net.minecraft.util.Util;

public enum UltralightPlatform
{
    WINDOWS("Windows", "win-x64", "https://github.com/Ayydxn/Voxellight-Ultralight-SDKs/releases/download/1.4.0/ultralight-free-sdk-1.4.0-win-x64.7z", ".dll"),
    MAC_OS("MacOS", "mac-x64", "https://github.com/Ayydxn/Voxellight-Ultralight-SDKs/releases/download/1.4.0/ultralight-free-sdk-1.4.0-mac-x64.7z", ".dylib"),
    LINUX("Linux", "linux-x64", "https://github.com/Ayydxn/Voxellight-Ultralight-SDKs/releases/download/1.4.0/ultralight-free-sdk-1.4.0-linux-x64.7z", ".so");

    private final String name;
    private final String shortName;
    private final String sdkDownloadURL;
    private final String libraryFileExtension;

    UltralightPlatform(String name, String shortName, String sdkDownloadURL, String libraryFileExtension)
    {
        this.name = name;
        this.shortName = shortName;
        this.sdkDownloadURL = sdkDownloadURL;
        this.libraryFileExtension = libraryFileExtension;
    }

    public static UltralightPlatform getCurrentPlatform()
    {
        return switch (Util.getOperatingSystem())
        {
            case WINDOWS -> UltralightPlatform.WINDOWS;
            case OSX -> UltralightPlatform.MAC_OS;
            case LINUX -> UltralightPlatform.LINUX;
            default -> throw new IllegalStateException("Current platform is unsupported by Ultralight!");
        };
    }

    public String getName()
    {
        return this.name;
    }

    public String getShortName()
    {
        return this.shortName;
    }

    public String getSDKDownloadURL()
    {
        return this.sdkDownloadURL;
    }

    public String getLibraryFileExtension()
    {
        return this.libraryFileExtension;
    }
}
