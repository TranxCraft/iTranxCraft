package com.wickedgaminguk.tranxcraft.player;

public enum Rank {
    UNKNOWN(0), PLAYER(1), MODERATOR(2), ADMIN(3), LEADADMIN(4), EXECUTIVE(5), COMMANDER(6);

    private int rank;

    Rank(int rank) {
        this.rank = rank;
    }

    public int getRankLevel() {
        return this.rank;
    }
}
