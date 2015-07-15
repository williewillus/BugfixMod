package williewillus.BugfixMod.coremod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import williewillus.BugfixMod.coremod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 *
 * This tweak replaces the double stone slab in a blacksmith house with an anvil.
 */
public class VillageAnvilTweakPatcher extends AbstractPatcher implements ModificationPatcher {

    public VillageAnvilTweakPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        return new InsnList();
    }


    @Override
    public void modifyInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet, InsnList instructions) {
        if (currentInstruction instanceof FieldInsnNode) {
            FieldInsnNode f = (FieldInsnNode) currentInstruction;
            String doubleSlabFieldName = MappingRegistry.getFieldNameFor("Blocks.double_stone_slab");

            if (f.name.equals(doubleSlabFieldName)) {
                printMessage("Found entry point: " + f.owner + " " + f.name + " " + f.desc);
                String blocksClassName = MappingRegistry.getClassNameFor("net/minecraft/init/Blocks");
                String anvilFieldName = MappingRegistry.getFieldNameFor("Blocks.anvil");
                String blockClassName = MappingRegistry.getClassNameFor("net/minecraft/block/Block");

                instructions.insert(currentInstruction, new FieldInsnNode(Opcodes.GETSTATIC,
                        blocksClassName,
                        anvilFieldName,
                        "L" + blockClassName + ";"
                ));
                printMessage("Replaced double stone slab with anvil.");
                instructions.remove(currentInstruction);
                successful = true;
            }
        }
    }
}
