package williewillus.BugfixMod.patchers;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
@Deprecated
public class ArrowFixPatcher extends AbstractPatcher {


    public ArrowFixPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        /*if (currentInstruction instanceof FieldInsnNode) {
            if (((FieldInsnNode) currentInstruction).name.equals(targetFieldName) && currentInstruction.getOpcode() == Opcodes.PUTFIELD) {
                printMessage("Found entry point!");
                String entityArrowName = MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow");
                String worldName = MappingRegistry.getClassNameFor("net/minecraft/world/World");
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.worldObj"), "L" + worldName + ";"));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145791_d"), "I"));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145792_e"), "I"));
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new FieldInsnNode(Opcodes.GETFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145789_f"), "I"));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, worldName, MappingRegistry.getMethodNameFor("World.getBlock"), "(III)L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"));
                toInject.add(new FieldInsnNode(Opcodes.PUTFIELD, entityArrowName, MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g"), "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"));
                //Get block at the arrow's coordinates and assign it to field_145790_g, overriding the incorrect assignment just before insertion.
                printMessage("Injected new field assignment!");
                successful = true;
            }
        }*/
        return toInject;
    }
}
