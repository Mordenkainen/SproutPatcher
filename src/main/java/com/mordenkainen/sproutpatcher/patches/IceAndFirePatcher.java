package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

public class IceAndFirePatcher implements IPatch {
    
    @Override
    public boolean shouldLoad() {
        return SproutConfig.IceAndFirePatch;
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if ("com.github.alexthe666.iceandfire.core.ModItems".equals(transformedName)) {
            SproutPatcherCoreLoader.logger.info("Patching Ice and Fire ModItems");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "init", "()V");
            if (method == null) {
                return basicClass;
            }
            
            final AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);
            if (targetNode == null) {
                return basicClass;
            }
            
            method.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/handlers/IceAndFireHandler", "setRepairMaterials", "()V", false)); 
            
            return ASMHelper.writeClassToBytes(classNode);
        }
        
        return basicClass;
    }

}
