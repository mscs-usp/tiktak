TOMCATROOT="$HOME/apache-tomcat-7.0.39"
TIKTAKROOT="$HOME/labXP/tiktak"
DASHBOARDROOT="$TIKTAKROOT/tiktak-dashboard"

$TOMCATROOT/bin/shutdown.sh

echo "#1"
cd $DASHBOARDROOT/core/
mvn clean install
mvn refdb:ref -Ph2

cd ..
#mvn clean install

echo "#2"
mkdir $TOMCATROOT/bin/tiktak/

echo "#3"
cp -av $HOME/.m2/repository/com/h2database/h2/1.1.109/h2-1.1.109.jar $TOMCATROOT/lib/

echo "#4"
mkdir $TOMCATROOT/lib/tiktak.jar/
cp -av $DASHBOARDROOT/web/src/test/resources/deploy/* $TOMCATROOT/lib/tiktak.jar/

echo "#5"
cat <<EOF >> $TOMCATROOT/lib/tiktak.jar/application-config.properties

#conf para H2
datasource.driverClassName=org.h2.Driver
datasource.url=jdbc:h2:file:tiktak/loadeddb;LOCK_MODE=3;MVCC=TRUE;AUTO_SERVER=TRUE;LOG=0
datasource.username=sa
datasource.password=a
database.hibernate.dialect=jmine.tec.environment.db.dialect.H2SequenceDialect
EOF

echo "#6"
cp -av $DASHBOARDROOT/core/target/loadeddb* $TOMCATROOT/bin/tiktak/

# Experimental
cd $DASHBOARDROOT
#mvn clean install
mvn install

echo "#7"
cp -av $DASHBOARDROOT/web/target/tiktak-dashboard.war $TOMCATROOT/webapps

$TOMCATROOT/bin/startup.sh
sleep 15

echo "#8"
cp -av $HOME/.m2/repository/jmine/tec/jmine-tec-persist/2.10.2-RC1/jmine-tec-persist-2.10.2-RC1-tests.jar $TOMCAT/webapps/tiktak-dashboard/WEB-INF/lib/
