# pagerank

- build
mvn eclipse:eclipse
mvn package

- run master
mvn exec:java -Dexec.mainClass=master.Master

- run worker
mvn exec:java -Dexec.mainClass=worker.Worker
