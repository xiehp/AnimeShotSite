
echo %~dp0

cd %~dp0

# set the java home path
set "app_java_home_path=D:\work\java\X64\jdk1.8.0_66"


set "app_java_exe=%app_java_home_path%\bin\java"
set "app_java_ext_lib_path=%app_java_home_path%\jre\lib\ext"


%app_java_exe% -version
%app_java_exe% -Djava.ext.dirs=%app_java_ext_lib_path%;AnimeLib;OtherLib; -Xmx400M -XX:MaxPermSize=256M -Dspring.profiles.active=production -Dlogback.configurationFile=conf/logback.xml -classpath conf;AnimeShotSite-timer-0.0.1-SNAPSHOT.jar; xie.animeshotsite.timer.main.MainTimer