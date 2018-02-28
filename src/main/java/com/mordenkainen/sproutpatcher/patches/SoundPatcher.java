package com.mordenkainen.sproutpatcher.patches;

import java.util.ListIterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;

public class SoundPatcher implements IPatch {

    @Override
    public boolean shouldLoad() {
        return SproutConfig.SoundPatch;
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if ("paulscode.sound.Source".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching paulscode Source");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "removed", "Z", null, null));
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
            
        if ("paulscode.sound.Library".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching paulscode Library");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "removeSource", "(Ljava/lang/String;)V");
            for (final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator(); iterator.hasNext();) {
                final AbstractInsnNode insn = (AbstractInsnNode) iterator.next(); 
                if(insn instanceof MethodInsnNode && ((MethodInsnNode) insn).owner.equals("paulscode/sound/Source") && ((MethodInsnNode) insn).name.equals("cleanup")) {
                    method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/handlers/SoundHandler", "cleanupSource", "(Lpaulscode/sound/Source;)V", false));
                    method.instructions.remove(insn);
                    break;
                }
            }
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        if ("paulscode.sound.StreamThread".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching paulscode StreamThread");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);
            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "run", "()V");
            for (final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator(); iterator.hasNext();) {
                AbstractInsnNode insn = (AbstractInsnNode) iterator.next();
                if(insn instanceof MethodInsnNode && ((MethodInsnNode) insn).owner.equals("java/util/ListIterator") && ((MethodInsnNode) insn).name.equals("next")) {
                    insn = insn.getNext().getNext();
                    
                    final int varIndex = ((VarInsnNode) insn).var;
                    
                    method.instructions.insert(insn, new VarInsnNode(Opcodes.ASTORE, varIndex));
                    method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/hand;ers/SoundHandler", "removeSource", "(Lpaulscode/sound/Source;)Lpaulscode/sound/Source;", false));
                    method.instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, varIndex));
                    break;
                }
            }
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        return basicClass;
    }
    
}
