package williewillus.BugfixMod;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * Created by Vincent on 3/10/14.
 */
public class BugfixModFMLLoadingPlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public String[] getASMTransformerClass() {
        return new String[]{BugfixModClassTransformer.class.getName()};
    }

    public String getSetupClass() {
        return BugfixModFMLLoadingPlugin.class.getName();
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public String getModContainerClass() {
        return BugfixModDummyContainer.class.getName();
    }


    public void injectData(Map<String, Object> data) {
        BugfixModClassTransformer.instance.settingsFile = new File(((File) data.get("mcLocation")).getPath() + "/config/BugfixMod.cfg");
        BugfixModClassTransformer.instance.initialize((Boolean) data.get("runtimeDeobfuscationEnabled"));

    }

    public Void call() {
        return null;
    }
}
