CREATE KEYSPACE IF NOT EXISTS phishing_cache
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};