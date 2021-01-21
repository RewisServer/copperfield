package dev.volix.rewinside.odyssey.common.copperfield.example;

import dev.volix.rewinside.odyssey.common.copperfield.annotation.CopperField;
import java.util.UUID;

/**
 * @author Benedikt Wüller
 */
public class PartyCreatedEvent implements PartyEvent {

    @CopperField
    public UUID byUuid;

}
