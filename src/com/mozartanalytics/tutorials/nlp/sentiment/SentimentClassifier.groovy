/**
 * Created by aviles on 10/20/2014.
 */
package com.mozartanalytics.tutorials.nlp.sentiment

import com.aliasi.classify.Classification
import com.aliasi.classify.Classified
import com.aliasi.classify.DynamicLMClassifier

/*****************
 * START
 */
println "Training Polarity Model"
// Data
def trainingTweets = loadTrainingData()
def testTweets = loadTestData()
// Classifier
String[] categories = [ "positive", "negative" ]
int nGram = 8
def classifier = DynamicLMClassifier.createNGramProcess(categories, nGram)
// Train - Test
train(classifier, categories, trainingTweets)
evaluate(classifier, testTweets)

/*****************
 * Helper Methods
 */
def train(classifier, categories, tweets) throws IOException {
    println "Training."
    def numTrainingCases = 0
    def numTrainingChars = 0

    def classifications = [:]
    categories.each{ cat ->  classifications.put(cat, new Classification(cat)) }

    tweets.each{ tweet ->
        ++numTrainingCases
        numTrainingChars += tweet.text.length()

        Classified<CharSequence> classified = new Classified<CharSequence>(tweet.text,classifications[tweet.category])
        classifier.handle(classified)
    }

    println "  # Training Cases=" + numTrainingCases
    println "  # Training Chars=" + numTrainingChars
}

def evaluate(classifier, tweets) {
    println "Evaluating."
    int numTests = 0
    int numCorrect = 0

    tweets.each{ tweet ->
        ++numTests
        Classification classification = classifier.classify(tweet.text)
        if (classification.bestCategory().equals(tweet.category))
            ++numCorrect
    }

    println "  # Test Cases=" + numTests
    println "  # Correct=" + numCorrect
    println "  % Correct=" + ((double)numCorrect)/(double)numTests
}

def loadTrainingData(){
    []
}

def loadTestData(){
    []
}