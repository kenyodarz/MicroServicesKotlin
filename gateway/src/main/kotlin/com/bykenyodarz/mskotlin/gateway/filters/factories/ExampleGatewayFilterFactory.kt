package com.bykenyodarz.mskotlin.gateway.filters.factories

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component

@Component
class ExampleGatewayFilterFactory: AbstractGatewayFilterFactory<ExampleGatewayFilterFactory.Config>() {
    class Config {

    }

    override fun apply(config: Config?): GatewayFilter {
        TODO("Not yet implemented")
    }


}