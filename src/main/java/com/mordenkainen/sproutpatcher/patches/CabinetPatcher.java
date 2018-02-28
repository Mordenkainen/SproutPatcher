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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.mordenkainen.sproutpatcher.SproutConfig;
import com.mordenkainen.sproutpatcher.SproutPatcherCoreLoader;
import com.mordenkainen.sproutpatcher.asmhelper.ASMHelper;
import com.mordenkainen.sproutpatcher.asmhelper.ObfHelper;

public class CabinetPatcher implements IPatch {

    private String cabinet = "rustic/common/tileentity/TileEntityCabinet";
    
    @Override
    public boolean shouldLoad() {
        return SproutConfig.CabinetPatch;
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if ("rustic.common.tileentity.TileEntityCabinet".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching TileEntityCabinet");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "getRenderBoundingBox", "()Lnet/minecraft/util/math/AxisAlignedBB;");
            if (method == null) {
                return basicClass;
            }
            
            final InsnList endList = new InsnList();
            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            endList.add(new FieldInsnNode(Opcodes.GETFIELD, cabinet, ObfHelper.isObfuscated() ? "field_145850_b" : "worldObj", "Lnet/minecraft/world/World;"));
            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            endList.add(new FieldInsnNode(Opcodes.GETFIELD, cabinet, ObfHelper.isObfuscated() ? "field_174879_c" : "pos", "Lnet/minecraft/util/math/BlockPos;"));
            endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/handlers/CabinetHandler", "getRenderBoundingBox", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", false));
            endList.add(new InsnNode(Opcodes.ARETURN));
            
            final AbstractInsnNode targetNode = ASMHelper.findFirstInstruction(method);
            if (targetNode == null) {
                return basicClass;
            }
            
            method.instructions.insertBefore(targetNode, endList);

            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        if ("rustic.client.renderer.CabinetRenderer".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching CabinetRenderer");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "renderTileEntityAt", "(Lrustic/common/tileentity/TileEntityCabinet;DDDFI)V");
            if (method == null) {
                return basicClass;
            }
            
            final AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(method, Opcodes.INVOKESTATIC);
            if (targetNode == null) {
                return basicClass;
            }
            
            final LabelNode label1 = new LabelNode();
            final LabelNode label2 = new LabelNode();
            final InsnList endList = new InsnList();
            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, cabinet, ObfHelper.isObfuscated() ? "func_145830_o" : "hasWorldObj", "()Z", false));
            endList.add(new JumpInsnNode(Opcodes.IFEQ, label1));
            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, cabinet, ObfHelper.isObfuscated() ? "func_145831_w" : "getWorld", "()Lnet/minecraft/world/World;", false));
            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, cabinet, ObfHelper.isObfuscated() ? "func_174877_v" : "getPos", "()Lnet/minecraft/util/math/BlockPos;", false));
            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfHelper.isObfuscated() ?"func_180495_p" :  "getBlockState", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
            endList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", ObfHelper.isObfuscated() ? "func_177230_c" :  "getBlock", "()Lnet/minecraft/block/Block;", true));
            endList.add(new FieldInsnNode(Opcodes.GETSTATIC, "rustic/common/blocks/ModBlocks", "CABINET", "Lrustic/common/blocks/BlockCabinet;"));
            endList.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, label2));
            endList.add(label1);
            endList.add(new InsnNode(Opcodes.RETURN));
            endList.add(label2);
            method.instructions.insertBefore(targetNode, endList);
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_MAXS);
        }

        return basicClass;
    }
    
}
