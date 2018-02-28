package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

public class BotaniaPatcher implements IPatch {

    @Override
    public boolean shouldLoad() {
        return SproutConfig.BotaniaPatch;
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if ("vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching SubTileMarimorphosis");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "getStoneToPut", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;");
            if (method == null) {
                return basicClass;
            }
            
            AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(method, Opcodes.GOTO);
            targetNode = ASMHelper.findNextInstructionWithOpcode(targetNode, Opcodes.ALOAD);
            if (targetNode == null) {
                return basicClass;
            }
            
            method.instructions.insertBefore(targetNode, new VarInsnNode(Opcodes.ALOAD, 3));
            
            targetNode = ASMHelper.findNextInstructionWithOpcode(targetNode, Opcodes.AALOAD);
            if (targetNode == null) {
                return basicClass;
            }
            
            method.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "gnu/trove/list/array/TIntArrayList", "get", "(I)I", false));
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }
    
}
