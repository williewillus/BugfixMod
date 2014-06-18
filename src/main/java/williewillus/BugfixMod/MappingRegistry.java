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

    public static void init(boolean isObf) {
        if (!hasInit) {
            MappingRegistry.isObf = isObf;
            if (MappingRegistry.isObf) {
                // ArrowFix
                classMap.put("net/minecraft/entity/projectile/EntityArrow", "xo");
                fieldMap.put("EntityArrow.worldObj", "p");
                fieldMap.put("EntityArrow.field_145791_d", "d");
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
                classMap.put("net/minecraft/world/gen/structure/StructureVillagePieces$House2", "auk");
                methodMap.put("StructureVillagePieces$House2.addComponentParts", "a");

                classMap.put("net/minecraft/world/gen/structure/StructureBoundingBox", "arh");

                classMap.put("net/minecraft/init/Blocks", "ahz");
                fieldMap.put("Blocks.anvil", "bQ");
                fieldMap.put("Blocks.double_stone_slab", "T");

                // XPFix
                classMap.put("net/minecraft/client/network/NetHandlerPlayClient", "biv");
                methodMap.put("NetHandlerPlayClient.handleSpawnExperienceOrb", "a");

                classMap.put("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb", "fo");

                // ItemStairBounceFix
                classMap.put("net/minecraft/block/BlockStairs", "ame");
                classMap.put("net/minecraft/util/AxisAlignedBB", "ayf");
                classMap.put("net/minecraft/entity/Entity", "qn");

                methodMap.put("BlockStairs.addCollisionBoxesToList", "a");
                methodMap.put("BlockStairs.setBlockBounds", "a");

                // ItemHopperBounceFix
                classMap.put("net/minecraft/block/BlockHopper", "ajz");

                methodMap.put("BlockHopper.addCollisionBoxesToList", "a");
                methodMap.put("BlockHopper.setBlockBounds", "a");

                // ArrowDingTweak
                classMap.put("net/minecraft/entity/monster/IMob", "wn");
            }
            hasInit = true;
        }


    }

    public static String getClassNameFor(String request, boolean useSlashes) {
        // par2 - should the deobfuscated name be delimited by slashes (true) or periods (false)
        if (!isObf) {
            if (useSlashes) {
                return request.replaceAll("\\.", "/");
            } else {
                return request.replaceAll("/", ".");
            }
        } else {
            return classMap.get(request.replaceAll("\\.", "/"));
        }
    }

    public static String getClassNameFor(String request) {
        return getClassNameFor(request, true);
    }

    public static String getFieldNameFor(String request) {
        // par1 will be in the format className.fieldName
        if (!isObf) {
            return request.substring(request.lastIndexOf(".") + 1); // return second half, the fieldname
        } else {
            return fieldMap.get(request);
        }
    }

    public static String getMethodNameFor(String request) {
        // par1 will be in the format className.methodName
        if (!isObf) {
            return request.substring(request.lastIndexOf(".") + 1); // return second half, the methodname
        } else {
            return methodMap.get(request);
        }
    }
}
