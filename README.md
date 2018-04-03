## 说明
将eclipse ermaster插件的模型，映射为mybatis的实体和mapper

## 使用
自己的maven项目中引入相关依赖
### dependencies中
<dependency>
	<groupId>tk.mybatis</groupId>
	<artifactId>mapper</artifactId>
	<version>3.5.3</version>
</dependency>

### build下的plugins中
<plugin>
 <groupId>com.github.weikaidai</groupId>
  <artifactId>erm-mbg-maven-plugin</artifactId>
  <version>1.0.0-SNAPSHOT</version>
    <executions>
      <execution>
        <goals>
          <goal>ermMbgMojo</goal>
        </goals>
          <configuration>
          <sources>
		    <source>ermfile.erm erm文件，相对与项目跟目录</source>
		  </sources>
          <targetPackage>生成的实体所在目录，生成实体在此路径下的 entity包内，mapper在此目录的entitymapper包内</targetPackage>
		  <superMapper>所有mapper继承的基础接口，可以不写。默认tk.mybatis.mapper.common.Mapper</superMapper>
		  </configuration>
		</execution>
	</executions>
</plugin>

