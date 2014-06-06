package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class NextHeartFlashFixPatcher extends AbstractPatcher {

    public NextHeartFlashFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        super(name, targetClassName, targetMethodName, targetMethodDesc, targetFieldName);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.ICONST_0) {
            printMessage("Found entry point");
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                    MappingRegistry.getMethodNameFor("EntityClientPlayerMP.getHealth"),
                    "()F"));
            toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
            toInject.add(new InsnNode(Opcodes.FADD));
            toInject.add(new FieldInsnNode(Opcodes.PUTFIELD,
                    MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                    MappingRegistry.getFieldNameFor("EntityClientPlayerMP.prevHealth"),
                    "F"));
        }
        return toInject;
    }
}
