package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 4/18/2015.
 */
public class PushTweakPatcher extends AbstractPatcher implements ModificationPatcher {
    public PushTweakPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }


    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof JumpInsnNode) {
            if (currentInstruction.getPrevious() instanceof FieldInsnNode) {
                FieldInsnNode field = ((FieldInsnNode) currentInstruction.getPrevious());
                if (field.name.equals(MappingRegistry.getFieldNameFor("World.isRemote"))) {
                    instructions.remove(currentInstruction.getPrevious().getPrevious().getPrevious());
                    instructions.remove(currentInstruction.getPrevious().getPrevious());
                    instructions.remove(currentInstruction.getPrevious());
                    instructions.remove(currentInstruction);
                }
            }
        }
    }
}
