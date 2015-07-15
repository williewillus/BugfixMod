package williewillus.BugfixMod.coremod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import williewillus.BugfixMod.coremod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/12/2014.
 *
 * This fix removes the setblockbounds to a full size block, preventing items from boinging around on locked hoppers.
 */
public class ItemHopperBounceFixPatcher extends AbstractPatcher implements ModificationPatcher {
    public ItemHopperBounceFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof MethodInsnNode && currentInstruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            String sbbMethodName = MappingRegistry.getMethodNameFor("BlockHopper.setBlockBounds");

            if (((MethodInsnNode) currentInstruction).name.equals(sbbMethodName)) {
                if (currentInstruction.getPrevious().getOpcode() == Opcodes.FCONST_1           // Much tedium, very dirty
                        && currentInstruction.getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_1
                        && currentInstruction.getPrevious().getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_1) {
                    AbstractInsnNode temp = currentInstruction.getPrevious().getPrevious().getPrevious().getPrevious();
                    if (temp.getOpcode() == Opcodes.FCONST_0
                            && temp.getPrevious().getOpcode() == Opcodes.FCONST_0
                            && temp.getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_0) {
                        instructions.remove(currentInstruction);
                        successful = true;
                    }
                }
            }
        }
    }
}
