package com.sample.config;

import java.util.Locale;

import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@Import(PropertyPlaceholderConfig.class)
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	private static Log logger = LogFactory.getLog(WebMvcConfig.class);
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	        "classpath:/META-INF/resources/", "classpath:/resources/",
	        "classpath:/static/", "classpath:/public/" };
	
	@Value("${server.session.timeout}")
	private String sessionTimeout;

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
		MyUrlAuthenticationSuccessHandler.SESSION_TIMEOUT_IN_SECONDS = Integer.valueOf(sessionTimeout);
	}
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		 registry.addViewController("/login").setViewName("login");
	}
	
	@Bean
	public HttpSessionListener httpSessionListener(){
	    return new MySessionListener();
	}

	@Bean
	@Profile("dev")
	public ServletRegistrationBean h2servletRegistrationDev() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
		registration.addUrlMappings("/h2-console/*");
		return registration;
	}
	
	@Bean
	@Profile("default")
	public ServletRegistrationBean h2servletRegistrationDefault() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
		
		return registration;
	}

	@Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Integer period = 31556926;
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(
	                "classpath:/META-INF/resources/webjars/").setCachePeriod(period);
		}
		 registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	} 
	
	@Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }
 
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setCacheSeconds(10); // reload messages every 10 seconds
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}