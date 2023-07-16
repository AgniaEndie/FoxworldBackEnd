package ru.endienasg.foxworld.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkinModel {
    private String url;
    private String digest;
    private MetadataModel metadata;
}
