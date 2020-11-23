package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.model.domain.PanelHome;
import com.seucontrolefinanceiro.services.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/panels")
public class PanelResource {

    @Autowired
    private PanelService panelService;

    @GetMapping
    public PanelHome panelHome(@RequestParam String userId) {
        return panelService.getPanel(userId);
    }
}
