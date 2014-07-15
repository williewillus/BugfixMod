package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class HeartFlashFixPatcher extends AbstractPatcher {

    public HeartFlashFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.ICONST_0) {
            printMessage("Found entry point");
            String ecpmpClassName = MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP");
            String getHealthMethodName = MappingRegistry.getMethodNameFor("EntityClientPlayerMP.getHealth");
            String prevHealthFieldName = MappingRegistry.getFieldNameFor("EntityClientPlayerMP.prevHealth");

            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    ecpmpClassName,
                    getHealthMethodName,
                    "()F"));
            toInject.add(new VarInsnNode(Opcodes.FLOAD, 2));
            toInject.add(new InsnNode(Opcodes.FADD));
            toInject.add(new FieldInsnNode(Opcodes.PUTFIELD,
                    ecpmpClassName,
                    prevHealthFieldName,
                    "F"));
            successful = true;
        }
        return toInject;
    }
}
