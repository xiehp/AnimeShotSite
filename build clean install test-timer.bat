
set nowPath=%cd%
echo ���ƶ���ǰcdλ�õ��ļ�����Ŀ¼
cd %~dp0

echo  ��ʼbuild
mvn -Ptest,timer -B -Dmaven.test.skip=true -DskipTests --threads 2 -s D:\work\maven\maven-setting.xml clean install

echo �ص�ԭ��cd����Ŀ¼
cd %nowPath%