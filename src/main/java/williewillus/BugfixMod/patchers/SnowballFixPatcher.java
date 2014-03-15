package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

/**
 * Created by Vincent on 3/11/14.
 */
public class SnowballFixPatcher {

    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName, targetMethodDesc;
        if (isObf) {
            targetMethodName = "a";
            targetMethodDesc = "(Lqb;F)Z";
        } else {
            targetMethodName = "attackEntityFrom";
            targetMethodDesc = "(Lnet/minecraft/util/DamageSource;F)Z";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        Iterator<MethodNode> methods = classNode.methods.iterator();


        while (methods.hasNext()) {
            MethodNode m = methods.next();

            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("Found target method: " + targetMethodName);

                InsnList instructionSet = m.instructions;

                for (int i = 0; i < instructionSet.size(); i++) {
                    if (instructionSet.get(i).getOpcode() == Opcodes.IFNE && instructionSet.get(i).getPrevious().getOpcode() == Opcodes.FCMPL) {
                        System.out.println("Found entry point: " + i);
                        if (instructionSet.get(i + 3).getOpcode() == Opcodes.ICONST_0) {
                            instructionSet.remove(instructionSet.get(i).getNext().getNext().getNext().getNext());
                            instructionSet.remove(instructionSet.get(i).getNext().getNext().getNext());
                            //Remove instruction to return without action when damagesource health is 0. In other words, being dealt 0 damage shows everything associated with damage.
                            //Including gui tilt, heart flash, sound, and, most importantly, knockback.
                            System.out.println("hi");
                        }
                    }
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("SnowballFix Applied Transform!");
        return writer.toByteArray();
    }
}
