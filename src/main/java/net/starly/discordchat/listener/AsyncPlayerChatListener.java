package net.starly.discordchat.listener;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.starly.discordchat.context.MessageContent;
import net.starly.discordchat.context.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        MessageContent config = MessageContent.getInstance();
        String url = config.getMessage(MessageType.BOT, "webhook.url").orElse(null);
        if (url == null) {
            System.out.println("§e 웹훅 URL이 연결되지 않았습니다.");
            return;
        }
        WebhookClient client = new WebhookClientBuilder(url).build();

        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        UUID uniqueId = player.getUniqueId();
        String message = event.getMessage();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(displayName)
                .setAvatarUrl(config.getMessage(MessageType.BOT, "webhook.avatarUrl").orElse(null) + uniqueId)
                .setContent(message);
        client.send(builder.build());
    }
}