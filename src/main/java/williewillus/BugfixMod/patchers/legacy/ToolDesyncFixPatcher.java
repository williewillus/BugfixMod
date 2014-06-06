package williewillus.BugfixMod.patchers.legacy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 3/21/2014.
 */
@Deprecated
public class ToolDesyncFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = MappingRegistry.getMethodNameFor("ItemTool.onBlockDestroyed");
        String targetMethodDesc =
                "(L" + MappingRegistry.getClassNameFor("net/minecraft/item/ItemStack") + ";"
                + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                + "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                + "IIIL" + MappingRegistry.getClassNameFor("net/minecraft/entity/EntityLivingBase") + ";)Z";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ToolDesyncFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction.getOpcode() == Opcodes.IFEQ && currentInstruction.getPrevious().getOpcode() == Opcodes.DCMPL) {
                        System.out.println("[ToolDesyncFix] Found entry point.");
                        InsnList toInject = new InsnList();
                        toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        toInject.add(new FieldInsnNode(Opcodes.GETFIELD,
                                MappingRegistry.getClassNameFor("net/minecraft/world/World"),
                                MappingRegistry.getFieldNameFor("World.isRemote"),
                                "Z"));
                        toInject.add(new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode) currentInstruction).label));
                        m.instructions.insert(currentInstruction, toInject);
                    }
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ToolDesyncFix] Applied Transform!");
        return writer.toByteArray();
    }
}
