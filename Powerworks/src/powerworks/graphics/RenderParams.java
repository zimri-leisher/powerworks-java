package powerworks.graphics;

import java.awt.Rectangle;

public class RenderParams {

    float widthScale = 1, heightScale = 1, scale = 1, alpha = 1;
    int xPixelOffset, yPixelOffset, rotation;
    Rectangle clip;
    boolean screenObject = true;

    public RenderParams setWidthScale(float widthScale) {
	this.widthScale = widthScale;
	return this;
    }

    public RenderParams setHeightScale(float heightScale) {
	this.heightScale = heightScale;
	return this;
    }

    public RenderParams setScale(float scale) {
	this.scale = scale;
	return this;
    }

    public RenderParams setAlpha(float alpha) {
	this.alpha = alpha;
	return this;
    }

    public RenderParams setXPixelOffset(int xPixelOffset) {
	this.xPixelOffset = xPixelOffset;
	return this;
    }

    public RenderParams setYPixelOffset(int yPixelOffset) {
	this.yPixelOffset = yPixelOffset;
	return this;
    }

    public RenderParams setRotation(int rotation) {
	this.rotation = rotation;
	return this;
    }

    public RenderParams setClip(Rectangle clip) {
	this.clip = clip;
	return this;
    }

    public RenderParams setScreenObject(boolean screenObject) {
	this.screenObject = screenObject;
	return this;
    }
}
