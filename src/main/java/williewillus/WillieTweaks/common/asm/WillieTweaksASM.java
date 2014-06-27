package williewillus.WillieTweaks.common.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import williewillus.WillieTweaks.common.Reference;

import java.util.Arrays;

/**
 * Created by Vincent on 3/10/14.
 */


public class WillieTweaksASM extends DummyModContainer {

    public WillieTweaksASM() {
        super(new ModMetadata());
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList("williewillus");
        myMeta.description = "Combination of all my other bugfix mods.";
        myMeta.modId = Reference.ASMMODID;
        myMeta.version = Reference.VERSION;
        myMeta.name = Reference.ASMMODID;
        myMeta.url = Reference.REPO;
		System.out.println("BUGFIXMOD VERSION " + myMeta.version + " LOADING.");
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }


}
