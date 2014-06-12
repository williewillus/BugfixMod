package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/12/2014.
 */
public class ItemStairBounceFixPatcher extends AbstractPatcher implements ModificationPatcher {

    public ItemStairBounceFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof MethodInsnNode && currentInstruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            if (((MethodInsnNode)currentInstruction).name.equals(MappingRegistry.getMethodNameFor("BlockStairs.setBlockBounds"))) {
                instructions.remove(currentInstruction);
            }
        }
    }
}
