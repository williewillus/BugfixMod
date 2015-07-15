package williewillus.BugfixMod.coremod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import williewillus.BugfixMod.coremod.BugfixModClassTransformer;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public abstract class AbstractPatcher {
    public String patcherName;
    protected String targetClassName;
    protected String targetMethodName;
    protected String targetMethodDesc;
    protected boolean successful;

    public AbstractPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        this.patcherName = name;
        this.targetClassName = targetClassName;
        this.targetMethodName = targetMethodName;
        this.targetMethodDesc = targetMethodDesc;
    }

    public byte[] patch(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(bytes);
        } catch (NullPointerException e) { // Thrown when LiteLoader classes are read.
            // Silently ignore.
            return bytes;
        }
        classReader.accept(classNode, 0);

        if (classNode.name.equals(targetClassName)) {
            for (MethodNode method : classNode.methods) {
                if (method.name.equals(targetMethodName) && method.desc.equals(targetMethodDesc)) {
                    printMessage("Found target method");
                    AbstractInsnNode currentInstruction;
                    Iterator<AbstractInsnNode> instructionSet = method.instructions.iterator();
                    while (instructionSet.hasNext()) {
                        currentInstruction = instructionSet.next();
                        if (this instanceof ModificationPatcher) {
                            ((ModificationPatcher) this).modifyInsns(currentInstruction, instructionSet, method.instructions);
                        } else {
                            InsnList toInject = buildNewInsns(currentInstruction, instructionSet);
                            if (toInject.size() > 0) {
                                method.instructions.insert(currentInstruction, toInject);
                            }
                        }
                    }
                }
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            printMessage(successful ? "Applied transform!" : "Failed to apply transform!");
            return writer.toByteArray();
        } else {
            return bytes;
        }
    }

    public abstract InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet);

    public void printMessage(String message) {
        BugfixModClassTransformer.instance.logger.info("[" + patcherName + "] " + message);
    }
}
