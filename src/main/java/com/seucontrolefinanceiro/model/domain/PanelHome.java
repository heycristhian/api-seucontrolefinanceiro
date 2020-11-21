package com.seucontrolefinanceiro.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PanelHome {
    List<Panel> panels;
    Integer panelQuantity;
}
