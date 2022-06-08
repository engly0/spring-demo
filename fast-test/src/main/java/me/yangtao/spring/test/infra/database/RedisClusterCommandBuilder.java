package me.yangtao.spring.test.infra.database;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.protocol.BaseRedisCommandBuilder;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;

import java.util.List;

import static io.lettuce.core.protocol.CommandKeyword.SLOTS;
import static io.lettuce.core.protocol.CommandType.CLUSTER;

public class RedisClusterCommandBuilder<K, V> extends BaseRedisCommandBuilder<K, V> {
    public RedisClusterCommandBuilder(RedisCodec<K, V> codec) {
        super(codec);
    }

    public Command<K, V, List<Object>> clusterSlots() {
        CommandArgs<K, V> args = new CommandArgs<>(codec).add(SLOTS);
        return createCommand(CLUSTER, new ArrayOutput<>(codec), args);
    }
}
