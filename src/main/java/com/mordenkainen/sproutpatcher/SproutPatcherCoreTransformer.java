package com.mordenkainen.sproutpatcher;

import com.mordenkainen.sproutpatcher.patches.BotaniaPatcher;
import com.mordenkainen.sproutpatcher.patches.CabinetPatcher;
import com.mordenkainen.sproutpatcher.patches.IPatch;
import com.mordenkainen.sproutpatcher.patches.ReComplexPatcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rustic.common.blocks.BlockCabinet;

public class SproutPatcherCoreTransformer implements IClassTransformer {
    private static IPatch[] patches= {new CabinetPatcher(), new ReComplexPatcher(), new BotaniaPatcher()};
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        byte[] workingClass = basicClass.clone();
        
        for (IPatch patch : patches) {
            if (patch.shouldLoad()) {
                workingClass = patch.transform(name, transformedName, workingClass);
            }
        }
        
        return workingClass;

        
        /*if ("rustic.common.tileentity.TileEntityCabinet".equals(name)) {
            final ClassReader cr = new ClassReader(basicClass);

            final ClassNode classNode = new ClassNode();
            cr.accept(classNode, 0);

            done:
            for (final MethodNode methodNode : classNode.methods) {
                if ("getRenderBoundingBox".equals(methodNode.name)) {
                    final Iterator<AbstractInsnNode> insnNodes = methodNode.instructions.iterator();
                    while (insnNodes.hasNext()) {
                        final AbstractInsnNode insn = insnNodes.next();
                        if (insn.getOpcode() == Opcodes.ALOAD) {
                            final InsnList endList = new InsnList();
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            endList.add(new FieldInsnNode(Opcodes.GETFIELD, "rustic/common/tileentity/TileEntityCabinet", SproutPatcherCoreLoader.DEOBF ? "worldObj" : "field_145850_b", "Lnet/minecraft/world/World;"));
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            endList.add(new FieldInsnNode(Opcodes.GETFIELD, "rustic/common/tileentity/TileEntityCabinet", SproutPatcherCoreLoader.DEOBF ? "pos" : "field_174879_c", "Lnet/minecraft/util/math/BlockPos;"));
                            endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mordenkainen/sproutpatcher/SproutPatcherCoreTransformer", "getRenderBoundingBox", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", false));
                            endList.add(new InsnNode(Opcodes.ARETURN));
                            methodNode.instructions.insertBefore(insn, endList);
                            break done;
                        }
                    }
                }
            }

            final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(cw);

            return cw.toByteArray();
        }
        
        if ("rustic.client.renderer.CabinetRenderer".equals(name)) {
            final ClassReader cr = new ClassReader(basicClass);

            final ClassNode classNode = new ClassNode();
            cr.accept(classNode, 0);

            done:
            for (final MethodNode methodNode : classNode.methods) {
                if ("renderTileEntityAt".equals(methodNode.name) && "(Lrustic/common/tileentity/TileEntityCabinet;DDDFI)V".equals(methodNode.desc)) {
                    LabelNode label1 = new LabelNode();
                    LabelNode label2 = new LabelNode();
                    final Iterator<AbstractInsnNode> insnNodes = methodNode.instructions.iterator();
                    
                    while (insnNodes.hasNext()) {
                        final AbstractInsnNode insn = insnNodes.next();
                        if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
                            final InsnList endList = new InsnList();
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", SproutPatcherCoreLoader.DEOBF ? "hasWorldObj" : "func_145830_o", "()Z", false));
                            endList.add(new JumpInsnNode(Opcodes.IFEQ, label1));
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", SproutPatcherCoreLoader.DEOBF ? "getWorld" : "func_145831_w", "()Lnet/minecraft/world/World;", false));
                            endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "rustic/common/tileentity/TileEntityCabinet", SproutPatcherCoreLoader.DEOBF ? "getPos" : "func_174877_v", "()Lnet/minecraft/util/math/BlockPos;", false));
                            endList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", SproutPatcherCoreLoader.DEOBF ? "getBlockState" : "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false));
                            endList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", SproutPatcherCoreLoader.DEOBF ? "getBlock" : "func_177230_c", "()Lnet/minecraft/block/Block;", true));
                            endList.add(new FieldInsnNode(Opcodes.GETSTATIC, "rustic/common/blocks/ModBlocks", "CABINET", "Lrustic/common/blocks/BlockCabinet;"));
                            endList.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, label2));
                            endList.add(label1);
                            endList.add(new InsnNode(Opcodes.RETURN));
                            endList.add(label2);
                            methodNode.instructions.insertBefore(insn, endList);
                            break done;
                        }
                    }
                }
            }
            final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            return cw.toByteArray();
        }

        return basicClass;*/
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
