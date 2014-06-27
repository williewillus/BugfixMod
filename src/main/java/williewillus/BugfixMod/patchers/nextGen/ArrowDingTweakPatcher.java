package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TypeInsnNode;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/17/2014.
 */
public class ArrowDingTweakPatcher extends AbstractPatcher implements ModificationPatcher {

    public ArrowDingTweakPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }

    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof TypeInsnNode && currentInstruction.getOpcode() == Opcodes.INSTANCEOF) {
            if (((TypeInsnNode) currentInstruction).desc.equals(MappingRegistry.getClassNameFor("net/minecraft/entity/player/EntityPlayer"))) {
                if (currentInstruction.getPrevious().getPrevious().getPrevious().getOpcode() == Opcodes.IF_ACMPEQ) {
                    printMessage("Found entry point!");
                    instructions.insert(currentInstruction, new TypeInsnNode(Opcodes.INSTANCEOF, MappingRegistry.getClassNameFor("net/minecraft/entity/monster/IMob")));
                    instructions.remove(currentInstruction);
                }
            }
        }
    }
}
