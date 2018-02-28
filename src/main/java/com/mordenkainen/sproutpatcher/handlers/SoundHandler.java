package com.mordenkainen.sproutpatcher.handlers;

import java.lang.reflect.Field;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import paulscode.sound.Source;

public final class SoundHandler {
    
    private SoundHandler() {}
    
    public static Field removed = ReflectionHelper.findField(Source.class, "removed");
    
    public static Source removeSource(final Source source) {
        try {
            if(removed.getBoolean(source)) {
                source.cleanup();
                return null;
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return source;
    }
    
    public static void cleanupSource(final Source source) {
        if(source.toStream) {
            try {
                removed.setBoolean(source, true);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            source.cleanup();
        }
    }
    
}
