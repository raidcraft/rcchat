package de.raidcraft.rcchat.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Philip on 17.01.2016.
 */
@Getter
@Setter
@Entity
@Table(name = "rc_chat_mutes")
public class TMute {

    @Id
    private int id;
    private UUID playerId;
    private UUID mutedPlayer;
    private Date created;
}
