package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

public class BotaniaPatcher implements IPatch {

    @Override
    public boolean shouldLoad() {
        return SproutConfig.BotaniaPatch;
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis".equals(name)) {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "getStoneToPut", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;");
            if (method != null) {
                AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(method, Opcodes.GOTO);
                targetNode = ASMHelper.findNextInstructionWithOpcode(targetNode, Opcodes.ALOAD);
                if (targetNode != null) {
                    method.instructions.insertBefore(targetNode, new VarInsnNode(Opcodes.ALOAD, 3));
                }
                targetNode = ASMHelper.findNextInstructionWithOpcode(targetNode, Opcodes.AALOAD);
                if (targetNode != null) {
                    method.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "gnu/trove/list/array/TIntArrayList", "get", "(I)I", false));
                }
            }
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }    
}
