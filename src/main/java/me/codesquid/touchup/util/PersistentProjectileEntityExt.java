package me.codesquid.touchup.util;

public interface PersistentProjectileEntityExt {
    boolean inGround();

    boolean hadFireTrail();

    void setHadFireTrail(boolean value);
}
