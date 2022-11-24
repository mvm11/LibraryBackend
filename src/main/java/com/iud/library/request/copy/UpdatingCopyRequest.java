package com.iud.library.request.copy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingCopyRequest {

    private String editionNumber;
    private String state;
}
