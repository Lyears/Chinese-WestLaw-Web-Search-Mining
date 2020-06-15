package wsm.utils;

import java.util.*;

public class FuzzyQueryHandler {

    /**
     * get edit distance between two strings
     * @param str1 string 1
     * @param str2 string 2
     * @return the edit distance
     */
    public static Integer getEditDistance(String str1,String str2) {
        int equalDistance;
        int[][] distances = new int[str1.length()+1][str2.length()+1];
        distances[0][0] = 0;
        for(int i = 1; i < str1.length() + 1; i++) {
            distances[i][0] = i;
        }
        for(int j = 1; j < str2.length() + 1; j++) {
            distances[0][j] = j;
        }
        for(int i = 1; i < str1.length() + 1; i++) {
            for(int j = 1; j < str2.length() + 1; j++) {
                if (str1.charAt(i-1) == str2.charAt(j-1)) {
                    equalDistance = 0;
                } else {
                    equalDistance = 1;
                }
                distances[i][j] = Min(distances[i-1][j] + 1,
                        distances[i][j-1] + 1, distances[i-1][j-1] + equalDistance);
            }
        }
        return distances[str1.length()][str2.length()];
    }

    public static int Min(int a,int b,int c) {
        return Math.min((Math.min(a, b)), c);
    }

    /**
     * find most similar keys from a candidate sets
     * @param key the search key
     * @param threshold the threshold for searching (1 - editDistance / maxLength)
     * @param candidateSet the candidate set
     * @return a list of string for most similar keys
     */
    public static List<String> findMostSimilarKeys(String key, Float threshold,
                                                   Set<String> candidateSet) {
        ArrayList<String> feedback = new ArrayList<>();
        for (String candidate: candidateSet) {
            System.out.println((float)getEditDistance(candidate, key));
            if ( ((float)getEditDistance(candidate, key)) /
                    Math.max(candidate.length(), key.length()) <= 1.0 - threshold + 0.0001) {
                feedback.add(candidate);
            }
        }
        return feedback;
    }

    public static void main(String[] args) {

        List<String> testStringPairs = Arrays.asList(
                "distance", "distbnce7",
                "hahasillyb", "qksillyc"
        );

        for (int i = 0; i < testStringPairs.size() / 2; i++) {
            System.out.printf("Edit distance between %s and %s is %d\n",
                    testStringPairs.get(2*i), testStringPairs.get(2*i+1),
                    getEditDistance(testStringPairs.get(2*i), testStringPairs.get(2*i+1)));
        }

        List<String> testCandidates = Arrays.asList(
                "5814789225", "5814009225", "5816453699"
        );
        Set<String> testCandidateSet = new TreeSet<>();
        testCandidateSet.addAll(testCandidates);
        List<String> testKeys = Arrays.asList("5814789225");
        float threshold = (float) 0.8;

        for (String key: testKeys) {
            List<String> foundSimilarKeys = findMostSimilarKeys(key, threshold, testCandidateSet);
            System.out.printf("Similar keys for %s is %s", key, foundSimilarKeys);
        }

    }
}
