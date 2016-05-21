
echo %~dp0

cd %~dp0

D:\work\java\X64\jdk1.8.0_66\bin\java -version

D:\work\java\X64\jdk1.8.0_66\bin\java -Xmx512M -XX:MaxPermSize=256M -Dspring.profiles.active=production -Dlogback.configurationFile=conf/logback.xml -Djava.ext.dirs=AnimeLib;OtherLib; -classpath conf;AnimeShotSite-timer-0.0.1-SNAPSHOT.jar; xie.animeshotsite.timer.main.MainTimer