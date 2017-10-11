package com.mordenkainen.sproutpatcher.patches;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

public class ReComplexPatcher implements IPatch {

    @Override
    public boolean shouldLoad() {
        return SproutPatcherCoreLoader.config.getTag("RecurrentComplex").setComment("Patch Recurrent Complex Script block Redstone Handling.").getBooleanValue(true);
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("ivorius.reccomplex.block.TileEntityBlockScript".equals(name)) {
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            
            MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "()V");
            if (method != null) {
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.ICONST_0);
                if (targetNode != null) {
                    method.instructions.set(targetNode, new InsnNode(Opcodes.ICONST_1));
                }
            }
            
            method = ASMHelper.findMethodNodeOfClass(classNode, "shouldPlaceInWorld", "(Livorius/reccomplex/world/gen/feature/structure/context/StructureSpawnContext;Livorius/reccomplex/world/gen/script/WorldScriptMulti$InstanceData;)Z");
            if (method != null) {
                LabelNode label1 = new LabelNode();
                LabelNode label2 = new LabelNode();
                final InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "ivorius/reccomplex/block/TileEntityBlockScript", "spawnTriggerable", "Z"));
                insnList.add(new JumpInsnNode(Opcodes.IFNE, label1));
                insnList.add(new InsnNode(Opcodes.ICONST_1));
                insnList.add(new JumpInsnNode(Opcodes.GOTO, label2));
                insnList.add(label1);
                AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(method, Opcodes.ICONST_0);
                method.instructions.insertBefore(targetNode, insnList);
                targetNode = targetNode.getNext();
                method.instructions.insertBefore(targetNode, label2);
            }
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }    
}
