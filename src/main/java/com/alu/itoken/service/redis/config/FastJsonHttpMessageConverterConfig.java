package com.alu.itoken.service.redis.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class FastJsonHttpMessageConverterConfig extends WebMvcConfigurerAdapter{
	  @Bean
	    public HttpMessageConverters fastJsonConfigure() {
	        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
	        FastJsonConfig fastJsonConfig = new FastJsonConfig();
	        //日期格式化
	        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
	        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.BrowserCompatible, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
	        converter.setFastJsonConfig(fastJsonConfig);
	        List<MediaType> fastMediaTypes = new ArrayList<>();
	        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
	        fastMediaTypes.add(MediaType.APPLICATION_JSON);
	        converter.setSupportedMediaTypes(fastMediaTypes);
	        return new HttpMessageConverters(converter);
	    }
}