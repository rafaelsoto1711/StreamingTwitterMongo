/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package recolecciontweetmongo;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class EarthQuakeListener implements StatusListener {
    
    public final static int MAX_TWEETS_ACCUM= 1; // Max number of tweets accumulated before saving	
	private List<Status> tweetAccum; // Accumulates Tweets

	private MongoConnection mongoConnection;
	
    public EarthQuakeListener(MongoConnection mongo) {		

            this.mongoConnection=mongo;
            this.tweetAccum=new ArrayList<Status>();

    }

    public void setMongoConnection(MongoConnection mongo){
            this.mongoConnection=mongo;
    }
	
    public void onStatus(Status status) {

            System.out.println("@" + status.getUser().getScreenName()+" - " + status.getText() +status.getUser().getLocation());

            DBObject tweet=Operations.dbTweet(status);	
            this.mongoConnection.insert(tweet);


    }
    
    @Override
    public void onDeletionNotice(
            StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice id:"
				+ statusDeletionNotice.getStatusId());
	}

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            System.out.println("Got track limitation notice:"
                            + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
            System.out.println("Got scrub_geo event userId:" + userId
                            + " upToStatusId:" + upToStatusId);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
            System.out.println("Got stall warning:" + warning);
    }

    @Override
    public void onException(Exception ex) {
            ex.printStackTrace();
    }

}
