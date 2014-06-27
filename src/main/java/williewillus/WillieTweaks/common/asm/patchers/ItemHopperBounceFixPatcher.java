package williewillus.WillieTweaks.common.asm.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import williewillus.WillieTweaks.common.asm.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/12/2014.
 */
public class ItemHopperBounceFixPatcher extends AbstractPatcher implements ModificationPatcher {
    public ItemHopperBounceFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof MethodInsnNode && currentInstruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            if (((MethodInsnNode) currentInstruction).name.equals(MappingRegistry.getMethodNameFor("BlockHopper.setBlockBounds"))) {
                if (currentInstruction.getPrevious().getOpcode() == Opcodes.FCONST_1           // Much tedium, very dirty
                        && currentInstruction.getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_1
                        && currentInstruction.getPrevious().getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_1) {
                    AbstractInsnNode temp = currentInstruction.getPrevious().getPrevious().getPrevious().getPrevious();
                    if (temp.getOpcode() == Opcodes.FCONST_0
                            && temp.getPrevious().getOpcode() == Opcodes.FCONST_0
                            && temp.getPrevious().getPrevious().getOpcode() == Opcodes.FCONST_0) {
                        instructions.remove(currentInstruction);
                    }
                }
            }
        }
    }
}
