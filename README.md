# pagerank

## build
mvn eclipse:eclipse
mvn package

## run master
mvn exec:java -Dexec.mainClass=master.Master -Dexec.args="--configure=/path/to/configure/file" -Dexec.classpathScope=compile

## run worker
mvn exec:java -Dexec.mainClass=worker.Worker -Dexec.args="--configure=/path/to/configure/file" -Dexec.classpathScope=compile
