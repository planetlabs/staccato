package com.boundlessgeo.staccato.es.config;

import lombok.Data;

/**
 * @author joshfix
 * Created on 2/12/19
 */
@Data
public class ShardReplicaConfig {
    private int numberOfShards = 2;
    private int numberOfReplicas = 1;
}
