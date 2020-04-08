package com.atguigu.myrule;

import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule {

	@Bean
	public IRule myRule(){
		IRule result = new RandomRule();
		//IRule result = new RoundRobinRule();
		//IRule result = new ClientConfigEnabledRoundRobinRule();
		//IRule result = new RetryRule();
		//new WeightedResponseTimeRule();
		//new BestAvailableRule();
		//new ZoneAvoidanceRule();
		//new AvailabilityFilteringRule();
		return  result;
	}
}
