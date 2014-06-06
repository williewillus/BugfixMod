package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public interface AbstractRemovalPatcher {
    public abstract void removeInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions);
}
