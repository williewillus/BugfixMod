package williewillus.BugfixMod;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 3/15/14.
 */

//This class is unused until I figure out how to implement submods, or load this in a dummy container
public class LinkCommand implements ICommand {
    private List aliases;

    public LinkCommand() {
        this.aliases = new ArrayList();
        this.aliases.add("clickable");
        this.aliases.add("linkme");
    }

    @Override
    public String getCommandName() {
        return "link";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "link <destination>";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 1 || args.length == 0) {
            throw new WrongUsageException("/link <destination>", new Object[0]);
        } else {
            MinecraftServer.getServer().getCommandManager().executeCommand(sender, "tellraw @a {\"text\":\"\",\"extra\":[{\"text\":\"" + "Clickable: " + args[0] + "\",\"color\":\"white\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + args[0] + "\"},\"underlined\":\"true\"}]}");
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
