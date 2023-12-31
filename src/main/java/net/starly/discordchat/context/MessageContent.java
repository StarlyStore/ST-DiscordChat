package net.starly.discordchat.context;

import net.starly.core.jb.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageContent {

    private static MessageContent instance;
    private Map<MessageType, Map<String, String>> map = new HashMap<>();

    private MessageContent() {}

    public static MessageContent getInstance() {
        if (instance == null) instance = new MessageContent();
        return instance;
    }

    public void initialize(FileConfiguration file) {
        map.clear();
        Arrays.stream(MessageType.values()).map(values -> new Pair<>(values, file.getConfigurationSection(values.getKey())))
                .forEach(this::initializeMessages);
    }

    private void initializeMessages(Pair<MessageType, ConfigurationSection> pair) {
        Map<String, String> messages = map.computeIfAbsent(pair.getFirst(), (unused) -> new HashMap<>());
        pair.getSecond().getKeys(true).forEach(key ->
                messages.put(key, ChatColor.translateAlternateColorCodes('&', pair.getSecond().getString(key))));
    }

    public Optional<String> getMessage(MessageType type, String key) {
        return Optional.ofNullable(map.get(type).get(key));
    }

    public Optional<String> getMessageAfterPrefix(MessageType type, String key) {
        String prefix = map.get(MessageType.NORMAL).get("prefix");
        String message = getMessage(type, key).orElse(null);
        return message == null ? Optional.empty() : Optional.of((prefix == null ? "" : prefix) + message);
    }
}