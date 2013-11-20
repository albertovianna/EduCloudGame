package com.cloudgame.core.models;

import android.graphics.Bitmap;


public class Piece implements Cloneable{
	
	int index;
	Bitmap bitmap;
	boolean isVisible;
	boolean isPaired;

	public Piece(int index, Bitmap bitmap) {
		this.index = index;
		this.bitmap = bitmap;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isPaired() {
		return isPaired;
	}

	public void setPaired(boolean isPaired) {
		this.isPaired = isPaired;
	}
}
