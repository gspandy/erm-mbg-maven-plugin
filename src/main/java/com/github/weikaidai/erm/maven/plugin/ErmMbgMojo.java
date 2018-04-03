package com.github.weikaidai.erm.maven.plugin;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.weikaidai.erm.maven.process.Generator;
import com.github.weikaidai.erm.maven.process.MavenConfig;
import com.github.weikaidai.erm.meta.Database;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
@Mojo(name="ermMbgMojo",defaultPhase=LifecyclePhase.GENERATE_SOURCES)
public class ErmMbgMojo extends AbstractMojo{

	@Parameter
	private String targetPackage;
	
	@Parameter
	private String superMapper;
	
	@Parameter
	private File sources[];
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if(StringUtils.isBlank(superMapper)) {
			superMapper = "tk.mybatis.mapper.common.Mapper";
		}
		
		MavenConfig mavenConfig = new MavenConfig();
		mavenConfig.setTargetPackage(targetPackage);
		mavenConfig.setSuperMapper(superMapper);
		
		for (File source : sources)
		{
			System.out.println("source erm file :"+ source);
			
			Database database = Database.genDatabase(source);
			Generator generator = new Generator(database,mavenConfig);
			generator.generatorJava();
		}
		
	}

}
