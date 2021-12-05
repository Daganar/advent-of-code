package uk.co.techbound.adentofcode;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AdventOfCodeApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AdventOfCodeApplication.class)
			.beanNameGenerator(new BeanNameGenerator() {
				@Override
				public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
					if(definition instanceof ProblemSolver) {
						ProblemSolver problemSolver = (ProblemSolver) definition;
						ProblemName problemName = problemSolver.getProblemName();
						return "Problem-" + problemName.getYear() + "-" + problemName.getDayOfMonth();
					}
					return DefaultBeanNameGenerator.INSTANCE.generateBeanName(definition, registry);
				}
			}).run(args);
	}

}
