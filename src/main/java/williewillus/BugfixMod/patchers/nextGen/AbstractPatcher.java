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
    public String patcherName;
    protected String targetClassName;
    protected String targetMethodName;
    protected String targetMethodDesc;
    protected String targetFieldName;

    public AbstractPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc, String targetFieldName) {
        this.patcherName = name;
        this.targetClassName = targetClassName;
        this.targetMethodName = targetMethodName;
        this.targetMethodDesc = targetMethodDesc;
        this.targetFieldName = targetFieldName;
    }

    public byte[] patch(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        if (classNode.name.equals(targetClassName)) {
            for (MethodNode m : classNode.methods) {
                if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                    printMessage("Found target method");
                    AbstractInsnNode currentInstruction;
                    Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();
                    while (instructionSet.hasNext()) {
                        currentInstruction = instructionSet.next();
                        if (this instanceof ModificationPatcher) {
                            ((ModificationPatcher) this).modifyInsns(currentInstruction, instructionSet, m.instructions);
                        } else {
                            InsnList toInject = buildNewInsns(currentInstruction, instructionSet);
                            if (toInject.size() > 0) {
                                m.instructions.insert(currentInstruction, toInject);
                            }
                        }
                    }
                }
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            printMessage("Applied transform!");
            return writer.toByteArray();
        } else {
            return bytes;
        }
    }

    public abstract InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet);

    public void printMessage(String par1) {
        System.out.println("[" + patcherName + "] " + par1);
    }
}
