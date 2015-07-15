package williewillus.BugfixMod.coremod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.coremod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 7/15/2014.
 *
 * This makes the heart delta flash and regen blink fixes work together by assigning some fields.
 */
public class HeartFlashFixCompatPatcher extends AbstractPatcher {

    public HeartFlashFixCompatPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction.getOpcode() == Opcodes.GETFIELD && currentInstruction.getPrevious().getOpcode() == Opcodes.ALOAD) {
            printMessage("Found entry point");
            String ecpmpClassName = MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP");
            String getHealthMethodName = MappingRegistry.getMethodNameFor("EntityClientPlayerMP.getHealth");
            String prevHealthFieldName = MappingRegistry.getFieldNameFor("EntityClientPlayerMP.prevHealth");

            toInject.add(new VarInsnNode(Opcodes.FLOAD, 1));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ecpmpClassName, getHealthMethodName, "()F"));
            InsnNode insertPoint = new InsnNode(Opcodes.FCMPL);
            toInject.add(insertPoint);
            // 1
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ecpmpClassName, getHealthMethodName, "()F"));
            toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, ecpmpClassName, prevHealthFieldName, "F"));
            LabelNode label = new LabelNode();
            toInject.add(label);

            /* 1 */ toInject.insert(insertPoint, new JumpInsnNode(Opcodes.IFLE, label));

            successful = true;
        }

        return toInject;
    }
}
