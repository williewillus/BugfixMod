package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 3/14/14.
 */
public class ChickenLureFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = "<init>";
        String targetMethodDesc = "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ChickenLureFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();



                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();

                    if (currentInstruction instanceof MethodInsnNode) {

                        MethodInsnNode currentMIN = (MethodInsnNode) currentInstruction;

                        if (currentMIN.getOpcode() == Opcodes.INVOKESPECIAL && currentMIN.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            String innerTargetOwner = MappingRegistry.getClassNameFor("net/minecraft/entity/ai/EntityAITempt");
                            if (currentMIN.owner.equals(innerTargetOwner)) {
                                System.out.println("[ChickenLureFix] Found entry point");
                                InsnList toInject = new InsnList();

                                /*if (isObf) {
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

                                } else*/ {
                                    String chickenName = MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken");
                                    String chickenTasks = MappingRegistry.getFieldNameFor("EntityChicken.tasks");
                                    String aiTaskName = MappingRegistry.getClassNameFor("net/minecraft/entity/ai/EntityAITasks");
                                    String aiTaskAdd = MappingRegistry.getMethodNameFor("EntityAITasks.addTask");
                                    String aiTemptName = MappingRegistry.getClassNameFor("net/minecraft/entity/ai/EntityAITempt");
                                    String aiBaseName = MappingRegistry.getClassNameFor("net/minecraft/entity/ai/EntityAIBase");
                                    String creatureName = MappingRegistry.getClassNameFor("net/minecraft/entity/EntityCreature");
                                    String initItemName = MappingRegistry.getClassNameFor("net/minecraft/init/Items");
                                    String itemName = MappingRegistry.getClassNameFor("net/minecraft/item/Item");
                                    String melonName = MappingRegistry.getFieldNameFor("Items.melon_seeds");
                                    String pumpkinName = MappingRegistry.getFieldNameFor("Items.pumpkin_seeds");

                                    //Melon seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, melonName, "L" + itemName + ";"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));

                                    //Pumpkin seeds
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                                    toInject.add(new InsnNode(Opcodes.DUP));
                                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, pumpkinName, "L" + itemName + ";"));
                                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));
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
