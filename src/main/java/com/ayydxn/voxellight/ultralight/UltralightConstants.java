package com.ayydxn.voxellight.ultralight;

import com.google.common.collect.ImmutableMap;

import java.util.List;

public class UltralightConstants
{
    public static final ImmutableMap<UltralightPlatform, List<String>> PLATFORM_TO_LIBRARY_FILES = new ImmutableMap.Builder<UltralightPlatform, List<String>>()
            .put(UltralightPlatform.WINDOWS, List.of("Ultralight.dll", "UltralightCore.dll", "AppCore.dll", "WebCore.dll"))
            .put(UltralightPlatform.MAC_OS, List.of("libUltralight.dylib", "libUltralightCore.dylib", "libAppCore.dylib", "libWebCore.dylib"))
            .put(UltralightPlatform.LINUX, List.of("libUltralight.so", "libUltralightCore.so", "libAppCore.so", "libWebCore.so"))
            .build();
}
