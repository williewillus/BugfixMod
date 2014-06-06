package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public abstract class AbstractPatcher {
    protected String targetClassName;
    protected String targetMethodName;
    protected String targetMethodDesc;
    protected String targetFieldName;
    public String name;

    public AbstractPatcher(String par1, String par2, String par3, String par4) {
        name = par1;
        targetClassName = par2;
        targetMethodName = par3;
        targetMethodDesc = par4;
    }

    public byte[] patch(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                printMessage("Found target method");
                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();
                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    InsnList toInject = buildNewInsns(currentInstruction, instructionSet);
                    if (toInject.size() > 0) {
                        m.instructions.insert(currentInstruction, toInject);
                    }
                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        printMessage("Applied transform!");
        return writer.toByteArray();
    }

    public abstract InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet);

    public void printMessage(String par1) {
        System.out.println("[" + name + "] " + par1);
    }
}
