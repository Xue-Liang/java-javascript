import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
Mongodb reader and writer

<dependency>
   <groupId>org.mongodb</groupId>
   <artifactId>mongo-java-driver</artifactId>
   <version>3.4.1</version>
</dependency
*/
public class MongoDao{
/**
 * Created by xue on 17-1-16.
 */
public class MongoReader {
    private static Logger logger = LoggerFactory.getLooger(MongoReader.class);
    protected MongoClient mongoCient = null;

    public MongoReader(String[] hosts, String databaseName, String userName, String password) {
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        for (String host : hosts) {
            String[] hp = host.split(":");
            if (hp.length == 2) {
                host = hp[0];
                int port = Integer.parseInt(hp[1]);
                ServerAddress addr = new ServerAddress(host, port);
                addrs.add(addr);
            }
        }
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        MongoCredential credential = MongoCredential.createCredential(userName, databaseName, password.toCharArray());
        credentials.add(credential);
        credentials.add(credential.withMechanismProperty("authenticationMechanism", "MONGODB-CR"));

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectTimeout(5000);
        builder.requiredReplicaSetName("gagrep");
        builder.readPreference(ReadPreference.nearest());
        builder.writeConcern(WriteConcern.MAJORITY);
        MongoClientOptions options = builder.build();
        this.mongoCient = new MongoClient(addrs, credentials, options);

    }

    public MongoDatabase getMongoDatabase(String databaseName) {
        return this.mongoCient.getDatabase(databaseName);
    }


    public MongoCursor<Document> query(String databaseName, String collectionName, Bson query) {
        MongoDatabase db = this.getMongoDatabase(databaseName);
        MongoCollection<Document> docs = db.getCollection(collectionName);
        FindIterable<Document> it = docs.find(query);
        return it.iterator();
    }

    public void close() {
        if (this.mongoCient != null) {
            try {
                this.mongoCient.close();
            } catch (Exception e) {
               logger.error("关闭mongoClient时发生异常.",e);
            }
        }
    }
}



/**
 * Created by xue on 17-1-16.
 */
public class MongoWriter extends MongoReader {
   private static Logger logger = LoggerFactroy.getLogger(MongoWriter.class);
    /**
     * @param host         "192.168.6.223"
     * @param port         17017
     * @param databaseName admin
     * @param userName     eval_user
     * @param password     eval_mongo123_3
     */
    public MongoWriter(String host, int port, String databaseName, String userName, String password) throws UnknownHostException {
        super(host, port, databaseName, userName, password);
    }

    public MongoWriter(String[] hosts, String databaseName, String userName, String password) {
        super(hosts, databaseName, userName, password);
    }

    public List<Document> write(String databaseName, String collectionName, Collection<Document> docs) {
        Iterator<Document> it = docs.iterator();
        List<Document> fails = new ArrayList<>(docs.size());
        while (it.hasNext()) {
            Document doc = it.next();
            boolean success = this.write(databaseName, collectionName, doc);
            if (!success) {
                fails.add(doc);
            }
        }
        return fails;
    }

    public boolean write(String databaseName, String collectionName, Document doc) {
        try {
            MongoDatabase database = this.mongoCient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            logger.error("插入数据时发生异常.",e);
            return false;
        }
    }
}



}
