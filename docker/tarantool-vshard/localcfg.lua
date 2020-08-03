return {
    memtx_memory = 100 * 1024 * 1024,
    sharding = {
        ['cbf06940-0790-498b-948d-042b62cf3d29'] = { -- replicaset #1
            replicas = {
                ['8a274925-a26d-47fc-9e1b-af88ce939412'] = {
                    uri = 'storage:storage@127.0.0.1:3301',
                    name = 'storage_1_a',
                    master = true
                },
                ['3de2e3e1-9ebe-4d0d-abb1-26d301b84633'] = {
                    uri = 'storage:storage@127.0.0.1:3302',
                    name = 'storage_1_b'
                }
            },
        }
    }, -- sharding
    replication_connect_quorum = 0,
}