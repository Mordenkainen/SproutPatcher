package com.mordenkainen.sproutpatcher.patches;

import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.ServerWorldEventHandler;

public class BedPatcher implements IPatch {

    static Method method;
    
    @Override
    public boolean shouldLoad() {
        return SproutConfig.BedPatch;
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.world.ServerWorldEventHandler".equals(transformedName)) {
            SproutPatcherCoreLoader.logger.info("Patching ServerWorldEventHandler");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass, ClassReader.EXPAND_FRAMES);
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "func_72709_b", "onEntityRemoved", "(Lnet/minecraft/entity/Entity;)V");
            ASMHelper.copyAndRenameMethod(classNode, method, "onEntityRemove_org");
            method.instructions.clear();
            final InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/patches/BedPatcher", "onEntityRemoved", "(Lnet/minecraft/world/ServerWorldEventHandler;Lnet/minecraft/entity/Entity;)V", false));
            insnList.add(new InsnNode(Opcodes.RETURN));
            method.instructions.insert(insnList);
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }
    
    public static void onEntityRemoved(ServerWorldEventHandler server, Entity entityIn) {
        if (!(entityIn instanceof EntityPlayerMP) || ((EntityPlayerMP) entityIn).isDead) {
            try {
                if (method == null) {
                    SproutPatcherCoreLoader.logger.info("onEntityRemove_org method null. Loading...");
                    method = server.getClass().getMethod("onEntityRemove_org", new Class[] {Entity.class});
                }
                method.invoke(server, entityIn);
            } catch (Exception e) {}
            return;
        }
    }
    
}
