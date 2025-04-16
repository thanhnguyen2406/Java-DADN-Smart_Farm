package dadn_SmartFarm.model.enums;

public enum Status {
    ACTIVE,
    INACTIVE;

    public Status opposite() {
        return this == ACTIVE ? INACTIVE : ACTIVE;
    }
}

