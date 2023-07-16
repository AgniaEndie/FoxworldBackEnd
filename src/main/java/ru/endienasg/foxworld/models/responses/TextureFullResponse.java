package ru.endienasg.foxworld.models.responses;

import lombok.Getter;
import lombok.Setter;
import ru.endienasg.foxworld.models.CapeModel;
import ru.endienasg.foxworld.models.SkinModel;

@Getter
@Setter
public class TextureFullResponse {
    private SkinModel SKIN;
    private CapeModel CAPE;
}
