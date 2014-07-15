package williewillus.BugfixMod.patchers;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created by Vincent on 7/12/2014.
 */
public interface ClassPatcher {
    public void changeClass(ClassNode cn);
}
