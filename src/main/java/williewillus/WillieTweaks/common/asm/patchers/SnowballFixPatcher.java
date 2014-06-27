package williewillus.WillieTweaks.common.asm.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class SnowballFixPatcher extends AbstractPatcher implements ModificationPatcher {

    public SnowballFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i).getOpcode() == Opcodes.IFNE && instructions.get(i).getPrevious().getOpcode() == Opcodes.FCMPL) {
                if (instructions.get(i + 3).getOpcode() == Opcodes.ICONST_0) {
                    instructions.remove(instructions.get(i).getNext().getNext().getNext().getNext());
                    instructions.remove(instructions.get(i).getNext().getNext().getNext());
                    printMessage("Removed instructions");
                    //Remove instruction to return without action when damagesource health is 0. In other words, being dealt 0 damage shows everything associated with damage.
                    //Including gui tilt, heart flash, sound, and, most importantly, knockback. This was behavior in older versions but was somehow removed in more recent years.
                }
            }
        }
    }
}
