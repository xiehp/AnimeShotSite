:: Please run the file in tomcat dir

:: chcp 65001
:: set "JAVA_OPTS=-Dfile.encoding=GBK"
set "JAVA_HOME=D:\work\java\jdk1.8.0_201"
set "CATALINA_OPTS=-Dspring.profiles.active=product181 -Xmx500m"
set "TITLE=%~f0"

:: other path\tomcat dir\webapps\ROOT\WEB-INF\this file
:: 跳转到tomcat的bin目录other path\tomcat dir\bin
cd ..
cd ..
cd ..
cd bin

call startup.bat -Dspring.profiles.active=product181
