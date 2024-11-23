FROM docker.elastic.co/elasticsearch/elasticsearch:8.7.1

RUN if ! elasticsearch-plugin list | grep -q analysis-nori; then \
      elasticsearch-plugin install analysis-nori; \
    fi
