package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 3/21/2014.
 */
public class ToolDesyncFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName,targetMethodDesc;
        if (isObf) {
            targetMethodName = "a";
            targetMethodDesc = "(Labp;Lafn;Lahu;IIILrh;)Z";
        } else {
            targetMethodName =  "onBlockDestroyed";
            targetMethodDesc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/Block;IIILnet/minecraft/entity/EntityLivingBase;)Z";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        Iterator<MethodNode> methods = classNode.methods.iterator();


        while (methods.hasNext()) {
            MethodNode m = methods.next();

            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ToolDesyncFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();


                int index = 0;
                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction.getOpcode() == Opcodes.IFEQ && currentInstruction.getPrevious().getOpcode() == Opcodes.DCMPL) {
                        System.out.println("[ToolDesyncFix] Found entry point.");
                        InsnList toInject = new InsnList();
                        toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        if (isObf) {
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "afn", "E", "Z"));
                        } else {
                            toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "isRemote", "Z"));
                        }
                        toInject.add(new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode) currentInstruction).label));
                        m.instructions.insert(currentInstruction, toInject);
                    }
                    index++;
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ToolDesyncFix] Applied Transform!");
        return writer.toByteArray();
    }
}
