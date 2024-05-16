package org.vut.ija_project.ApplicationLayer.Canvas;

public interface SelectableView {
    public void setSelected(boolean isSelected);

    public boolean isClicked(double x, double y);
}
