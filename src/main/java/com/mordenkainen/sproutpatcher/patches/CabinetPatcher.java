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

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rustic.common.blocks.BlockCabinet;

public class CabinetPatcher implements IPatch {

    @Override
    public boolean shouldLoad() {
        return SproutConfig.CabinetPatch;
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("rustic.common.tileentity.TileEntityCabinet".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching TileEntityCabinet");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "getRenderBoundingBox", "()Lnet/minecraft/util/math/AxisAlignedBB;");
            if (method != null) {
                final InsnList endList = new InsnList();
                endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                endList.add(new FieldInsnNode(Opcodes.GETFIELD, "rustic/common/tileentity/TileEntityCabinet", ObfHelper.isObfuscated() ? "field_145850_b" : "worldObj", "Lnet/minecraft/world/World;"));
                endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                endList.add(new FieldInsnNode(Opcodes.GETFIELD, "rustic/common/tileentity/TileEntityCabinet", ObfHelper.isObfuscated() ? "field_174879_c" : "pos", "Lnet/minecraft/util/math/BlockPos;"));
                endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/patches/CabinetPatcher", "getRenderBoundingBox", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", false));
                endList.add(new InsnNode(Opcodes.ARETURN));
                
                AbstractInsnNode targetNode = ASMHelper.findFirstInstruction(method);
                if (targetNode != null) {
                    method.instructions.insertBefore(targetNode, endList);
                }
            }

            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_FRAMES);
        }
        
        if ("rustic.client.renderer.CabinetRenderer".equals(name)) {
            SproutPatcherCoreLoader.logger.info("Patching CabinetRenderer");
            final ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

            final MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "renderTileEntityAt", "(Lrustic/common/tileentity/TileEntityCabinet;DDDFI)V");
            if (method != null) {
                AbstractInsnNode targetNode = ASMHelper.findFirstInstructionWithOpcode(method, Opcodes.INVOKESTATIC);
                if (targetNode != null) {
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    final InsnList endList = new InsnList();
                    endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", ObfHelper.isObfuscated() ? "func_145830_o" : "hasWorldObj", "()Z", false));
                    endList.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                    endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", ObfHelper.isObfuscated() ? "func_145831_w" : "getWorld", "()Lnet/minecraft/world/World;", false));
                    endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", ObfHelper.isObfuscated() ? "func_174877_v" : "getPos", "()Lnet/minecraft/util/math/BlockPos;", false));
                    endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfHelper.isObfuscated() ?"func_180495_p" :  "getBlockState", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
                    endList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", ObfHelper.isObfuscated() ? "func_177230_c" :  "getBlock", "()Lnet/minecraft/block/Block;", true));
                    endList.add(new FieldInsnNode(Opcodes.GETSTATIC, "rustic/common/blocks/ModBlocks", "CABINET", "Lrustic/common/blocks/BlockCabinet;"));
                    endList.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, label2));
                    endList.add(label1);
                    endList.add(new InsnNode(Opcodes.RETURN));
                    endList.add(label2);
                    method.instructions.insertBefore(targetNode, endList);
                }
            }
            
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_MAXS);
        }

        return basicClass;
    }
    
    @SuppressWarnings("deprecation")
    public static AxisAlignedBB getRenderBoundingBox(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        try {
            if (((Boolean)state.getBlock().getActualState(state, world, pos).getValue(BlockCabinet.BOTTOM)).booleanValue()) {
                return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D);
            }
        } catch (IllegalArgumentException e) {}
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D);
    }
    
}
