package com.ngames.nclient.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleUtils;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.settings.Setting;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingString;
import com.ngames.nclient.module.settings.SettingValue;
import com.ngames.nclient.module.settings.Settings;
import com.ngames.nclient.keybinds.KeyBinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeHell;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import static com.ngames.nclient.NClient.MC;

public class Gui extends GuiScreen {

    public static int mouseX;
    public static int mouseY;
    public static boolean justPressed;
    public Button selButton;
    public Button mouseOverButton;
    public Setting listining;
    public boolean listen;
    public String cache = "";

    public final GuiScreen lastScreen;
    public static Category currentCategory = Category.ALL;
    Framebuffer framebuffer;
    List<Button> buttonList = new ArrayList<>();
    int ButtonID = 8;
    List<List<Object>> buttonCoords = new ArrayList<>();
    boolean sync = false;
    boolean wait = true;

    public Gui(GuiScreen lastScreen) {
        this.lastScreen = lastScreen;
        framebuffer = new Framebuffer(MC.displayWidth, MC.displayHeight, false);
        addButtons();
        glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Modules.clickGUI.isEnabled()) {
            calculateMouse();
            GlStateManager.color(1, 1, 1);
            setFocused(true);
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
            this.drawInfo();
            for (Button gb : buttonList) {
                gb.drawButton(MC, mouseX, mouseY, MC.getRenderPartialTicks());
            }
            drawStrings();
            if (mouseOverButton != null && ModuleUtils.doesModuleExist(mouseOverButton.displayString)) {
                NClient.guiFont.drawHoveringText(ModuleUtils.getDescrption(ModuleUtils.getModule(mouseOverButton.displayString)), mouseX, mouseY);
            }
            mouseOverButton = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 || keyCode == KeyBinds.getModuleKeyBind("ClickGUI")) {
            this.mc.displayGuiScreen((GuiScreen) null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
            Modules.clickGUI.onToggle();
            justPressed = true;
        }
        if (listen) {
            if (this.listining.type == Settings.VALUE_TYPE && (Character.isDigit(typedChar) || typedChar == '-' || typedChar == '.')) {
                if (cache.equals("")) cache = "";
                cache += typedChar;
            }
            if (this.listining.type == Settings.VALUE_TYPE && keyCode == Keyboard.KEY_BACK) {
                cache = StringUtils.chop(cache);
            }
            if (this.listining.type == Settings.STRING_TYPE && !(Character.isISOControl(typedChar))) {
                if (cache.equals("")) cache = "";
                cache += typedChar;
            }
            if (this.listining.type == Settings.STRING_TYPE && keyCode == Keyboard.KEY_BACK) {
                cache = StringUtils.chop(cache);
            }
        }
        if (keyCode == Keyboard.KEY_RETURN) {
            this.listen = false;
            if (ModuleUtils.doesModuleExist(selButton.displayString)) {
                if (listining.type == Settings.VALUE_TYPE) {
                    if (!cache.equals("")) {
                        SettingValue sv = (SettingValue) this.listining;
                        if (NumberUtils.isCreatable(cache))
                            sv.setValue(Float.valueOf(cache));
                        sv.onUpdate();
                    }
                }
                if (listining.type == Settings.STRING_TYPE) {
                    if (!cache.equals("")) {
                        SettingString ss = (SettingString) this.listining;
                        ss.setValue(cache);
                        ss.onUpdate();
                    }
                }
                ModuleUtils.getModule(selButton.displayString).onUpdate();
                cache = "";
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static int getScale() {
        int scale = MC.gameSettings.guiScale;
        if (scale == 0)
            scale = 1000;
        int scaleFactor = 0;
        while (scaleFactor < scale && MC.displayWidth / (scaleFactor + 1) >= 320 && MC.displayHeight / (scaleFactor + 1) >= 240)
            scaleFactor++;
        if (scaleFactor == 0)
            scaleFactor = 1;
        return scaleFactor;
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) {
        calculateMouse();
        x = mouseX;
        y = mouseY;
        for (List<Object> obj : buttonCoords) {
            Button b = (Button) obj.get(0);
            Integer x1 = (Integer) obj.get(1);
            Integer y1 = (Integer) obj.get(2);
            Integer x2 = (Integer) obj.get(3);
            Integer y2 = (Integer) obj.get(4);

            if (x > x1 && x < x2 && y > y1 && y < y2) {

                if (b.id < 9 && b.id > 0) {
                    if (currentCategory == Category.getCategory(b.id)) {
                        currentCategory = Category.ALL;
                        reloadHacks();
                    } else {
                        Gui.currentCategory = Category.getCategory(b.id);
                        reloadHacks();
                    }
                } else if (b.id < 8096) {
                    Module h = ModuleUtils.getModule(b.displayString);
                    if (mouseButton == 0)
                        h.onToggle();
                    if (mouseButton == 1) {
                        new Thread(() -> {
                            while (this.wait) {
                            }
                            if (this.selButton != null && this.selButton.equals(b)) {
                                clearSettings(h);
                                this.selButton = null;
                            } else {
                                if (selButton != null)
                                    clearSettings(ModuleUtils.getModule(selButton.displayString));
                                this.selButton = b;
                                addSettings(h);
                            }
                        }).start();
                    }
                } else if (b.id >= 8096) {
                    if (b instanceof ButtonFloat) {
                        ButtonFloat bf = (ButtonFloat) b;
                        this.listining = bf.parent;
                        this.listen = true;
                        this.listining.onUpdate();
                    }
                    if (b instanceof ButtonString) {
                        ButtonString bs = (ButtonString) b;
                        this.listining = bs.parent;
                        this.listen = true;
                        this.listining.onUpdate();
                    }
                    if (b instanceof ButtonChoose) {
                        ButtonChoose bc = (ButtonChoose) b;
                        bc.mouseReleased(mouseX, mouseY);
                        ModuleUtils.getModule(selButton.displayString).onUpdate();
                    }
                    if (b instanceof ButtonBoolean) {
                        ButtonBoolean bb = (ButtonBoolean) b;
                        bb.mouseReleased(mouseX, mouseY);
                        ModuleUtils.getModule(selButton.displayString).onUpdate();
                    }
                }
            }
        }
        this.wait = false;
    }

    private void calculateMouse() {
        int scaleFactor = getScale();
        mouseX = Mouse.getX() / scaleFactor;
        mouseY = MC.displayHeight / scaleFactor - Mouse.getY() / scaleFactor - 1;
    }

    private void calculateButtons() {
        List<List<Object>> buttonCoords = new ArrayList<>();
        for (Button b : buttonList) {
            Integer _x1 = b.x;
            Integer _y1 = b.y;
            Integer _x2 = b.x + b.width;
            Integer _y2 = b.y + b.height;
            List<Object> data = new ArrayList<>();
            data.add(b);
            data.add(_x1);
            data.add(_y1);
            data.add(_x2);
            data.add(_y2);
            buttonCoords.add(data);
        }
        this.buttonCoords = buttonCoords;
    }

    private void addButtons() {
        int y = 30;
        buttonList.add(addButton(new Button(1, 10, y, 80, 20, "Misc")));
        buttonList.add(addButton(new Button(2, 10, y += 25, 80, 20, "Chat")));
        buttonList.add(addButton(new Button(3, 10, y += 25, 80, 20, "World")));
        buttonList.add(addButton(new Button(4, 10, y += 25, 80, 20, "Player")));
        buttonList.add(addButton(new Button(5, 10, y += 25, 80, 20, "Render")));
        buttonList.add(addButton(new Button(6, 10, y += 25, 80, 20, "Combat")));
        buttonList.add(addButton(new Button(7, 10, y += 25, 80, 20, "Exploit")));
        buttonList.add(addButton(new Button(8, 10, y += 25, 80, 20, "Movement")));
        addHacks();
        calculateButtons();
    }

    private void addHacks() {
        int j = 0;
        int x2 = 95;
        int y2 = 30;
        List<Module> moduleList = NClient.moduleList;
        for (int i = 0; i < moduleList.size(); i++) {
            if (ModuleUtils.getCategory(moduleList.get(i)) == currentCategory || currentCategory == Category.ALL) {
                String name = ModuleUtils.getName(moduleList.get(i));
                if (j == 0) {
                    buttonList.add(addButton(new Button(++ButtonID, x2, y2, 80, 20, name)));
                }
                if (j == 1) {
                    buttonList.add(addButton(new Button(++ButtonID, x2 + 85, y2, 80, 20, name)));
                }
                if (j == 2) {
                    buttonList.add(addButton(new Button(++ButtonID, x2 + 85 * 2, y2, 80, 20, name)));
                    y2 += 25;
                    j = -1;
                }
                j++;
            }
        }
    }

    private void clearHacks() {
        for (int i = 0; i < buttonList.size(); i++) {
            if (ModuleUtils.doesModuleExist(buttonList.get(i).displayString)) {
                buttonList.remove(i);
            }
        }
    }

    private void reloadHacks() {
        clearHacks();
        addHacks();
        calculateButtons();
    }

    private void drawStrings() {
        NClient.guiFont.drawString("N-Client " + NClient.VERSION, 10, 10, -1);
    }

    private void addSettings(Module module) {
        int settingID = 8096;
        int x = 355;
        int y = 30;
        int i = 0;
        for (Setting s : module.settings) {
            if (s.getType() == Settings.VALUE_TYPE) {
                SettingValue sv = (SettingValue) s;
                Button b = new ButtonFloat(settingID, x, y, s.name, sv);
                sv.element = (ButtonFloat) b;
                buttonList.add(addButton(b));
            }
            if (s.getType() == Settings.STRING_TYPE) {
                SettingString ss = (SettingString) s;
                Button b = new ButtonString(settingID, x, y, s.name, ss);
                ss.element = (ButtonString) b;
                buttonList.add(addButton(b));
            }
            if (s.getType() == Settings.CHOOSE) {
                SettingChoose sc = (SettingChoose) s;
                Button b = new ButtonChoose(settingID, x, y, s.name, sc);
                sc.element = (ButtonChoose) b;
                buttonList.add(addButton(b));
            }
            if (s.getType() == Settings.BOOLEAN) {
                SettingBoolean sb = (SettingBoolean) s;
                Button b = new ButtonBoolean(settingID, x, y, s.name, sb);
                sb.element = (ButtonBoolean) b;
                buttonList.add(addButton(b));
            }
            s.id = settingID;
            settingID++;
            y += 25;
            i++;
            if (i > 18) {
                x += 125;
                y = 30;
            }
        }
        calculateButtons();
    }

    private void clearSettings(Module module) {
        for (int i = 0; i < buttonList.size(); i++) {
            for (Setting s : module.settings) {
                if (buttonList.get(i).getSetting() != null && s.id == buttonList.get(i).getSetting().id) {
                    if (buttonList.get(i).id >= 8096) {
                        buttonList.remove(i);
                    }
                }
            }
        }
        for (int i = 0; i < buttonList.size(); i++) {
            for (Setting s : module.settings) {
                if (Button.getSetting(buttonList.get(i)) != null && s.id == Button.getSetting(buttonList.get(i)).id) {
                    if (buttonList.get(i).id >= 8096) {
                        buttonList.remove(i);
                    }
                }
            }
        }
        calculateButtons();
    }

    public static void drawBackground(ResourceLocation texture) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        MC.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double) MC.currentScreen.height, 0.0D).tex(0.0D, (double) ((float) 1 + (float) 0)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double) MC.currentScreen.width, (double) MC.currentScreen.height, 0.0D).tex((double) ((float) 1), (double) ((float) 1 + (float) 0)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double) MC.currentScreen.width, 0.0D, 0.0D).tex((double) ((float) 1), (double) 0).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) 0).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public static void drawBackground() {
        //drawBackground(NClient.background); i commented this because it doesn't work yet and throws exceptions
    }

    private void drawInfo() {
        String coords = "XYZ: [" + MC.player.posX + ", " + MC.player.posY + ", " + MC.player.posZ + "]";
        String nether = !(MC.world.getBiome(MC.player.getPosition()) instanceof BiomeHell) ?
                "Nether: [" + MC.player.posX / 8 + ", " + MC.player.posY + ", " + MC.player.posZ / 8 + "]" :
                "Overworld: [" + MC.player.posX * 8 + ", " + MC.player.posY + ", " + MC.player.posZ * 8 + "]";
        String fps = "FPS: " + Minecraft.getDebugFPS();
        String tps = "TPS: " + NClient.tps;
        String ping = "PING: " + String.valueOf(MC.getCurrentServerData() == null ? 0 : MC.getCurrentServerData().pingToServer);
        String biggest = coords.length() > nether.length() ? coords : nether;

        int y = 30;
        this.drawGradientRect(485, y, 490 + Math.round(NClient.guiFont.getStringWidth(biggest)) + 5, y + 5 + 15 * 5, -1072689136, -804253680);
        NClient.guiFont.drawString(coords, 490, y += 5, -1);
        NClient.guiFont.drawString(nether, 490, y += 15, -1);
        NClient.guiFont.drawString(fps, 490, y += 15, -1);
        NClient.guiFont.drawString(tps, 490, y += 15, -1);
        NClient.guiFont.drawString(ping, 490, y += 15, -1);
    }
}
