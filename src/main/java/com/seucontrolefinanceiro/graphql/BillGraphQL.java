package com.seucontrolefinanceiro.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.seucontrolefinanceiro.model.domain.PanelHome;
import com.seucontrolefinanceiro.services.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillGraphQL implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired private PanelService panelService;

    public PanelHome panelHome(String userId) {
        return panelService.getPanelHome(userId);
    }
}
