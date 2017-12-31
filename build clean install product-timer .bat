
set nowPath=%cd%
echo 先移动当前cd位置到文件所在目录
cd %~dp0

echo  开始build
mvn -Pproduct,timer -B -Dmaven.test.skip=true -DskipTests --threads 2 -s D:\work\maven\maven-setting.xml clean install

echo 回到原来cd所在目录
cd %nowPath%