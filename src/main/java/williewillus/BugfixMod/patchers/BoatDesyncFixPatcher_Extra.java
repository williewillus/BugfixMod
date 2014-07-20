package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

/**
 * Created by Vincent on 7/19/2014.
 */
public class BoatDesyncFixPatcher_Extra extends AbstractPatcher implements ModificationPatcher {

    public BoatDesyncFixPatcher_Extra(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction.getOpcode() == Opcodes.ICONST_5 && currentInstruction.getNext().getOpcode() == Opcodes.IADD) {
            printMessage("Found entry point");
            instructions.remove(currentInstruction.getNext());
            instructions.remove(currentInstruction);
            successful = true;
        }
    }
}
