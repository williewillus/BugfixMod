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
                //fieldMap.put("EntityArrow.worldObj", "p");
                //fieldMap.put("EntityArrow.field_145791_d", "d");
                //fieldMap.put("EntityArrow.field_145792_e", "e");
                //fieldMap.put("EntityArrow.field_145789_f", "f");
                //fieldMap.put("EntityArrow.field_145790_g", "g");
				// ArrowFix's bug has been FIXED by Mojang as of Minecraft 1.7.6. YAY!
                // methodMap.put("World.getBlock", "a");
                BugFixModSettings settings = BugfixModClassTransformer.instance.settings;

                if (settings.ArrowDingTweakEnabled) {
                    classMap.put("net/minecraft/entity/projectile/EntityArrow", "zc");
                    methodMap.put("EntityArrow.onUpdate", "h");
                    classMap.put("net/minecraft/entity/player/EntityPlayer", "yz");
                    classMap.put("net/minecraft/entity/monster/IMob", "yb");
                }


                if (settings.ChatOpacityFixEnabled) {
                    classMap.put("net/minecraft/client/gui/GuiNewChat", "bcc");
                    methodMap.put("GuiNewChat.drawChat", "a");
                }


                if (settings.ChickenLureFixEnabled) {
                    classMap.put("net/minecraft/entity/passive/EntityChicken", "wg");
                    fieldMap.put("EntityChicken.tasks", "c");

                    classMap.put("net/minecraft/world/World", "ahb");
                    classMap.put("net/minecraft/entity/ai/EntityAIBase", "ui");
                    classMap.put("net/minecraft/entity/ai/EntityAITempt", "vk");

                    classMap.put("net/minecraft/entity/ai/EntityAITasks", "uj");
                    methodMap.put("EntityAITasks.addTask", "a");

                    classMap.put("net/minecraft/item/Item", "adb");

                    classMap.put("net/minecraft/init/Items", "ade");
                    fieldMap.put("Items.pumpkin_seeds", "bb");
                    fieldMap.put("Items.melon_seeds", "bc");
                    fieldMap.put("Items.nether_wart", "bm");

                    classMap.put("net/minecraft/entity/EntityCreature", "td");
                }


                if (settings.HeartBlinkFixEnabled) {
                    classMap.put("net/minecraft/client/entity/EntityClientPlayerMP", "bjk");
                    fieldMap.put("EntityClientPlayerMP.hurtResistantTime", "");
                    methodMap.put("EntityClientPlayerMP.onUpdate", "");
                    methodMap.put("EntityClientPlayerMP.getHealth", "aS");

                    classMap.put("net/minecraft/client/network/NetHandlerPlayClient", "bjb");
                    methodMap.put("NetHandlerPlayClient.handleEntityMetadata", "");

                    classMap.put("net/minecraft/network/play/server/S1CPacketEntityMetadata", "");
                    methodMap.put("S1CPacketEntityMetadata.func_149376_c", "");

                    classMap.put("net/minecraft/client/gui/GuiIngame", "");
                    fieldMap.put("GuiIngame.mc", "");
                    methodMap.put("GuiIngame.func_110327_a", "a");

                    classMap.put("net/minecraft/client/Minecraft", "");
                    fieldMap.put("Minecraft.thePlayer", "");

                    classMap.put("net/minecraft/entity/DataWatcher$WatchableObject", "");
                    methodMap.put("DataWatcher$WatchableObject.getObjectType", "");


                }

                if (settings.HeartFlashFixEnabled) {
                    classMap.put("net/minecraft/client/entity/EntityClientPlayerMP", "bjk");
                    fieldMap.put("EntityClientPlayerMP.prevHealth", "aw");
                    methodMap.put("EntityClientPlayerMP.getHealth", "aS");
                    methodMap.put("EntityClientPlayerMP.attackEntityFrom", "a");

                    classMap.put("net/minecraft/util/DamageSource", "ro");
                }

                if (settings.ItemHopperBounceFixEnabled) {
                    classMap.put("net/minecraft/block/BlockHopper", "aln");
                    methodMap.put("BlockHopper.addCollisionBoxesToList", "a");
                    methodMap.put("BlockHopper.setBlockBounds", "a");

                    classMap.put("net/minecraft/world/World", "ahb");
                    classMap.put("net/minecraft/util/AxisAlignedBB", "azt");
                    classMap.put("net/minecraft/entity/Entity", "sa");
                }

                if (settings.ItemStairBounceFixEnabled) {
                    classMap.put("net/minecraft/block/BlockStairs", "ans");
                    methodMap.put("BlockStairs.addCollisionBoxesToList", "a");
                    methodMap.put("BlockStairs.setBlockBounds", "a");

                    classMap.put("net/minecraft/world/World", "ahb");
                    classMap.put("net/minecraft/util/AxisAlignedBB", "azt");
                    classMap.put("net/minecraft/entity/Entity", "sa");

                }

                if (settings.SnowballFixEnabled) {
                    classMap.put("net/minecraft/entity/player/EntityPlayer", "yz");
                    methodMap.put("EntityPlayer.attackEntityFrom", "a");

                    classMap.put("net/minecraft/util/DamageSource", "ro");
                }

                if (settings.ToolDesyncFixEnabled) {
                    classMap.put("net/minecraft/item/ItemTool", "acg");
                    methodMap.put("ItemTool.onBlockDestroyed", "a");

                    classMap.put("net/minecraft/item/ItemStack", "add");
                    classMap.put("net/minecraft/entity/EntityLivingBase", "sv");
                    classMap.put("net/minecraft/block/Block", "aji");

                    classMap.put("net/minecraft/world/World", "ahb");
                    fieldMap.put("World.isRemote", "E");
                }

                if (settings.VillageAnvilTweakEnabled) {
                    classMap.put("net/minecraft/world/World", "ahb");

                    classMap.put("net/minecraft/world/gen/structure/StructureVillagePieces$House2", "avz");
                    methodMap.put("StructureVillagePieces$House2.addComponentParts", "a");

                    classMap.put("net/minecraft/world/gen/structure/StructureBoundingBox", "asv");

                    classMap.put("net/minecraft/block/Block", "aji");
                    classMap.put("net/minecraft/init/Blocks", "ajn");
                    fieldMap.put("Blocks.anvil", "bQ");
                    fieldMap.put("Blocks.double_stone_slab", "T");
                }

                if (settings.XPFixEnabled) {
                    classMap.put("net/minecraft/client/network/NetHandlerPlayClient", "bjb");
                    methodMap.put("NetHandlerPlayClient.handleSpawnExperienceOrb", "a");

                    classMap.put("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb", "fx");
                }

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
            String get = classMap.get(request.replaceAll("\\.", "/"));
            if (get == null) {
                System.out.println("[BugfixMod] Warning: MappingRegistry just returned null for a class lookup.");
            }
            return get;
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
            String get = fieldMap.get(request);
            if (get == null) {
                System.out.println("[BugfixMod] Warning: MappingRegistry just returned null for a field lookup.");
            }
            return get;
        }
    }

    public static String getMethodNameFor(String request) {
        // par1 will be in the format className.methodName
        if (!isObf) {
            return request.substring(request.lastIndexOf(".") + 1); // return second half, the methodname
        } else {
            String get = methodMap.get(request);
            if (get == null) {
                System.out.println("[BugfixMod] Warning: MappingRegistry just returned null for a method lookup.");
            }
            return get;
        }
    }
}
