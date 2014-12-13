package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 *
 * Fixes broken chat opacity by reinserting a glEnable call that got lost after 1.6.4.
 */
public class ChatOpacityFixPatcher extends AbstractPatcher {

    public ChatOpacityFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.INVOKESTATIC && currentInstruction.getPrevious().getOpcode() == Opcodes.ISHL && currentInstruction.getPrevious().getPrevious().getOpcode() == Opcodes.BIPUSH) {
            printMessage("Entry point found");
            toInject.add(new IntInsnNode(Opcodes.SIPUSH, 3042));
            toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V")); // Simply insert a glEnable call that was present in 1.6.4 but absent in 1.7.x
            successful = true;
        }
        return toInject;
    }
}
