package gregicadditions;

import gregicadditions.armor.ArmorLogicSuite;
import gregicadditions.armor.ArmorUtils;
import gregicadditions.armor.PowerlessJetpack;
import gregicadditions.input.Key;
import gregicadditions.input.Keybinds;
import gregicadditions.item.GAMetaItem;
import gregicadditions.item.GAMetaItems;
import gregicadditions.machines.multi.centralmonitor.MetaTileEntityCentralMonitor;
import gregicadditions.network.KeysPacket;
import gregicadditions.network.NetworkHandler;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import gregtech.api.items.armor.ArmorMetaItem;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GAEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRender(final TickEvent.RenderTickEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.inGameHasFocus && mc.world != null && !mc.gameSettings.showDebugInfo && Minecraft.isGuiEnabled()) {
            final ItemStack item = mc.player.inventory.armorItemInSlot(EntityEquipmentSlot.CHEST.getIndex());
            if (item.getItem() instanceof ArmorMetaItem) {
                ArmorMetaItem<?>.ArmorMetaValueItem armorMetaValue = ((ArmorMetaItem<?>) item.getItem()).getItem(item);
                if (armorMetaValue.getArmorLogic() instanceof ArmorLogicSuite) {
                    ArmorLogicSuite armorLogic = (ArmorLogicSuite) armorMetaValue.getArmorLogic();
                    if (armorLogic.isNeedDrawHUD()) {
                        armorLogic.drawHUD(item);
                    }
                } else if (armorMetaValue.getArmorLogic() instanceof PowerlessJetpack) {
                    PowerlessJetpack armorLogic = (PowerlessJetpack) armorMetaValue.getArmorLogic();
                    if (armorLogic.isNeedDrawHUD()) {
                        armorLogic.drawHUD(item);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onKeyInput(KeyInputEvent event) {
        if (ArmorUtils.SIDE.isClient()) {
            boolean needNewPacket = false;
            for (Key key : Keybinds.REGISTERY) {
                boolean keyState = key.getBind().isKeyDown();
                if (key.state != keyState) {
                    key.state = keyState;
                    needNewPacket = true;
                }
            }
            if (needNewPacket) NetworkHandler.INSTANCE.sendToServer(new KeysPacket(Keybinds.REGISTERY));
        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            ItemStack jet = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            final ItemStack NANO = GAMetaItems.NANO_MUSCLE_SUITE_BOOTS.getStackForm();
            final ItemStack QUARK = GAMetaItems.QUARK_TECH_SUITE_BOOTS.getStackForm();
            final ItemStack JET = GAMetaItems.IMPELLER_JETPACK.getStackForm();
            final ItemStack ADJET = GAMetaItems.ADVANCED_IMPELLER_JETPACK.getStackForm();
            final ItemStack FLUIDJET = GAMetaItems.SEMIFLUID_JETPACK.getStackForm();
            if(!(jet.getItem() instanceof GAMetaItem) && !(armor.getItem() instanceof GAMetaItem)){
                return;
            }

            if (!(jet.isItemEqual(JET) || jet.isItemEqual(ADJET) || (jet.isItemEqual(FLUIDJET)) || armor.isItemEqual(QUARK) || armor.isItemEqual(NANO))) {
                return;
            }
            if (jet.isItemEqual(FLUIDJET)) {
                event.setCanceled(true);
            } else {
                ItemStack armorPiece = jet.isEmpty() ? armor : jet;

                ArmorMetaItem<?>.ArmorMetaValueItem armorMetaValue = ((ArmorMetaItem<?>) armorPiece.getItem()).getItem(armorPiece);
                ArmorLogicSuite armorLogic = (ArmorLogicSuite) armorMetaValue.getArmorLogic();
                IElectricItem item = armorPiece.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
                if (item == null) return;
                int energyCost = (armorLogic.getEnergyPerUse() * Math.round(event.getDistance()));
                if (item.getCharge() >= energyCost) {
                    item.discharge(energyCost, item.getTier(), true, false, false);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onGUIEvent(GuiOpenEvent event) {
        boolean resync = false;
        for (Key current : Keybinds.REGISTERY) {
            if (current.state = true) {
                current.state = false;
                resync = true;
            }
        }
        if (resync) NetworkHandler.INSTANCE.sendToServer(new KeysPacket(Keybinds.REGISTERY));
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getTileEntity(event.getPos()) instanceof MetaTileEntityHolder) {
            event.setUseBlock(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntityPlayer().isCreative()) {
            TileEntity holder = event.getWorld().getTileEntity(event.getPos());
            if (holder instanceof MetaTileEntityHolder && ((MetaTileEntityHolder) holder).getMetaTileEntity() instanceof MetaTileEntityCentralMonitor) {
                ((MetaTileEntityCentralMonitor) ((MetaTileEntityHolder) holder).getMetaTileEntity()).invalidateStructure();
            }
        }
    }
}
