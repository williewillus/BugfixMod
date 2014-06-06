package williewillus.BugfixMod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import williewillus.BugfixMod.patchers.legacy.*;
import williewillus.BugfixMod.patchers.nextGen.*;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Vincent on 3/10/14.
 */
public class BugfixModClassTransformer implements IClassTransformer {

    private boolean hasInit = false;
    public static BugfixModClassTransformer instance;
    private BugFixModSettings settings;
    public File settingsFile;
    private boolean isObf;
    private ArrayList<AbstractPatcher> patchers;

    public BugfixModClassTransformer() {
        if (instance != null) {
            throw new RuntimeException("Only one transformer may exist!");
        } else {
            instance = this;

        }

    }

    public void initialize(Boolean par1isObf) {
        if (!hasInit) {
            isObf = par1isObf;
            MappingRegistry.init(par1isObf);
            setupPatchers();
            Configuration config = new Configuration(settingsFile);
            config.load();
            settings = new BugFixModSettings();

            settings.ArrowFixEnabled = config.get("COMMON", "ArrowFixEnabled", true).getBoolean(true);
            settings.SnowballFixEnabled = config.get("COMMON", "SnowballFixEnabled", true).getBoolean(true);
            settings.ChickenLureFixEnabled = config.get("COMMON", "ChickenLureFixEnabled", true).getBoolean(true);


            settings.LinkCommandEnabled = config.get("TWEAKS", "LinkCommandEnabled", false).getBoolean(false);
            settings.VillageAnvilTweakEnabled = config.get("TWEAKS","VillageAnvilTweakEnabled",false).getBoolean(false);

            settings.XPFixEnabled = config.get("CLIENT", "XPFixEnabled", true).getBoolean(true);
            settings.ChatOpacityFixEnabled = config.get("CLIENT", "ChatOpacityFixEnabled", true).getBoolean(true);
            settings.ToolDesyncFixEnabled = config.get("CLIENT", "ToolDesyncFixEnabled", false).getBoolean(false);
            settings.HeartFlashFixEnabled = config.get("CLIENT", "HeartFlashFixEnabled", true).getBoolean(true);

            config.save();


            hasInit = true;
        }
    }

    public byte[] transform(String par1, String par2, byte[] bytes) {
        if (hasInit) {
            if (settings.ArrowFixEnabled) {
                bytes = patchers.get(0).patch(bytes);
            }
            if (settings.ChatOpacityFixEnabled) {
                bytes = patchers.get(1).patch(bytes);
            }
            if (settings.ChickenLureFixEnabled) {
                bytes = patchers.get(2).patch(bytes);
            }
            if (settings.HeartFlashFixEnabled) {
                bytes = patchers.get(3).patch(bytes);
            }
            if (settings.SnowballFixEnabled) {
                bytes = patchers.get(4).patch(bytes);
            }
            if (settings.ToolDesyncFixEnabled) {
                bytes = patchers.get(5).patch(bytes);
            }
            if (settings.VillageAnvilTweakEnabled) {
                bytes = patchers.get(6).patch(bytes);
            }
            if (settings.XPFixEnabled) {
                bytes = patchers.get(7).patch(bytes);
            }
        }
        return bytes;
    }

