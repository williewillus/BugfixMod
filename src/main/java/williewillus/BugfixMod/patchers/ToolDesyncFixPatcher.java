package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 *
 * This fix makes Unbreaking tools not desync anymore by only damaging tools serverside
 */
public class ToolDesyncFixPatcher extends AbstractPatcher {

    public ToolDesyncFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.IFEQ && currentInstruction.getPrevious().getOpcode() == Opcodes.DCMPL) {
            printMessage("Found entry point.");
            String worldClassName = MappingRegistry.getClassNameFor("net/minecraft/world/World");
            String remoteFieldName = MappingRegistry.getFieldNameFor("World.isRemote");

            toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            toInject.add(new FieldInsnNode(Opcodes.GETFIELD,
                    worldClassName,
                    remoteFieldName,
                    "Z"));
            toInject.add(new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode) currentInstruction).label));
            successful = true;
        }
        return toInject;
    }
}
