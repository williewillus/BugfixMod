package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class ToolDesyncFixPatcher extends AbstractPatcher {

    public ToolDesyncFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.IFEQ && currentInstruction.getPrevious().getOpcode() == Opcodes.DCMPL) {
            printMessage("Found entry point.");
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            toInject.add(new FieldInsnNode(Opcodes.GETFIELD,
                    MappingRegistry.getClassNameFor("net/minecraft/world/World"),
                    MappingRegistry.getFieldNameFor("World.isRemote"),
                    "Z"));
            toInject.add(new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode) currentInstruction).label));
            successful = true;
        }
        return toInject;
    }
}
