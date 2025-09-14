package net.juniorwmg.opensmashbats.objects.item;

import net.juniorwmg.opensmashbats.Main;
import net.juniorwmg.opensmashbats.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.juniorwmg.opensmashbats.util.interfaces.IHasModel;
import net.minecraft.item.Item;

public class BaseItem extends Item implements IHasModel {
    public BaseItem(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.COMBAT);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerModel(this, 0, "inventory");
    }
}
