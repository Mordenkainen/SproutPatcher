package com.mordenkainen.sproutpatcher.patches;

public interface IPatch {
    
    default boolean shouldLoad() {
        return true;
    }
    
    byte[] transform(final String name, final String transformedName, final byte[] basicClass);
}
