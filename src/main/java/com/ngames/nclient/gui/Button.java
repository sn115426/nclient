package com.ngames.nclient.gui;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.ModuleUtils;
import com.ngames.nclient.module.settings.Setting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class Button	extends GuiButton
{
	public static final int enabledColor = Theme.getRGB((byte) 255, (byte) 45, (byte) 32);
	public static final int listenColor = Theme.getRGB((byte) 86, (byte) 64, (byte) 193);
	
	public Button(int buttonId, int x, int y, String buttonText)
	{
		super(buttonId, x, y, buttonText);
	}
	
	public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
        {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered) 
            {
                j = 16777120;
                NClient.gui.mouseOverButton = this;
            }
            
            if (ModuleUtils.doesModuleExist(this.displayString) && ModuleUtils.getModule(this.displayString).isEnabled())
            {
            	j = enabledColor;
            }

            NClient.guiFont.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
	}

	public Setting getSetting()
	{
		if (this instanceof ButtonFloat)
		{
			return ((ButtonFloat) this).parent;
		}
		if (this instanceof ButtonString)
		{
			return ((ButtonString) this).parent;
		}
		if (this instanceof ButtonChoose)
		{
			return ((ButtonChoose) this).parent;
		}
		if (this instanceof ButtonBoolean)
		{
			return ((ButtonBoolean) this).parent;
		}
		return null;
	}
	
	public static Setting getSetting (GuiButton button)
	{
		if (button instanceof ButtonFloat)
		{
			return ((ButtonFloat) button).parent;
		}
		if (button instanceof ButtonString)
		{
			return ((ButtonString) button).parent;
		}
		if (button instanceof ButtonChoose)
		{
			return ((ButtonChoose) button).parent;
		}
		if (button instanceof ButtonBoolean)
		{
			return ((ButtonBoolean) button).parent;
		}
		return null;
	}
}
