package williewillus.BugfixMod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class XPFixPatcher extends AbstractPatcher {

    public XPFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();

        if (currentInstruction.getOpcode() == Opcodes.ALOAD) {
            if (currentInstruction.getPrevious().getOpcode() == Opcodes.DUP) {
                printMessage("Found initial dup instruction");
                toInject.add(new LdcInsnNode(32.0F)); //load a new float constant 32.0f and store it to use below
                toInject.add(new InsnNode(Opcodes.F2D));
                toInject.add(new VarInsnNode(Opcodes.DSTORE, 3));
                printMessage("Injected constant into instruction set");
                return toInject;
            }
        }

        if (currentInstruction.getOpcode() == Opcodes.I2D) {
            if (currentInstruction.getNext().getOpcode() == Opcodes.ALOAD) {
                //Verifying this is where the packet coordinates get cast to double before the next coordinate is loaded
                printMessage("Found a retrieval of coordinate field from packet.");
                toInject.add(new VarInsnNode(Opcodes.DLOAD, 3)); //load our constant back from vars
                toInject.add(new InsnNode(Opcodes.DDIV)); //DIVIDE!
                printMessage("Injected a division instruction");
                successful = true;
                return toInject;
            }
        }

        return toInject;
    }
}
