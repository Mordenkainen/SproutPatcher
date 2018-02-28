package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;
import com.mordenkainen.sproutpatcher.asmhelper.ObfHelper;

public class BedPatcher implements IPatch {
    
    @Override
    public boolean shouldLoad() {
        return SproutConfig.BedPatch;
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if ("net.minecraft.world.chunk.Chunk".equals(transformedName)) {
            SproutPatcherCoreLoader.logger.info("Patching Chunk");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "func_76623_d", "onChunkUnload", "()V");
            final InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", ObfHelper.isObfuscated() ? "field_76637_e" : "worldObj", "Lnet/minecraft/world/World;"));
            insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/chunk/Chunk", ObfHelper.isObfuscated() ? "field_76645_j" : "entityLists", "[Lnet/minecraft/util/ClassInheritanceMultiMap;"));
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/handlers/BedHandler", "onChunkUnload", "(Lnet/minecraft/world/World;[Lnet/minecraft/util/ClassInheritanceMultiMap;)V", false));
            final AbstractInsnNode insert = ASMHelper.findFirstInstruction(method);
            method.instructions.insertBefore(insert, insnList);
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }
    
}
