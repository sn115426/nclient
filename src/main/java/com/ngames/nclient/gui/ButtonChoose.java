package com.ngames.nclient.gui;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.settings.SettingChoose;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonChoose extends Button
{
	SettingChoose parent;
	public String ds;
	
	public ButtonChoose(int buttonId, int x, int y, String buttonText, SettingChoose parent)
	{
		super(buttonId, x, y, 120, 20, buttonText);
		this.parent = parent;
		this.ds = this.parent.name + ": " + this.parent.mapping.get(this.parent.getValue());
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
            }
            NClient.guiFont.drawCenteredString(this.ds, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY)
	{
		this.parent.setValue();
		this.ds = this.parent.name + ": " + this.parent.mapping.get(this.parent.getValue());
		this.parent.onUpdate();
	}
}
