package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/12/2014.
 *
 * This fix removes the setblockbounds to a full size block, preventing items from boinging around on the stair step.
 */
public class ItemStairBounceFixPatcher extends AbstractPatcher implements ModificationPatcher {

    public ItemStairBounceFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof MethodInsnNode && currentInstruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            String sbbMethodName = MappingRegistry.getMethodNameFor("BlockStairs.setBlockBounds");
            if (((MethodInsnNode)currentInstruction).name.equals(sbbMethodName)) {
                instructions.remove(currentInstruction);
                successful = true;
            }
        }
    }
}
