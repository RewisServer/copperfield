package dev.volix.rewinside.odyssey.common.copperfield.example;

/**
 * @author Benedikt Wüller
 */
public enum PartyEventType {

    PARTY_CREATED_EVENT(PartyCreatedEvent.class);

    public final Class<? extends PartyEvent> type;

    PartyEventType(final Class<? extends PartyEvent> type) {
        this.type = type;
    }
}
