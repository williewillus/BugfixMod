package williewillus.WillieTweaks.common.asm.patchers;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public interface ModificationPatcher {
    // For patchers that do not just add bytecode
    public abstract void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions);
    // Passes instruction list itself so the patcher can modify it at free will.
}
