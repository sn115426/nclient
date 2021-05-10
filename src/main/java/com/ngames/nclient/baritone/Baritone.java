package com.ngames.nclient.baritone;

import static com.ngames.nclient.NClient.MC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Modules;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

public class Baritone {

    //slot id
    public static final int CRAFTING_1 = 0;
    public static final int CRAFTING_2 = 1;
    public static final int CRAFTING_3 = 2;
    public static final int CRAFTING_4 = 3;
    public static final int ARMOR_HELMET = 4;
    public static final int ARMOR_CHESTPLACE = 5;
    public static final int ARMOR_LEGGINS = 6;
    public static final int ARMOR_BOOTS = 7;
    public static final int INVENTORY_1 = 8;
    public static final int INVENTORY_2 = 9;
    public static final int INVENTORY_3 = 10;
    public static final int INVENTORY_4 = 11;
    public static final int INVENTORY_5 = 12;
    public static final int INVENTORY_6 = 13;
    public static final int INVENTORY_7 = 14;
    public static final int INVENTORY_8 = 15;
    public static final int INVENTORY_9 = 16;
    public static final int INVENTORY_10 = 17;
    public static final int INVENTORY_11 = 18;
    public static final int INVENTORY_12 = 19;
    public static final int INVENTORY_13 = 20;
    public static final int INVENTORY_14 = 21;
    public static final int INVENTORY_15 = 22;
    public static final int INVENTORY_16 = 23;
    public static final int INVENTORY_17 = 24;
    public static final int INVENTORY_18 = 25;
    public static final int INVENTORY_19 = 26;
    public static final int INVENTORY_20 = 27;
    public static final int INVENTORY_21 = 28;
    public static final int INVENTORY_22 = 29;
    public static final int INVENTORY_23 = 30;
    public static final int INVENTORY_24 = 31;
    public static final int INVENTORY_25 = 32;
    public static final int INVENTORY_26 = 33;
    public static final int INVENTORY_27 = 34;
    public static final int HOTBAR_1 = 35;
    public static final int HOTBAR_2 = 36;
    public static final int HOTBAR_3 = 37;
    public static final int HOTBAR_4 = 38;
    public static final int HOTBAR_5 = 39;
    public static final int HOTBAR_6 = 40;
    public static final int HOTBAR_7 = 41;
    public static final int HOTBAR_8 = 42;
    public static final int HOTBAR_9 = 43;
    public static final int OFFHAND = 45;

    //window id
    public static final int INVENTORY = 0;
    public static final int CHEST = 1;

    public static boolean clickSync = false;
    public static float yaw;
    public static float pitch;
    public static float prevYaw;
    public static float prevPitch;
    public static boolean overrideRotation;
    public static boolean serverSprinting;
    public static boolean serverSprintingState;
    public static boolean overrideSprinting;
    public static boolean needRotate;
    private static short rotationPriority;

