set OPTS=-Djava.util.logging.config.file=logging.properties -Dfile.encoding=UTF-8
set CP1=../ColoryChart-IMPL/target/ColoryChart-IMPL-1.0-SNAPSHOT.jar;../ColoryChart-API/target/ColoryChart-API-1.0-SNAPSHOT.jar
set CP2=%CP1%;../lafmanager/target/lafmanager-1.0-SNAPSHOT.jar;../ColoryHelpSet/target/ColoryHelpSet-1.0-SNAPSHOT.jar
set CP3=%CP2%;./lib/jcommon-1.0.17.jar;./lib/jfreechart-1.0.14.jar;./lib/jhall.jar;./lib/ColoryProps.jar
set CP=%CP3%;target/ColoryGame-1.0-SNAPSHOT.jar
java %OPTS% -cp %CP% com.home.colorygame.colory.ColoryFrame
