package williewillus.WillieTweaks.common.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * Created by Vincent on 3/10/14.
 */
public class ASMFMLLoadingPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public String[] getASMTransformerClass() {
        return new String[]{ASMClassTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return WillieTweaksASM.class.getName();
    }

    public String getSetupClass() {
        return ASMFMLLoadingPlugin.class.getName();
    }

    public void injectData(Map<String, Object> data) {
        ASMClassTransformer.instance.settingsFile = new File(((File) data.get("mcLocation")).getPath() + "/config/WillieTweaks.cfg");
        ASMClassTransformer.instance.initialize((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public Void call() {
        return null;
    }
}
