package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;

import java.util.Iterator;

/**
 * Created by Vincent on 7/15/2014.
 *
 * This fix makes the hearts blink when regenning again by giving the network pipe more time to process the next packet.
 */
public class HeartBlinkFixPatcher extends AbstractPatcher {
    public HeartBlinkFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();

        if (currentInstruction.getOpcode() == Opcodes.IDIV && currentInstruction.getPrevious().getOpcode() == Opcodes.ICONST_2) {
            printMessage("Found entry point");
            toInject.add(new InsnNode(Opcodes.ICONST_1));
            toInject.add(new InsnNode(Opcodes.IADD));
            successful = true;
        }

        return toInject;
    }
}
