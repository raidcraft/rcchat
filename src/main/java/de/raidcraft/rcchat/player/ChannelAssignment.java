package de.raidcraft.rcchat.player;

/**
 * @author Philip
 */
public class ChannelAssignment {

    private String channel;
    private AssignmentType type;

    public ChannelAssignment(String channel, String type) {

        this.channel = channel;
        this.type = AssignmentType.valueOf(type);
    }

    public String getChannel() {

        return channel;
    }

    public void setChannel(String channel) {

        this.channel = channel;
    }

    public AssignmentType getType() {

        return type;
    }

    public void setType(AssignmentType type) {

        this.type = type;
    }
}