    public byte[] transform2(String par1, String par2, byte[] bytes) {
        if (par1.equals("net.minecraft.entity.projectile.EntityArrow") || par1.equals("xo")) {
            if (settings.ArrowFixEnabled) {
                return ArrowFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ArrowFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.client.network.NetHandlerPlayClient") || par1.equals("biv")) {
            if (settings.XPFixEnabled) {
                return XPFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("XPFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.entity.player.EntityPlayer") || par1.equals("xl")) {
            if (settings.SnowballFixEnabled) {
                return SnowballFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("SnowballFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.client.gui.GuiNewChat") || par1.equals("bao")) {
            if (settings.ChatOpacityFixEnabled) {
                return ChatOpacityFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ChatOpacityFix disabled, skipping patch");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.entity.passive.EntityChicken") || par1.equals("us")) {
            if (settings.ChickenLureFixEnabled) {
                return ChickenLureFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ChickenLureFix disabled, skipping patch");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.world.gen.structure.StructureVillagePieces$House2") || par1.equals("auk")) {
            if (settings.VillageAnvilTweakEnabled) {
                return VillageAnvilTweakPatcher.patch(bytes, isObf);
            } else {
                System.out.println("VillageAnvilTweak disabled, skipping patch");
            }
        }

        if (par1.equals("net.minecraft.item.ItemTool") || par1.equals("aas")) {
            if (settings.ToolDesyncFixEnabled) {
                return ToolDesyncFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ToolDesyncFix disabled, skipping patch");
            }
        }

        if (par1.equals("net.minecraft.client.entity.EntityClientPlayerMP") || par1.equals("bje")) {
            if (settings.HeartFlashFixEnabled) {
                return HeartFlashFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("HeartFlashFix disabled, skipping patch");
            }
        }

        return bytes;
    }

    private void setupPatchers() {
        if (patchers != null) {
            System.out.println("Patcher already initialized!!");
        } else {
            patchers = new ArrayList<AbstractPatcher>(15);

            patchers.add(new NextArrowFixPatcher(
                    "ArrowFix",
                    MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow"),
                    MappingRegistry.getMethodNameFor("EntityArrow.onUpdate"),
                    "()V",
                    MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g")
            ));

            patchers.add(new NextChatOpacityFixPatcher(
                    "ChatOpacityFix",
                    MappingRegistry.getClassNameFor("net/minecraft/client/gui/GuiNewChat"),
                    MappingRegistry.getMethodNameFor("GuiNewChat.drawChat"),
                    "(I)V",
                    ""
            ));

            patchers.add(new NextChickenLureFixPatcher(
                    "ChickenLureFix",
                    MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken"),
                    "<init>",
                    "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V",
                    ""
            ));

            patchers.add(new NextHeartFlashFixPatcher(
                    "HeartFlashFix",
                    MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                    MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom"),
                    "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                    ""
            ));

            patchers.add(new NextSnowballFixPatcher(
                    "SnowballFix",
                    MappingRegistry.getClassNameFor("net/minecraft/entity/player/EntityPlayer"),
                    MappingRegistry.getMethodNameFor("EntityPlayer.attackEntityFrom"),
                    "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                    ""
            ));

            String sig = "(L" + MappingRegistry.getClassNameFor("net/minecraft/item/ItemStack") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                    + "IIIL" + MappingRegistry.getClassNameFor("net/minecraft/entity/EntityLivingBase") + ";)Z";

            patchers.add(new NextToolDesyncFixPatcher(
                    "ToolDesyncFix",
                    MappingRegistry.getClassNameFor("net/minecraft/item/ItemTool"),
                    MappingRegistry.getMethodNameFor("ItemTool.onBlockDestroyed"),
                    sig, // break out into separate block above for readability
                    ""
            ));

            String sig2 = "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "Ljava/util/Random;"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureBoundingBox") + ";)Z";

            patchers.add(new NextVillageAnvilTweakPatcher(
                    "VillageAnvilTweak",
                    MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureVillagePieces$House2"),
                    MappingRegistry.getMethodNameFor("StructureVillagePieces$House2.addComponentParts"),
                    sig2, // break out into separate block above for readability
                    MappingRegistry.getFieldNameFor("Blocks.double_stone_slab")
            ));

            patchers.add(new NextXPFixPatcher(
                    "XPFix",
                    MappingRegistry.getClassNameFor("net/minecraft/client/network/NetHandlerPlayClient"),
                    MappingRegistry.getMethodNameFor("NetHandlerPlayClient.handleSpawnExperienceOrb"),
                    "(L" + MappingRegistry.getClassNameFor("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb") + ";)V",
                    ""
            ));
        }
    }


}
