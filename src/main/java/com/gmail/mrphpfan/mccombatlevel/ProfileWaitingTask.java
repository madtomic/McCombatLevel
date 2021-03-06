package com.gmail.mrphpfan.mccombatlevel;

import com.gmail.nossr50.util.player.UserManager;

import java.lang.ref.WeakReference;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProfileWaitingTask implements Runnable {

    private static final int MAX_TRIES = 5;

    private final WeakReference<Player> weakPlayer;
    private final McCombatLevel plugin;

    private int attempt;

    public ProfileWaitingTask(McCombatLevel plugin, Player player) {
        this.plugin = plugin;

        this.weakPlayer = new WeakReference<Player>(player);
    }

    @Override
    public void run() {
        Player player = weakPlayer.get();
        if (player != null && player.isOnline()) {
            if (UserManager.hasPlayerDataKey(player)) {
                //profiles are loaded async. We need to wait for it
                plugin.updateLevel(player);
            } else {
                attempt++;
                if (attempt <= MAX_TRIES) {
                    //max tries - last try would test after 5 * 5 * 20 => 500 ticks or 25 seconds
                    Bukkit.getScheduler().runTaskLater(plugin, this, attempt * 5 * 20L);
                }
            }
        }
    }
}
