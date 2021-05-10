package com.ngames.nclient.gui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.ngames.nclient.NClient;
import com.ngames.nclient.gui.render.Renderer2D;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NFontRenderer
{
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("\u00A7" + "[0123456789abcdefklmnor]");
    public final int FONT_HEIGHT = 9;
    private final int[] colorCodes =
    { 0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF };
    private float antiAliasingFactor;
    private UnicodeFont unicodeFont;
    private int prevScaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    private Font font;

    @SuppressWarnings("unchecked")
	public NFontRenderer(Font font, int fontSize)
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        prevScaleFactor = resolution.getScaleFactor();
        unicodeFont = new UnicodeFont(font.deriveFont((float)font.getSize() * prevScaleFactor / 2), fontSize, false, false);
        unicodeFont.addAsciiGlyphs();
        unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        try {
			unicodeFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
        this.antiAliasingFactor = resolution.getScaleFactor();
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    @SuppressWarnings("unchecked")
	public int drawString(String text, float x, float y, int color)
    {
        if (text == null)
            return 0;

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        
        try
        {
            if (resolution.getScaleFactor() != prevScaleFactor)
            {
                prevScaleFactor = resolution.getScaleFactor();
                unicodeFont = new UnicodeFont(font.deriveFont((float)font.getSize() * prevScaleFactor / 2));
                unicodeFont.addAsciiGlyphs();
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.loadGlyphs();
            }
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        GL11.glPushMatrix();
        GlStateManager.scale(1 / antiAliasingFactor, 1 / antiAliasingFactor, 1 / antiAliasingFactor);
        x *= antiAliasingFactor;
        y *= antiAliasingFactor;
        float originalX = x;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);

        int currentColor = color;

        char[] characters = text.toCharArray();

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String s : parts)
        {
            for (String s2 : s.split("\n"))
            {
                for (String s3 : s2.split("\r"))
                {

                    unicodeFont.drawString(x, y, s3, new org.newdawn.slick.Color(currentColor));
                    x += unicodeFont.getWidth(s3);

                    index += s3.length();
                    if (index < characters.length && characters[index] == '\r')
                    {
                        x = originalX;
                        index++;
                    }
                }
                if (index < characters.length && characters[index] == '\n')
                {
                    x = originalX;
                    y += getHeight(s2) * 2;
                    index++;
                }
            }
            if (index < characters.length)
            {
                char colorCode = characters[index];
                if (colorCode == '\u00A7')
                {
                    char colorChar = characters[index + 1];
                    int codeIndex = ("0123456789" + "abcdef").indexOf(colorChar);
                    if (codeIndex < 0)
                    {
                        if (colorChar == 'r')
                        {
                            currentColor = color;
                        }
                    }
                    else
                    {
                        currentColor = colorCodes[codeIndex];
                    }
                    index += 2;
                }
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
        return (int) getWidth(text);
    }

    public int drawStringWithShadow(String text, float x, float y, int color)
    {
        if (text == null || text == "")
            return 0;

        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0x000000);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color)
    {
        drawString(text, x - ((int) getWidth(text) >> 1), y - 2, color);
    }

    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawCenteredString(text, 0, 0, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color)
    {
        drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, color);
        drawCenteredString(text, x, y, color);
    }
    
    public void drawHoveringText (String text, int x, int y)
    {
    	if (!text.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = (int) getWidth(text);
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (l1 + i > NClient.MC.displayWidth)
            {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > NClient.MC.displayHeight)
            {
                i2 = NClient.MC.displayHeight - k - 6;
            }
            
            Renderer2D.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
            Renderer2D.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
            Renderer2D.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
            Renderer2D.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
            Renderer2D.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
            Renderer2D.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
            Renderer2D.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
            Renderer2D.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
            Renderer2D.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

            this.drawStringWithShadow(text, (float)l1, (float)i2, -1);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public float getWidth(String text)
    {
        return unicodeFont.getWidth(text) / antiAliasingFactor;
    }

    public float getCharWidth(char c)
    {
        return unicodeFont.getWidth(String.valueOf(c)) / antiAliasingFactor;
    }

    public float getHeight(String s)
    {
        return unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont()
    {
        return unicodeFont;
    }

    public void drawSplitString(ArrayList<String> lines, int x, int y, int color)
    {
        drawString(String.join("\n\r", lines), x, y, color);
    }

    public List<String> splitString(String text, int wrapWidth)
    {
        List<String> lines = new ArrayList<>();

        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();

        for (String word : splitText)
        {
            String potential = currentString + " " + word;

            if (getWidth(potential) >= wrapWidth)
            {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }

            currentString.append(word).append(" ");
        }

        lines.add(currentString.toString());
        return lines;
    }

    public float getStringWidth(String p_Name)
    {   
        return ((float)unicodeFont.getWidth(p_Name)) / 2;
    }

    public float getStringHeight(String p_Name)
    {
        return getHeight(p_Name);
    }

    /**
     * Trims a string to fit a specified Width.
     */
    public String trimStringToWidth(String text, int width)
    {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse)
    {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k)
        {
            char c0 = text.charAt(l);
            float i1 = this.getWidth(text);

            if (flag)
            {
                flag = false;

                if (c0 != 'l' && c0 != 'L')
                {
                    if (c0 == 'r' || c0 == 'R')
                    {
                        flag1 = false;
                    }
                }
                else
                {
                    flag1 = true;
                }
            }
            else if (i1 < 0)
            {
                flag = true;
            }
            else
            {
                i += i1;

                if (flag1)
                {
                    ++i;
                }
            }

            if (i > width)
            {
                break;
            }

            if (reverse)
            {
                stringbuilder.insert(0, c0);
            }
            else
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}