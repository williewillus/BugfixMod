package williewillus.BugfixMod;

import java.util.HashMap;

/**
 * Created by Vincent on 6/6/2014.
 */
public class MappingRegistry {
    private static HashMap<String, String> classMap = new HashMap<String, String>();
    private static HashMap<String, String> fieldMap = new HashMap<String, String>();
    private static HashMap<String, String> methodMap = new HashMap<String, String>();

    private static boolean hasInit = false;
    private static boolean isObf;

    public static void init(boolean isObfuscated) {
        if (!hasInit) {
            isObf = isObfuscated;
            if (isObf) {
                // ArrowFix
                classMap.put("net/minecraft/entity/projectile/EntityArrow", "xo");
                fieldMap.put("EntityArrow.worldObj", "p");
                fieldMap.put("EntityArrow.field_145791_d","d");
                fieldMap.put("EntityArrow.field_145792_e", "e");
                fieldMap.put("EntityArrow.field_145789_f", "f");
                fieldMap.put("EntityArrow.field_145790_g", "g");
                methodMap.put("EntityArrow.onUpdate", "h");

                classMap.put("net/minecraft/world/World", "afn");
                methodMap.put("World.getBlock", "a");

                classMap.put("net/minecraft/block/Block", "ahu");

                // ChatOpacityFix
                classMap.put("net/minecraft/client/gui/GuiNewChat", "bao");
                methodMap.put("GuiNewChat.drawChat", "a");

                // ChickenLureFix
                classMap.put("net/minecraft/entity/passive/EntityChicken", "us");
                fieldMap.put("EntityChicken.tasks", "c");

                classMap.put("net/minecraft/entity/ai/EntityAIBase", "su");
                classMap.put("net/minecraft/entity/ai/EntityAITempt", "tw");

                classMap.put("net/minecraft/entity/ai/EntityAITasks", "sv");
                methodMap.put("EntityAITasks.addTask", "a");

                classMap.put("net/minecraft/item/Item", "abn");

                classMap.put("net/minecraft/init/Items", "abq");
                fieldMap.put("Items.pumpkin_seeds", "bb");
                fieldMap.put("Items.melon_seeds", "bc");

                classMap.put("net/minecraft/entity/EntityCreature", "rp");

                // HeartFlashFix
                classMap.put("net/minecraft/client/entity/EntityClientPlayerMP", "bje");
                fieldMap.put("EntityClientPlayerMP.prevHealth", "ax");
                methodMap.put("EntityClientPlayerMP.getHealth", "aS");
                methodMap.put("EntityClientPlayerMP.attackEntityFrom", "a");

                classMap.put("net/minecraft/util/DamageSource", "qb");

                // SnowballFix
                classMap.put("net/minecraft/entity/player/EntityPlayer", "xl");
                methodMap.put("EntityPlayer.attackEntityFrom", "a");

                // ToolDesyncFix
                classMap.put("net/minecraft/item/ItemTool", "aas");
                classMap.put("net/minecraft/item/ItemStack", "abp");
                classMap.put("net/minecraft/entity/EntityLivingBase", "rh");
                fieldMap.put("World.isRemote", "E");

                // VillageAnvilTweak
                classMap.put("net.minecraft.world.gen.structure.StructureVillagePieces$House2", "auk");
                methodMap.put("net.minecraft.world.gen.structure.StructureVillagePieces$House2.addComponentParts", "a");

                classMap.put("net/minecraft/world/gen/structure/StructureBoundingBox", "arh");

                classMap.put("net/minecraft/init/Blocks", "ahz");
                fieldMap.put("Blocks.anvil", "bQ");
                fieldMap.put("Blocks.double_stone_slab", "I");

                // XPFix
                classMap.put("net/minecraft/client/network/NetHandlerPlayClient", "biv");
                methodMap.put("NetHandlerPlayClient.handleSpawnExperienceOrb", "a");

                classMap.put("net/minecraft/network/play/server/S11PacketSpawnExperieneOrb", "fo");
            }
            hasInit = true;
        }


    }

    public static String getClassNameFor(String par1, boolean par2) {
        // par2 - should the deobfuscated name be delimited by slashes (true) or periods (false)
        if (!isObf) {
            if (par2) {
                return par1.replaceAll(".", "/");
            } else {
                return par1.replaceAll("/", ".");
            }
        } else {
            return classMap.get(par1);
        }
    }

    public static String getFieldNameFor(String par1) {
        // par1 will be in the format className.fieldName
        if (!isObf) {
            return par1.substring(par1.lastIndexOf(".") + 1); // return second half, the fieldname
        } else {
            return fieldMap.get(par1);
        }
    }

    public static String getMethodNameFor(String par1) {
        // par1 will be in the format className.methodName
        if (!isObf) {
            return par1.substring(par1.lastIndexOf(".") + 1); // return second half, the fieldname
        } else {
            return methodMap.get(par1);
        }
    }
}
