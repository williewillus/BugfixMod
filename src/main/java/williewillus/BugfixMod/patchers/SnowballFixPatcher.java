package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import williewillus.BugfixMod.MappingRegistry;

/**
 * Created by Vincent on 3/11/14.
 */
public class SnowballFixPatcher {

    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName = MappingRegistry.getMethodNameFor("EntityPlayer.attackEntityFrom");
        String targetMethodDesc = "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[SnowballFix] Found target method: " + targetMethodName);

                InsnList instructionSet = m.instructions;

                for (int i = 0; i < instructionSet.size(); i++) {
                    if (instructionSet.get(i).getOpcode() == Opcodes.IFNE && instructionSet.get(i).getPrevious().getOpcode() == Opcodes.FCMPL) {
                        System.out.println("[SnowballFix] Found entry point: " + i);
                        if (instructionSet.get(i + 3).getOpcode() == Opcodes.ICONST_0) {
                            instructionSet.remove(instructionSet.get(i).getNext().getNext().getNext().getNext());
                            instructionSet.remove(instructionSet.get(i).getNext().getNext().getNext());
                            //Remove instruction to return without action when damagesource health is 0. In other words, being dealt 0 damage shows everything associated with damage.
                            //Including gui tilt, heart flash, sound, and, most importantly, knockback. This was behavior in older versions but was somehow removed in more recent years.
                        }
                    }
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[SnowballFix] Applied Transform!");
        return writer.toByteArray();
    }
}