    public static void rightClickMouse() {
        try {
            Method m = Minecraft.class.getDeclaredMethod("func_147121_ag");
            m.setAccessible(true);
            m.invoke(MC);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
    }

    public static void leftClickMouse() {
        try {
            Method m = Minecraft.class.getDeclaredMethod("func_147116_af");
            m.setAccessible(true);
            m.invoke(MC);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Modules.cpsCount.clicks++;
    }

    public static void processKeyF3(int auxKey) {
        try {
            Method m = Minecraft.class.getDeclaredMethod("func_184122_c", int.class);
            m.setAccessible(true);
            m.invoke(MC, auxKey);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
    }

    public static void attackEntity(Entity target) {
        if (target != null && MC.player != null) {
            MC.playerController.attackEntity(MC.player, target);
            MC.player.swingArm(EnumHand.MAIN_HAND);
        }
        Modules.cpsCount.clicks++;
    }

    public static void attackEntity() {
        if (MC.player != null && MC.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            MC.playerController.attackEntity(MC.player, MC.objectMouseOver.entityHit);
            MC.player.swingArm(EnumHand.MAIN_HAND);
        }
        Modules.cpsCount.clicks++;
    }

    public static void interactWithEntity(Entity target) {
        MC.playerController.interactWithEntity(MC.player, target, EnumHand.MAIN_HAND);
    }

    public static void rightClickBlock(BlockPos block, EnumHand hand) {
        MC.playerController.processRightClickBlock(MC.player, MC.world, block, EnumFacing.getDirectionFromEntityLiving(block, MC.player),
                new Vec3d(block.getX(), block.getY(), block.getZ()), hand);
    }

    public static int getLeftClickCounter() {
        Field f = null;
        try {
            f = Minecraft.class.getDeclaredField("field_71429_W");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        int ret = 10;
        try {
            ret = f.getInt(MC);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void setLeftClickCounter() {
        Field f = null;
        try {
            f = Minecraft.class.getDeclaredField("field_71429_W");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        try {
            f.setInt(MC, 10);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setTimer(Timer timer) {
        Field f = null;
        try {
            f = Minecraft.class.getDeclaredField("field_71428_T");
            f.setAccessible(true);
            f.set(MC, timer);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setSPRINTING_SPEED_BOOST(AttributeModifier SPRINTING_SPEED_BOOST) {
        Field f = null;
        try {
            f = EntityLivingBase.class.getDeclaredField("field_110157_c");
            f.setAccessible(true);
            f.set(MC.player, SPRINTING_SPEED_BOOST);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static int getJumpTicks() {
        Field f = null;
        try {
            f = EntityLivingBase.class.getDeclaredField("field_70773_bE");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        int ret = -1;
        try {
            ret = f.getInt(MC.player);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean getServerSprintState() {
        Field f = null;
        try {
            f = EntityPlayerSP.class.getDeclaredField("field_175171_bO");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        boolean ret = false;
        try {
            ret = f.getBoolean(MC.player);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static Set<ChunkPos> getVisibleChunks() {
        Field f = null;
        try {
            f = WorldClient.class.getDeclaredField("field_184157_a");
            f.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        Set<ChunkPos> ret = null;
        try {
            ret = (Set<ChunkPos>) f.get(MC.world);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static FoodStats getFoodStats() {
        return MC.player.getFoodStats();
    }

    public static void dropItem(int slotId) {

        MC.playerController.windowClick(MC.player.openContainer.windowId, slotId, 1, ClickType.THROW, MC.player);
    }

    public static void dropCurrentItem() {
        MC.player.dropItem(true);
    }

    public static void addRotationPlayer(float yaw, float pitch) {
        rotatePlayer(MC.player.rotationYaw + yaw, MC.player.rotationPitch + pitch);
    }

    public static void dropAllItems(float minDelay, float maxDelay) {
        dropAllItems((int) minDelay, (int) maxDelay);
    }

    public static void dropAllItems() {
        List<Integer> id = new ArrayList<>();
        for (Slot slot : MC.player.openContainer.inventorySlots) {
            id.add(slot.getSlotIndex());
        }
        for (int cid : id) {
            dropItem(cid);
        }
    }

    public static void dropAllItems(int minDelay, int maxDelay) {
        List<Integer> id = new ArrayList<>();
        for (Slot slot : MC.player.openContainer.inventorySlots) {
            id.add(slot.getSlotIndex());
        }
        for (int cid : id) {
            BUtils.sleep(BUtils.randomInRange(minDelay, maxDelay), BUtils.randomInRange(0, 999999));
            dropItem(cid);
        }
    }

    public static void closeInventory() {
        MC.player.openContainer.onContainerClosed(MC.player);
        MC.displayGuiScreen((GuiScreen) null);
        if (MC.currentScreen == null) {
            MC.setIngameFocus();
        }
    }

    public static void rotatePlayer(float yaw, float pitch) {
        MC.player.rotationYaw = yaw;
        MC.player.rotationPitch = pitch;
    }

    public static void rotatePlayer(float yaw, float pitch, boolean onlyServer) {
        if (onlyServer) {
            CPacketPlayer pc = new CPacketPlayer();
            new Rotation(yaw, pitch, true);
            MC.getConnection().sendPacket(pc);
        } else
            rotatePlayer(yaw, pitch);
    }

    public static void rotateHead(float yaw) {
        MC.player.rotationYawHead = yaw;
    }

    public static void addRotationPlayer(float yaw, float pitch, boolean onlyServer) {
        Baritone.yaw += yaw;
        Baritone.pitch += pitch;
        if (onlyServer)
            Baritone.updateRotation();
        else
            addRotationPlayer(yaw, pitch);
    }

    public static void addRotationHead(float yaw) {
        rotateHead(MC.player.rotationYawHead + yaw);
    }

    public static void setRotationToEntity(Entity entity) {
        setRotationToEntity(entity, 0.5f);
    }

    public static void setRotationToEntity(Entity entity, boolean onlyServer) {
        setRotationToEntity(entity, 0.5f, onlyServer);
    }

    public static void setRotationToEntity(Entity entity, float dPitch) {
        setRotationToEntity(entity, dPitch, false);
    }

    public static void setRotationToEntity(Entity entity, float dPitch, boolean onlyServer) {
        BUtils.EntityLookHelper elh = new BUtils.EntityLookHelper(MC.player);
        elh.setLookPositionWithEntity(entity, 360, 360);
        elh.onUpdateLook(dPitch, onlyServer);
    }

    public static void setRotationToBlockPos(BlockPos pos) {
        BUtils.EntityLookHelper elh = new BUtils.EntityLookHelper(MC.player);
        elh.setLookPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 360, 360);
        elh.onUpdateLook(0f);
    }

    public static void refreshRotation() {
        Baritone.yaw = MC.player.rotationYaw;
        Baritone.pitch = MC.player.rotationPitch;
    }

    public static void smoothRotatePlayer(float yaw, float pitch, long millis, boolean dirYaw, boolean dirPitch) {
        float aYaw = 0;
        float aPitch = 0;
        while (MC.player.rotationYaw != yaw || MC.player.rotationPitch != pitch) {
            if (MC.player.rotationYaw != yaw && dirYaw)
                aYaw = 0.01f;
            if (MC.player.rotationPitch != pitch && dirPitch)
                aPitch = 0.01f;
            if (MC.player.rotationYaw != yaw && !dirYaw)
                aYaw = -0.01f;
            if (MC.player.rotationPitch != pitch && !dirPitch)
                aPitch = -0.01f;
            if ((MC.player.rotationYaw > yaw && dirYaw) || (MC.player.rotationPitch > pitch && dirPitch) ||
                    (MC.player.rotationYaw < yaw && !dirYaw) || (MC.player.rotationPitch < pitch && !dirPitch))
                break;
            addRotationPlayer(aYaw, aPitch);
            BUtils.sleep((long) (millis / 0.01f));
        }
    }

    public static void smoothRotateHead(float yaw, long millis) {
        double yawMs = yaw / millis;
        for (int i = 0; i < millis; i++) {
            addRotationHead((float) yawMs);
        }
        rotateHead(yaw);
    }

    public static List<BlockPos> findBlocks(Block block) {
        List<BlockPos> ret = new ArrayList<>();
        if (MC.world != null) {
            for (ChunkPos cp : getVisibleChunks()) {
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 256; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockPos pos = new BlockPos(cp.getXStart() + x, y, cp.getZStart() + z);
                            IBlockState blockState = MC.world.getBlockState(pos);
                            if (blockState.getBlock() == block)
                                ret.add(pos);
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static Entity findEntity(float range, Class<? extends Entity> type) {
        Entity ret = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : MC.world.getLoadedEntityList()) {
            double dis = MC.player.getDistanceToEntity(e);
            if (e.getClass() == type && (ret == null || dis < distance) && dis <= range) {
                ret = e;
                distance = dis;
            }
        }
        return ret;
    }

    public static Entity findEntity(float range, Class<? extends Entity> type, List<Entity> except) {
        Entity ret = null;
        double distance = Double.MAX_VALUE;
        for (Entity e : MC.world.getLoadedEntityList()) {
            boolean contains = false;
            for (Entity e1 : except) {
                if (e1.getEntityId() == e.getEntityId()) {
                    contains = true;
                    break;
                }
            }
            double dis = MC.player.getDistanceToEntity(e);
            if (e.getClass() == type && dis < distance && !contains && dis <= range) {
                distance = dis;
                ret = e;
            }
        }
        return ret;
    }

    public static void useItem(EnumHand hand) {
        MC.playerController.processRightClick(MC.player, MC.world, hand);
    }

    public static void useItem() {
        KeyBinding.setKeyBindState(MC.gameSettings.keyBindUseItem.getKeyCode(), true);
    }

    public static void usedItem() {
        KeyBinding.setKeyBindState(MC.gameSettings.keyBindUseItem.getKeyCode(), false);
        //MC.playerController.onStoppedUsingItem(MC.player);
    }

    public static void putInMainHand(int slotId) {
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, slotId, 0, ClickType.PICKUP_ALL, MC.player);
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, MC.player.inventory.currentItem + 36, 0, ClickType.SWAP, MC.player);
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, slotId, 0, ClickType.PICKUP_ALL, MC.player);
    }

    public static void putInOffHand(int slotId) {
        if (slotId < 9)
            slotId += 36;
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, slotId, 0, ClickType.PICKUP, MC.player);
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, OFFHAND, 0, ClickType.PICKUP, MC.player);
        MC.playerController.windowClick(NClient.MC.player.inventoryContainer.windowId, slotId, 0, ClickType.PICKUP, MC.player);
    }

    public static void putInAir(int slotId) {
        MC.playerController.windowClick(0, slotId, 1, ClickType.QUICK_MOVE, MC.player);
    }

    public static void setMainHand(int slotId) {
        MC.player.inventory.currentItem = slotId;
    }

    public static int getItem(ItemStack itemStack, boolean onlyHotbar) {
        int ret = -1;
        int i = 0;
        Item item = itemStack.getItem();
        for (ItemStack itemStack2 : MC.player.inventory.mainInventory) {
            Item item2 = itemStack2.getItem();
            if (item.equals(item2)) {
                if (onlyHotbar && i > 34 && i < 44)
                    ret = i;
                else if (!onlyHotbar)
                    ret = i;
            }
            i++;
        }
        return ret;
    }

    public static int getBestFood(boolean onlyHotbar) {
        int ret = 8;
        boolean isFood = false;
        int i = 0;
        for (ItemStack itemStack : MC.player.inventory.mainInventory) {
            Item item = itemStack.getItem();
            ItemStack itemStack2 = MC.player.inventory.mainInventory.get(ret);
            Item item2 = itemStack2.getItem();
            if (item instanceof ItemFood) {
                isFood = true;
                if (item2 instanceof ItemFood) {
                    if (((ItemFood) item).getHealAmount(itemStack) > ((ItemFood) item2).getHealAmount(itemStack2)) {
                        if (onlyHotbar && i > 34 && i < 44)
                            ret = i;
                        else if (!onlyHotbar)
                            ret = i;
                    }
                } else
                    ret = i;
            }
            i++;
        }
        if (isFood)
            return ret;
        else
            return -1;
    }

    public static int getFoodWithHeal(byte healLvl, boolean onlyHotbar) //return food with heal level that equal or close healLvl
    {
        int ret = 8;
        boolean isFood = false;
        int i = 0;
        byte oldDelta = 21;
        for (ItemStack itemStack : MC.player.inventory.mainInventory) {
            Item item = itemStack.getItem();
            ItemStack itemStack2 = MC.player.inventory.mainInventory.get(ret);
            Item item2 = itemStack2.getItem();
            if ((item instanceof ItemFood)) {
                isFood = true;
                if (item2 instanceof ItemFood) {
                    byte healAmount = (byte) ((ItemFood) item).getHealAmount(itemStack);
                    byte healAmount2 = (byte) ((ItemFood) item2).getHealAmount(itemStack2);
                    byte delta = (byte) (healAmount > healAmount2 ? healAmount - healAmount2 : healAmount2 - healAmount);
                    if (delta < oldDelta) {
                        if (onlyHotbar && i > 34 && i < 44)
                            ret = i;
                        else if (!onlyHotbar)
                            ret = i;
                        oldDelta = delta;
                    }
                } else
                    ret = i;
            }
            i++;
        }
        if (isFood)
            return ret;
        else
            return -1;
    }

    public static Entity getPriorityTarget(float range, boolean onlyPlayers, boolean onlyMobs, boolean onlyPrevTarget, @Nullable Entity prevTarget, byte priority) {
        Entity target = null;
        float distance = Float.MAX_VALUE;
        float hp = Float.MAX_VALUE;
        if (MC.world != null)
            for (int i = 0; i < MC.world.getLoadedEntityList().size(); i++) {
                Entity e = MC.world.getLoadedEntityList().get(i);
                if (MC.world != null && MC.player.getDistanceToEntity(e) <= range && isAlive(e))
                    if ((e instanceof EntityOtherPlayerMP && onlyPlayers) || (e instanceof EntityLiving && onlyMobs)) {
                        float health = e instanceof EntityOtherPlayerMP ? ((EntityOtherPlayerMP) e).getHealth() : ((EntityLiving) e).getHealth();
                        float dist = MC.player.getDistanceToEntity(e);
                        if (priority == 0 && health < hp) {
                            target = e;
                            hp = health;
                        }
                        if (priority == 1 && dist < distance) {
                            target = e;
                            distance = dist;
                        }
                        if (health == hp && dist < distance)
                            target = e;
                        if (dist < distance && health < hp)
                            target = e;

                    }
            }
        if (onlyPrevTarget && prevTarget != null && isAlive(prevTarget) && MC.player.getDistanceToEntity(prevTarget) < range)
            target = prevTarget;
        return target;
    }

    public static Entity getBotTarget(Entity target, boolean filterEntityType) {
        Entity entityBot = target;
        boolean equalsPos;
        boolean notPlayer;
        boolean biggestId;
        for (int i = 0; i < MC.world.getLoadedEntityList().size(); i++) {
            Entity e = MC.world.getLoadedEntityList().get(i);
            if (e != null && e != MC.player) {
                equalsPos = BUtils.isInRange(e.posX, entityBot.posX, 0.5f) && BUtils.isInRange(e.posY, entityBot.posY, 1.2f) && BUtils.isInRange(e.posZ, entityBot.posZ, 0.5f);
                notPlayer = e instanceof EntityLiving;
                biggestId = e.getEntityId() > entityBot.getEntityId();
                if (equalsPos && (notPlayer || !filterEntityType) && (biggestId || !notPlayer))
                    entityBot = e;
            }
        }
        return entityBot;
    }

    public static List<Entity> getMultiTargets(float range, boolean onlyPlayers, boolean onlyMobs) {
        List<Entity> targets = new ArrayList<>();
        for (int i = 0; i < MC.world.getLoadedEntityList().size(); i++) {
            Entity e = MC.world.getLoadedEntityList().get(i);
            if (MC.world != null && MC.player.getDistanceToEntity(e) <= range && isAlive(e))
                if ((e instanceof EntityOtherPlayerMP && onlyPlayers) || (e instanceof EntityLiving && onlyMobs)) {
                    targets.add(e);
                }
        }
        return targets;
    }

    public static boolean isAlive(Entity e) {
        return !e.isEntityAlive() || !(e instanceof EntityLivingBase) || ((EntityLivingBase) e).deathTime == 0;
    }

    public static void displayMessage(String message) {
        MC.player.sendMessage(new TextComponentString("\\u00A7d[NClient]\\u00A73 " + message));
    }

    public static int getSlotFor(Item item, boolean onlyHotbar) {
        InventoryPlayer inv = MC.player.inventory;
        int slot = -1;
        int i = 0;
        for (ItemStack itemStack : inv.mainInventory) {
            if (itemStack.getItem().getRegistryName().equals(item.getRegistryName())) {
                if (onlyHotbar && i < 9)
                    slot = i;
                else if (!onlyHotbar)
                    slot = i;
                return slot;
            }
            i++;
        }
        return slot;
    }

    public static boolean isInMainHand(Item item) {
        return MC.player.inventory.mainInventory.get(MC.player.inventory.currentItem).getItem().getRegistryName().equals(item.getRegistryName());
    }

    public static boolean isInOffHand(Item item) {
        return Item.getIdFromItem(MC.player.inventory.offHandInventory.get(0).getItem()) == Item.getIdFromItem(item);
    }

    public static boolean isEmptyInOffHand() {
        return MC.player.inventory.offHandInventory.get(0).isItemEqual(ItemStack.EMPTY);
    }

    public static boolean isEmpty(ItemStack item) {
        return item == null || item.isEmpty() || item.getItem() == Items.AIR;
    }

    public static boolean isMoving() {
        return NClient.MC.player.moveForward != 0 || NClient.MC.player.moveStrafing != 0;
    }

    //Will send the rotation update packet next tick
    public static void updateRotation() {
        Field yaw = null;
        Field pitch = null;
        try {
            yaw = EntityPlayerSP.class.getDeclaredField("field_175164_bL");
            pitch = EntityPlayerSP.class.getDeclaredField("field_175165_bM");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        yaw.setAccessible(true);
        pitch.setAccessible(true);
        try {
            yaw.set(MC.player, MC.player.rotationYaw - 1);
            pitch.set(MC.player, MC.player.rotationPitch - 1);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setSneaking(boolean sneak) {
        if (MC.player.isSneaking() != sneak)
            KeyBinding.setKeyBindState(MC.gameSettings.keyBindSneak.getKeyCode(), sneak);
    }

    public static void setJumping(boolean jump) {
        KeyBinding.setKeyBindState(MC.gameSettings.keyBindJump.getKeyCode(), jump);
    }

    public static void setOverrideSprinting(boolean state) {
        serverSprintingState = getServerSprintState();
        overrideSprinting = state;
    }

    public static boolean calcRotationPriority(short rotationPriorityOther) {
        if (rotationPriorityOther >= rotationPriority) {
            rotationPriority = rotationPriorityOther;
            return true;
        }
        return false;
    }

    public static void setRotationPriority(short rotationPriority) {
        Baritone.rotationPriority = rotationPriority;
    }
}
