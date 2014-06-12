package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class VillageAnvilTweakPatcher extends AbstractPatcher implements ModificationPatcher {

    public VillageAnvilTweakPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }


    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof FieldInsnNode) {
            FieldInsnNode f = (FieldInsnNode) currentInstruction;
            if (f.name.equals(targetFieldName)) {
                printMessage("Found entry point: " + f.owner + " " + f.name + " " + f.desc);
                instructions.insert(currentInstruction, new FieldInsnNode(Opcodes.GETSTATIC,
                        MappingRegistry.getClassNameFor("net/minecraft/init/Blocks"),
                        MappingRegistry.getFieldNameFor("Blocks.anvil"),
                        "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                ));
                printMessage("Replaced double stone slab with anvil.");
                instructions.remove(currentInstruction);
            }
        }
    }
}
