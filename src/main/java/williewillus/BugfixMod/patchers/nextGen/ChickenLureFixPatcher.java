package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class ChickenLureFixPatcher extends AbstractPatcher {

    public ChickenLureFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction instanceof MethodInsnNode) {

            MethodInsnNode currentMIN = (MethodInsnNode) currentInstruction;

            if (currentMIN.getOpcode() == Opcodes.INVOKESPECIAL && currentMIN.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                String innerTargetOwner = MappingRegistry.getClassNameFor("net/minecraft/entity/ai/EntityAITempt");

                if (currentMIN.owner.equals(innerTargetOwner)) {
                    printMessage("Found entry point");
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
                    String wartName = MappingRegistry.getFieldNameFor("Items.nether_wart");

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

                    //Netherwart
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                    toInject.add(new InsnNode(Opcodes.DUP));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, wartName, "L" + itemName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));

                    successful = true;
                }
            }
        }
        return toInject;
    }
}
