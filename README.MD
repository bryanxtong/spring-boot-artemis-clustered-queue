Please refer to url below for activemq artemis cluster queue configuration
https://github.com/bryanxtong/spring-boot-artemis-clustered-topic

Three nodes forms a cluster and there is one producer which produces message to the cluster of nodes, and there are three(or more) consumers and all the messages will be load balanced between the consumers.

Sometimes If a consumer fails to connect to a broker , It will connect to other brokers and consume messages of that broker and this way, maybe redistribution needs to be configured.