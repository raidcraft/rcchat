package de.raidcraft.rcchat.player;

import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class ChatPlayer {

    Player player;

    public ChatPlayer(Player player) {

        this.player = player;
    }

    public Player getPlayer() {

        return player;
    }

    public boolean isMuted() {

        //TODO isMuted
        return false;
    }

}
