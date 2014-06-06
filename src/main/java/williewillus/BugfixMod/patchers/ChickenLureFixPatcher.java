package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 3/14/14.
 */
public class ChickenLureFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName, targetMethodDesc;
        if (isObf) {
            targetMethodName = "<init>";
            targetMethodDesc = "(Lafn;)V";
        } else {
            targetMethodName = "<init>";
            targetMethodDesc = "(Lnet/minecraft/world/World;)V";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ChickenLureFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                String innerTargetOwner = isObf ? "tw" : "net/minecraft/entity/ai/EntityAITempt";


                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();

                    if (currentInstruction instanceof MethodInsnNode) {

                        MethodInsnNode currentMIN = (MethodInsnNode) currentInstruction;

                        if (currentMIN.getOpcode() == Opcodes.INVOKESPECIAL && currentMIN.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {

                            if (currentMIN.owner.equals(innerTargetOwner)) {
                                System.out.println("[ChickenLureFix] Found entry point");
                                InsnList toInject = new InsnList();

                                if (isObf) {
                                    //Melon seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "us", "c", "Lsv;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, "tw"));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "abq", "bc", "Labn;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "tw", "<init>", "(Lrp;DLabn;Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "sv", "a", "(ILsu;)V"));

                                    //Pumpkin seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "us", "c", "Lsv;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, "tw"));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "abq", "bb", "Labn;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "tw", "<init>", "(Lrp;DLabn;Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "sv", "a", "(ILsu;)V"));

                                } else {

                                    //Melon seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityChicken", "tasks", "Lnet/minecraft/entity/ai/EntityAITasks;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/entity/ai/EntityAITempt"));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "melon_seeds", "Lnet/minecraft/item/Item;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/ai/EntityAITempt", "<init>", "(Lnet/minecraft/entity/EntityCreature;DLnet/minecraft/item/Item;Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/ai/EntityAITasks", "addTask", "(ILnet/minecraft/entity/ai/EntityAIBase;)V"));

                                    //Pumpkin seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityChicken", "tasks", "Lnet/minecraft/entity/ai/EntityAITasks;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/entity/ai/EntityAITempt"));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "pumpkin_seeds", "Lnet/minecraft/item/Item;"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/ai/EntityAITempt", "<init>", "(Lnet/minecraft/entity/EntityCreature;DLnet/minecraft/item/Item;Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/ai/EntityAITasks", "addTask", "(ILnet/minecraft/entity/ai/EntityAIBase;)V"));
                                }

                                m.instructions.insert(currentInstruction, toInject);

                            }
                        }
                    }
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ChickenLureFix] Applied Transform!");
        return writer.toByteArray();

    }
}
