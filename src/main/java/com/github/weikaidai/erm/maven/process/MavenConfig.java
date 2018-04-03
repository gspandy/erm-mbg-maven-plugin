package com.github.weikaidai.erm.maven.process;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class MavenConfig {

	private String targetPackage;
	
	private String superMapper;

	public String getSuperMapper() {
		return superMapper;
	}

	public void setSuperMapper(String superMapper) {
		this.superMapper = superMapper;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

}
