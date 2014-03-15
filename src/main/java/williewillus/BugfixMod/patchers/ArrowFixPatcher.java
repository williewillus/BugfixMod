package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 3/11/14.
 */
public class ArrowFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName, targetMethodDesc;
        if (isObf) {
            targetMethodName = "h";
            targetMethodDesc = "()V";
        } else {
            targetMethodName = "onUpdate";
            targetMethodDesc = "()V";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        Iterator<MethodNode> methods = classNode.methods.iterator();


        while (methods.hasNext()) {
            MethodNode m = methods.next();

            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ArrowFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                int index = 0;
                String targetFieldName = isObf ? "g" : "field_145790_g";


                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction instanceof FieldInsnNode) {
                        if (((FieldInsnNode) currentInstruction).name.equals(targetFieldName) && currentInstruction.getOpcode() == Opcodes.PUTFIELD) {
                            System.out.println("[ArrowFix] Found entry point!");
                            InsnList toInject = new InsnList();

                            if (isObf) {
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "xo", "p", "Lafn;"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "xo", "d", "I"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "xo", "e", "I"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "xo", "f", "I"));
                                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "afn", "a", "(III)Lahu;"));
                                toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, "xo", "g", "Lahu;"));
                            } else {
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/projectile/EntityArrow", "worldObj", "Lnet/minecraft/world/World;"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/projectile/EntityArrow", "field_145791_d", "I"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/projectile/EntityArrow", "field_145792_e", "I"));
                                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/projectile/EntityArrow", "field_145789_f", "I"));
                                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "getBlock", "(III)Lnet/minecraft/block/Block;"));
                                toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/projectile/EntityArrow", "field_145790_g", "Lnet/minecraft/block/Block;"));
                            }
                            //Get block at the arrow's coordinates and assign it to field_145790_g, overriding the incorrect assignment just before insertion.
                            m.instructions.insert(currentInstruction, toInject);
                            System.out.println("[ArrowFix] Injected new field assignment!");
                        }
                    }
                    index++;
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ArrowFix] Applied Transform!");
        return writer.toByteArray();
    }
}
