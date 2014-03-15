package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

/**
 * Created by Vincent on 3/15/14.
 */
public class VillageAnvilTweakPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName,targetMethodDesc,innerTargetFieldName;

        if (isObf) {
            targetMethodName = "a";
            targetMethodDesc = "(Lafn;Ljava/util/Random;Larh;)Z";
            innerTargetFieldName = "T";
        } else {
            targetMethodName = "addComponentParts";
            targetMethodDesc = "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)Z";
            innerTargetFieldName = "double_stone_slab";
        }

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(classNode,0);
        Iterator<MethodNode> methods = classNode.methods.iterator();

        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[VillageAnvilTweak] Found target method: " + m.name);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction instanceof FieldInsnNode) {
                        FieldInsnNode f = (FieldInsnNode) currentInstruction;
                        if (f.name.equals(innerTargetFieldName)) {
                            System.out.println("[VillageAnvilTweak] Found entry point: " + f.owner + " " + f.name + " " + f.desc);
                            if (isObf) {
                                m.instructions.insert(currentInstruction, new FieldInsnNode(Opcodes.GETSTATIC, "ahz", "bQ", "Lahu;"));
                                System.out.println("[VillageAnvilTweak] Replaced double stone slab with anvil.");
                            } else {
                                m.instructions.insert(currentInstruction, new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", "anvil", "Lnet/minecraft/block/Block;"));
                                System.out.println("[VillageAnvilTweak] Replaced double stone slab with anvil.");
                            }
                            m.instructions.remove(currentInstruction);
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[VillageAnvilTweak] Applied Transform!");
        return writer.toByteArray();
    }
}
