/**
 * Created by aviles on 10/20/2014.
 * Based on : http://alias-i.com/lingpipe/demos/tutorial/sentiment/read-me.html
 */

import com.aliasi.classify.Classification
import com.aliasi.classify.Classified
import com.aliasi.classify.DynamicLMClassifier

println "Loading training and testing data."
def trainingTweets = loadTrainingData()
def testingTweets = loadTestData()

println "Create a classifier for 'positive' and 'negative' categories with ngram of 8."
String[] categories = [ "positive", "negative" ]
int nGram = 8
def classifier = DynamicLMClassifier.createNGramProcess(categories, nGram)

println "Training the classifier model."
train(classifier, categories, trainingTweets)

println "Testing the classifier model."
test(classifier, testingTweets)

/*****************
 * Helper Methods
 */
def train(classifier, categories, tweets) throws IOException {
    // Informative variables
    def (trainingCases, trainingChars) = [0,0]

    // Classes (the NLP slang for categories)
    def classifications = [:]
    categories.each{ cat ->  classifications.put(cat, new Classification(cat)) }

    // Feed the classifier with each classified tweet
    tweets.each{ tweet ->
        ++trainingCases
        trainingChars += tweet.text.length()
        // Manually Classify
        Classified<CharSequence> classified = new Classified<CharSequence>(tweet.text,classifications[tweet.category])
        // Train
        classifier.handle(classified)
    }

    println "  Training Cases : " + trainingCases
    println "  Training Chars : " + trainingChars
}

def test(classifier, tweets) {
    def (testingCases, numCorrect) = [0,0]

    tweets.each{ tweet ->
        ++testingCases
        // Predict Class
        Classification classification = classifier.classify(tweet.text)
        if (classification.bestCategory().equals(tweet.category))
            ++numCorrect
    }

    println "  Testing Cases  : " + testingCases
    println "  Correct #      : " + numCorrect
    println "          %      : " + ((double)numCorrect)/(double)testingCases
}

/**
 * The training and testing data must be classified 'a priory'. This is usually done manually or from an existing classified (categorized) corpus.
 * In our code we expect the following object structure
 * { text: "some text", category: "positive" }
 */
def loadTrainingData(){
    // This is not a usual training set. In real life we need to feed the model a richer corpus of data.
    [
        [text: "RestaurantX is the best restaurant in town. the food presentation is beautiful and the flavors wonderful", category: "positive"],
        [text: "RestaurantY is the worst restaurant in town. the food presentation is disgusting and the flavors terrible", category: "negative"]
    ]
}
def loadTestData(){
    // This is not a usual test set. In real life we need to feed the model a richer corpus of data.
    // Testing data should not be part of the training data.
    [
        [text: "TheaterM is the best theater in town. the place is beautiful and the service is wonderful", category: "positive"],
        [text: "TheaterN is the worst theater in town. the place is disgusting and the service is terrible", category: "negative"]
    ]
}