package williewillus.BugfixMod.patchers.nextGen;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import williewillus.BugfixMod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 */
public class NextArrowFixPatcher extends AbstractPatcher {

    public NextArrowFixPatcher(String par1, String par2, String par3, String par4) {
        super(par1, par2, par3, par4);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        InsnList toInject = new InsnList();
        if (currentInstruction instanceof FieldInsnNode) {
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
            }
        }
        return toInject;
    }
}
