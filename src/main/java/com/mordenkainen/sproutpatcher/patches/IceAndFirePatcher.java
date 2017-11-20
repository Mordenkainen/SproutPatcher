package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

import net.minecraft.item.ItemStack;

public class IceAndFirePatcher implements IPatch {
    
    @Override
    public boolean shouldLoad() {
        return SproutConfig.IceAndFirePatch;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("com.github.alexthe666.iceandfire.core.ModItems".equals(transformedName)) {
            SproutPatcherCoreLoader.logger.info("Patching Ice and Fire ModItems");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "init", "()V");

            if (method != null) {
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.RETURN);
                if (targetNode != null) {
                    method.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/patches/IceAndFirePatcher", "setRepairMaterials", "()V", false));
                    
                }
            }
            
            return ASMHelper.writeClassToBytes(classNode);
        }
        return basicClass;
    }

    public static void setRepairMaterials() {
        ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.boneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
    }

}
