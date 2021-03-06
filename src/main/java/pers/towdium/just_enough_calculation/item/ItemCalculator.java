package pers.towdium.just_enough_calculation.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import pers.towdium.just_enough_calculation.JustEnoughCalculation;
import pers.towdium.just_enough_calculation.network.packets.PacketSyncCalculator;
import pers.towdium.just_enough_calculation.util.Utilities;
import pers.towdium.just_enough_calculation.util.exception.IllegalPositionException;

import javax.annotation.Nonnull;

/**
 * @author Towdium
 */
public class ItemCalculator extends Item {

    public ItemCalculator() {
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) { // client
            if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))){
                itemStackIn = new ItemStack(itemStackIn.getItem(), itemStackIn.getCount(), Utilities.circulate(itemStackIn.getMetadata(), 2, true), itemStackIn.getTagCompound());
                JustEnoughCalculation.networkWrapper.sendToServer(new PacketSyncCalculator(itemStackIn));
            } else {
                playerIn.openGui(JustEnoughCalculation.instance, itemStackIn.getMetadata(), worldIn, 0, 0, 0);
            }
        } else {
            playerIn.openGui(JustEnoughCalculation.instance, itemStackIn.getMetadata(), worldIn, 0, 0, 0);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        switch (stack.getMetadata()) {
            case 0: return "item.itemCalculator";
            case 1: return "item.itemMathCalculator";
            default: throw new IllegalPositionException();
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }
}