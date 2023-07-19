package ru.endienasg.foxworld.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.endienasg.foxworld.models.CapeModel;
import ru.endienasg.foxworld.models.SkinModel;
@Setter
public class TextureFullResponse {

    private SkinModel SKIN;
    private CapeModel CAPE;
    @JsonProperty("SKIN")
    public SkinModel getSKIN() {
        return SKIN;
    }
    @JsonProperty("CAPE")
    public CapeModel getCAPE() {
        return CAPE;
    }
}
