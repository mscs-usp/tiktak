TOMCATROOT="$HOME/apache-tomcat-7.0.39"
TIKTAKROOT="$HOME/labXP/tiktak/"
DASHBOARDROOT="$TIKTAKROOT/tiktak-dashboard/core/"

cd $DASHBOARDROOT
mvn clean install
mvn refdb:ref -Ph2

cd ..
mvn clean install

mkdir $TOMCATROOT/tiktak/

cp -av $HOME/.m2/repository/com/h2database/h2/1.1.109/h2-1.1.109.jar $TOMCATROOT/lib/

mkdir $TOMCATROOT/lib/tiktak.jar/

cp -av $HOME/labXP/tiktak/tiktak-dashboard/web/src/test/resources/deploy/* $TOMCATROOT/lib/tiktak.jar/

cat <<EOF >> $TOMCATROOT/lib/tiktak.jar/application-config.properties

#conf para H2
datasource.driverClassName=org.h2.Driver
datasource.url=jdbc:h2:file:tiktak/loadeddb;LOCK_MODE=3;MVCC=TRUE;AUTO_SERVER=TRUE;LOG=0
datasource.username=sa
datasource.password=a
database.hibernate.dialect=jmine.tec.environment.db.dialect.H2SequenceDialect
EOF

cp -av $DASHBOARDROOT/target/loadeddb* $TOMCATROOT/tiktak/

cp -av
