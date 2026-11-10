package org.mod.industrialtech.common.entity;

import net.minecraftforge.items.ItemStackHandler;

/** Общий интерфейс машин: тик на сервере и доступ к слотам (для дропа при разрушении). */
public interface MachineBE {
    void serverTick();

    ItemStackHandler items();
}
