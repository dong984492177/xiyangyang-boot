package com.ywt.console.models.resmodel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgInfoModel {

    private String rawData;
    private String trigger;
}
