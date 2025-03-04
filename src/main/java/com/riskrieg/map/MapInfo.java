package com.riskrieg.map;

import com.riskrieg.map.alignment.InterfaceAlignment;

public class MapInfo { // TODO: Convert to Record (?) -- Need to move MapStatus first

  private final String name;
  private final String displayName;
  private final String author;
  private final InterfaceAlignment alignment;
  private MapStatus status;

  public MapInfo(String name, String displayName, String author, InterfaceAlignment alignment) {
    this.name = name;
    this.displayName = displayName;
    this.author = author;
    this.alignment = alignment;
  }

  public String name() {
    return name;
  }

  public String displayName() {
    return displayName;
  }

  public String author() {
    return author;
  }

  public InterfaceAlignment alignment() {
    return alignment;
  }

  public MapStatus status() {
    return status;
  }

  public void setStatus(MapStatus status) {
    this.status = status;
  }

}
