package williewillus.BugfixMod.patchers.legacy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 3/11/14.
 */
@Deprecated
public class ArrowFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = MappingRegistry.getMethodNameFor("EntityArrow.onUpdate");
        String targetMethodDesc = "()V";
        String targetFieldName = MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        System.out.println("CLASSNODE NAME: " + classNode.name);
        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ArrowFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction instanceof FieldInsnNode) {
                        if (((FieldInsnNode) currentInstruction).name.equals(targetFieldName) && currentInstruction.getOpcode() == Opcodes.PUTFIELD) {
                            System.out.println("[ArrowFix] Found entry point!");
                            InsnList toInject = new InsnList();

                            String entityArrowName = MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow");
                            String worldName = MappingRegistry.getClassNameFor("net/minecraft/world/World");
                            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.worldObj"), "L" + worldName + ";"));
                            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145791_d"), "I"));
                            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145792_e"), "I"));
                            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145789_f"), "I"));
                            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, worldName, MappingRegistry.getMethodNameFor("World.getBlock"), "(III)L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"));
                            toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g"), "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"));
                            //Get block at the arrow's coordinates and assign it to field_145790_g, overriding the incorrect assignment just before insertion.
                            m.instructions.insert(currentInstruction, toInject);
                            System.out.println("[ArrowFix] Injected new field assignment!");
                        }
                    }
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ArrowFix] Applied Transform!");
        return writer.toByteArray();
    }
}
