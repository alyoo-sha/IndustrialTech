package org.mod.industrialtech_ae.client.gui.element;

public class Rect {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int screenX(int leftPos) {
        return leftPos + this.x;
    }

    public int screenY(int topPos) {
        return topPos + this.y;
    }

    public boolean contains(double mouseX, double mouseY, int leftPos, int topPos) {
        int sx = this.screenX(leftPos);
        int sy = this.screenY(topPos);
        return mouseX >= (double)sx && mouseX < (double)(sx + this.width) && mouseY >= (double)sy && mouseY < (double)(sy + this.height);
    }
}
