package williewillus.BugfixMod.patchers.legacy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/5/2014.
 */
@Deprecated
public class HeartFlashFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom");
        String targetMethodDesc = "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[HeartFlashFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                while(instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction.getOpcode() == Opcodes.ICONST_0) {
                        System.out.println("[HeartFlashFix] Found entry point");
                        InsnList toInject = new InsnList();
                        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                                MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                                MappingRegistry.getMethodNameFor("EntityClientPlayerMP.getHealth"),
                                "()F"));
                        toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
                        toInject.add(new InsnNode(Opcodes.FADD));
                        toInject.add(new FieldInsnNode(Opcodes.PUTFIELD,
                                MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                                MappingRegistry.getFieldNameFor("EntityClientPlayerMP.prevHealth"),
                                "F"));
                        m.instructions.insertBefore(currentInstruction, toInject);
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[HeartFlashFix] Applied Transform!");
        return writer.toByteArray();
    }
}
