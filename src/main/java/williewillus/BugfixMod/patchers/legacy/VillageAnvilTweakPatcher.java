package williewillus.BugfixMod.patchers.legacy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 3/15/14.
 */
@Deprecated
public class VillageAnvilTweakPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = MappingRegistry.getMethodNameFor("net.minecraft.world.gen.structure.StructureVillagePieces$House2.addComponentParts");
        String targetMethodDesc =
                "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                + "Ljava/util/Random;"
                + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureBoundingBox") + ";)Z";
        String innerTargetFieldName = MappingRegistry.getFieldNameFor("Blocks.double_stone_slab");

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(classNode,0);

        for (MethodNode m : classNode.methods) {
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
                            m.instructions.insert(currentInstruction, new FieldInsnNode(Opcodes.GETSTATIC,
                                    MappingRegistry.getClassNameFor("net/minecraft/init/Blocks"),
                                    MappingRegistry.getFieldNameFor("Blocks.anvil"),
                                    "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                            ));
                            System.out.println("[VillageAnvilTweak] Replaced double stone slab with anvil.");
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
