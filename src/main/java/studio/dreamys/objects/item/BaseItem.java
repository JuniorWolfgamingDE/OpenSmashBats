package studio.dreamys.objects.item;

import net.minecraft.creativetab.CreativeTabs;
import studio.dreamys.ExampleMod;
import studio.dreamys.init.ItemInit;
import studio.dreamys.util.interfaces.IHasModel;
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
        ExampleMod.proxy.registerModel(this, 0, "inventory");
    }
}
