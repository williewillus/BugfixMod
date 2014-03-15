package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 3/11/14.
 */
public class XPFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName, targetMethodDesc;

        if (isObf) {
            targetMethodName = "a";
            targetMethodDesc = "(Lfo;)V";
        } else {
            targetMethodName = "handleSpawnExperienceOrb";
            targetMethodDesc = "(Lnet/minecraft/network/play/server/S11PacketSpawnExperienceOrb;)V";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        Iterator<MethodNode> methods = classNode.methods.iterator();

        while (methods.hasNext()) {
            MethodNode m = methods.next();


            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[XPFix] Found target method");

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction.getOpcode() == Opcodes.ALOAD) {
                        if (currentInstruction.getPrevious().getOpcode() == Opcodes.DUP) {
                            System.out.println("[XPFix] Found initial dup instruction");
                            InsnList toInject = new InsnList();
                            toInject.add(new LdcInsnNode(new Float(32.0f))); //load a new float constant 32.0f and store it to use below
                            toInject.add(new InsnNode(Opcodes.F2D));
                            toInject.add(new VarInsnNode(Opcodes.DSTORE, 3));
                            m.instructions.insert(currentInstruction, toInject);
                            System.out.println("[XPFix] Injected constant into instruction set");
                        }
                    }
                    if (currentInstruction.getOpcode() == Opcodes.I2D) {
                        if (currentInstruction.getNext().getOpcode() == Opcodes.ALOAD) {
                            //Verifying this is where the packet coordinates get cast to double before the next coordinate is loaded
                            System.out.println("[XPFix] Found a retrieval of coordinate field from packet.");
                            InsnList toInject = new InsnList();
                            toInject.add(new VarInsnNode(Opcodes.DLOAD, 3)); //load our constant back from vars
                            toInject.add(new InsnNode(Opcodes.DDIV)); //DIVIDE!
                            m.instructions.insert(currentInstruction, toInject);
                            System.out.println("[XPFix] Injected a division instruction");
                        }
                    }

                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[XPFix] Successfully Applied Transform!");
        return writer.toByteArray();
    }
}
