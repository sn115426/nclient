package com.ngames.nclient;

import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerJoinWorldEvent;
import com.ngames.nclient.gui.Gui;
import com.ngames.nclient.gui.Hud;
import com.ngames.nclient.keybinds.KeyBinds;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.modules.combat.Criticals;
import com.ngames.nclient.module.modules.render.NoRender;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.ngames.nclient.NClient.MC;

public class EventsHandler {

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        Entity e = event.getEntity();
        if (e instanceof EntityPlayerSP) {
            FileUtil.loadAll();
            if (NClientEvent.callEvent(new PlayerJoinWorldEvent())) {
                event.setCanceled(true);
            }
        }
        if (Modules.noRender.isEnabled()) {
            NoRender nr = Modules.noRender;
            if (nr.item.getValue() && e instanceof EntityItem)
                e.onKillCommand();
            if (nr.entity.getValue() && e instanceof EntityLiving)
                e.onKillCommand();
            if (nr.other.getValue() && !(e instanceof EntityOtherPlayerMP) && !(e instanceof EntityLiving) && !(e instanceof EntityItem))
                e.onKillCommand();
        }
    }

    @SubscribeEvent
    public void onCommand(ClientChatEvent event) {
        if (event.getMessage().startsWith(NClient.commandPrefix)) {
            NClient.commandManager.parse(event.getMessage());
            event.setCanceled(true);
            return;
        }

        if (Modules.chatPrefix.isEnabled()) {
            event.setMessage(Modules.chatPrefix.prefix.getValue() + event.getMessage());
        }
        if (Modules.chatPostfix.isEnabled() && !event.getMessage().startsWith("/") && !event.getMessage().startsWith("#")) {
            event.setMessage(event.getMessage() + Modules.chatPostfix.postfix.getValue());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        NClient.ticks++;
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (MC.world != null && !(MC.currentScreen instanceof GuiChat) && !Keyboard.getEventKeyState()) {
            if (Gui.justPressed) {
                if (Keyboard.getEventKey() != KeyBinds.getModuleKeyBind("ClickGUI")) {
                    KeyBinds.getBind(Keyboard.getEventKey());
                }
                Gui.justPressed = false;
            } else {
                KeyBinds.getBind(Keyboard.getEventKey());
            }
        }
    }

    @SubscribeEvent
    public void onCritical(CriticalHitEvent event) {
        if (Modules.criticals.isEnabled() && Modules.criticals.type.getValue() == 0)
            event.setResult(Result.ALLOW);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            if (Modules.hud.isEnabled()) {
                Hud.drawHUD();
            }
            if (NClient.gui != null && Modules.noRender.isEnabled()) {
                NClient.gui.drawScreen(Gui.mouseX, Gui.mouseY, MC.getRenderPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre<?> event) {
    }

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
            NClient.c.add(NClient.t);
            Modules.cpsCount.clicks++;
            NClient.isLeftPressed = false;
        } else if (Mouse.getEventButton() == 0)
            NClient.isLeftPressed = true;
        if (Mouse.getEventButtonState() && (MC.gameSettings.keyBindUseItem.isKeyDown() || (MC.gameSettings.keyBindAttack.isKeyDown() && MC.objectMouseOver.typeOfHit != Type.MISS))
                && (Baritone.isInMainHand(Items.ENDER_PEARL) || Baritone.isInMainHand(Items.BOW) || Baritone.isInMainHand(Items.SNOWBALL)) && Baritone.overrideRotation) {
            Baritone.prevYaw = Baritone.yaw;
            Baritone.prevPitch = Baritone.pitch;
            Baritone.yaw = MC.player.rotationYaw;
            Baritone.pitch = MC.player.rotationPitch;
            Baritone.needRotate = true;

        }
        if (!Mouse.getEventButtonState() && (!MC.gameSettings.keyBindUseItem.isKeyDown() ||
                !(Baritone.isInMainHand(Items.ENDER_PEARL) || Baritone.isInMainHand(Items.BOW) || Baritone.isInMainHand(Items.SNOWBALL))) &&
                (!MC.gameSettings.keyBindAttack.isKeyDown() || MC.objectMouseOver.typeOfHit == Type.MISS)
                && Baritone.needRotate && Baritone.overrideRotation) {
            Baritone.yaw = Baritone.prevYaw;
            Baritone.pitch = Baritone.prevPitch;
            Baritone.needRotate = false;
        }
    }

    @SubscribeEvent
    public void onPvP(AttackEntityEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            NClient.inPvP = true;
            new Thread(() -> {
                BUtils.sleep(5000);
                NClient.inPvP = false;
            }).start();
            if (!Modules.autoEz.targeted.contains(event.getTarget().getEntityId()))
                Modules.autoEz.targeted.add(event.getTarget().getEntityId());
            event.setCanceled(Criticals.miniJump(event.getTarget()));
        }
    }

    @SubscribeEvent
    public void onHurt(LivingAttackEvent event) {
        if ((event.getEntity() instanceof EntityPlayerSP)) {
            NClient.inPvP = true;
            new Thread(() -> {
                BUtils.sleep(5000);
                NClient.inPvP = false;
            }).start();
        }
    }

    @SubscribeEvent
    public void onBackgroundDrawn(BackgroundDrawnEvent event) {
        if (event.getGui() instanceof GuiIngameMenu || MC.world != null)
            return;
        Gui.drawBackground();
    }
}
