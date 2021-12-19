package io.github.ashindigo.mail.container;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class MailBoxContainer implements Container {

    private final int SIZE = 27;
    private final NonNullList<ItemStack> items;

    public MailBoxContainer() {
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> list = this.items.stream().filter(arg -> !arg.isEmpty()).collect(Collectors.toList());
        this.clearContent();
        return list;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack lv = ContainerHelper.removeItem(this.items, i, j);
        if (!lv.isEmpty()) {
            this.setChanged();
        }

        return lv;
    }

    public ItemStack removeItemType(Item arg, int i) {
        ItemStack lv = new ItemStack(arg, 0);

        for (int j = this.SIZE - 1; j >= 0; --j) {
            ItemStack lv2 = this.getItem(j);
            if (lv2.getItem().equals(arg)) {
                int k = i - lv.getCount();
                ItemStack lv3 = lv2.split(k);
                lv.grow(lv3.getCount());
                if (lv.getCount() == i) {
                    break;
                }
            }
        }

        if (!lv.isEmpty()) {
            this.setChanged();
        }

        return lv;
    }

    public ItemStack addItem(ItemStack arg) {
        ItemStack lv = arg.copy();
        this.moveItemToOccupiedSlotsWithSameType(lv);
        if (lv.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.moveItemToEmptySlots(lv);
            return lv.isEmpty() ? ItemStack.EMPTY : lv;
        }
    }

    public boolean canAddItem(ItemStack arg) {
        boolean bl = false;

        for (ItemStack lv : this.items) {
            if (lv.isEmpty() || ItemStack.isSameItemSameTags(lv, arg) && lv.getCount() < lv.getMaxStackSize()) {
                bl = true;
                break;
            }
        }

        return bl;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack lv = this.items.get(i);
        if (lv.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(i, ItemStack.EMPTY);
            return lv;
        }
    }

    @Override
    public void setItem(int i, ItemStack arg) {
        this.items.set(i, arg);
        if (!arg.isEmpty() && arg.getCount() > this.getMaxStackSize()) {
            arg.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return this.SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack lv : this.items) {
            if (!lv.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player arg) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    public String toString() {
        return this.items.stream().filter(arg -> !arg.isEmpty()).toList().toString();
    }

    private void moveItemToEmptySlots(ItemStack arg) {
        for (int i = 0; i < this.SIZE; ++i) {
            ItemStack lv = this.getItem(i);
            if (lv.isEmpty()) {
                this.setItem(i, arg.copy());
                arg.setCount(0);
                return;
            }
        }

    }

    private void moveItemToOccupiedSlotsWithSameType(ItemStack arg) {
        for (int i = 0; i < this.SIZE; ++i) {
            ItemStack lv = this.getItem(i);
            if (ItemStack.isSameItemSameTags(lv, arg)) {
                this.moveItemsBetweenStacks(arg, lv);
                if (arg.isEmpty()) {
                    return;
                }
            }
        }

    }

    private void moveItemsBetweenStacks(ItemStack arg, ItemStack arg2) {
        int i = Math.min(this.getMaxStackSize(), arg2.getMaxStackSize());
        int j = Math.min(arg.getCount(), i - arg2.getCount());
        if (j > 0) {
            arg2.grow(j);
            arg.shrink(j);
            this.setChanged();
        }

    }

    public void fromTag(CompoundTag tag) {
        fromTag(tag.getList("inv", 0));
    }

    public void fromTag(ListTag arg) {
        for (int i = 0; i < arg.size(); ++i) {
            ItemStack lv = ItemStack.of(arg.getCompound(i));
            if (!lv.isEmpty()) {
                this.addItem(lv);
            }
        }
    }

    public CompoundTag createCompoundTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("inv", createTag());
        return tag;
    }

    public ListTag createTag() {
        ListTag lv = new ListTag();
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack lv2 = this.getItem(i);
            if (!lv2.isEmpty()) {
                lv.add(lv2.save(new CompoundTag()));
            }
        }
        return lv;
    }
}
