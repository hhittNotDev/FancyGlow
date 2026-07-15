package hhitt.fancyglow.managers;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.bukkit.platform.LiteAdventurePlatformExtension;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import hhitt.fancyglow.FancyGlow;
import hhitt.fancyglow.commands.ColorCommand;
import hhitt.fancyglow.commands.DisableCommand;
import hhitt.fancyglow.commands.MainCommand;
import hhitt.fancyglow.commands.SetCommand;
import hhitt.fancyglow.commands.handler.AvailableColorMode;
import hhitt.fancyglow.commands.handler.FancyInvalidUsageHandler;
import hhitt.fancyglow.commands.handler.FancyMissingPermissionHandler;
import hhitt.fancyglow.utils.MessageHandler;
import hhitt.fancyglow.utils.Messages;
import org.bukkit.command.CommandSender;

public final class CommandLoader {

    private final MessageHandler messageHandler;
    private final LiteCommands<CommandSender> liteCommands;

    public CommandLoader(FancyGlow plugin) {
        this.messageHandler = plugin.getMessageHandler();

        this.liteCommands = LiteBukkitFactory.builder()
                .settings(settings -> settings
                        .fallbackPrefix("yasm")
                        .nativePermissions(false)
                )

                .commands(
                        new MainCommand(plugin),
                        new ColorCommand(plugin),
                        new SetCommand(plugin),
                        new DisableCommand(plugin))

                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> messageHandler.sendMessageBuilder(null, Messages.UNKNOWN_TARGET)
                        .placeholder("%player_name%", input)
                        .parse())
                .message(LiteBukkitMessages.PLAYER_ONLY, "Only players can run this command.")

                .invalidUsage(new FancyInvalidUsageHandler(messageHandler))
                .missingPermission(new FancyMissingPermissionHandler(messageHandler))

                .argument(String.class, ArgumentKey.of("available-colors"), new AvailableColorMode(plugin))
                .extension(new LiteAdventurePlatformExtension<>(plugin.adventure()), config -> config
                        .miniMessage(true)
                        .colorizeArgument(false)
                )

                .build();
    }

    public void unregisterAll() {
        liteCommands.unregister();
    }
}
