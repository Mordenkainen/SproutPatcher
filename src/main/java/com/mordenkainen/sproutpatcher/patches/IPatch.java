package com.mordenkainen.sproutpatcher.patches;

public interface IPatch {
    
    default boolean shouldLoad() {
        return true;
    }
    
    byte[] transform(String name, String transformedName, byte[] basicClass);
}
